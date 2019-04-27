package org.drama.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drama.annotation.ElementProperty;
import org.drama.annotation.LayerDescription;
import org.drama.annotation.LayerProperty;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.exception.DramaException;
import org.drama.vo.BiParameterValueObject;
import org.drama.vo.KeyValueObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.func;

/**
 * Stage 和 layer 的运转内核
 * 
 * @author john
 *
 */
class DramaKernel implements Kernel {
	private static final Map<Signature, DramaKernel> INSTANCE_MAP;

	static {
		INSTANCE_MAP = new ConcurrentHashMap<>();
	}

	private final Set<Class<? extends Event>> REGISTERED_EVENTS;
	private final Map<KeyValueObject<Class<? extends Event>, LayerContainer>, Set<ElementContainer>> HANDING_POOL;
	private final Set<LayerContainer> LAYER_CONTAINERS;
	private Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> layerGenerator;

	private DramaKernel() {
		REGISTERED_EVENTS = new HashSet<>();
		HANDING_POOL = new ConcurrentHashMap<>();
		LAYER_CONTAINERS = new TreeSet<>();
	}

	public static Kernel getInstance(Configuration configuration) {
		return INSTANCE_MAP.computeIfAbsent(Objects.requireNonNull(configuration.getSignature()), s -> new DramaKernel());
	}

	@Override
	public void setLayerGenerator(
			Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> generator) {

		layerGenerator = generator;
	}

	@Override
	public Function<BiParameterValueObject<Class<? extends Layer>, LayerDescriptor>, Layer> getLayerGenerator() {
		return layerGenerator;
	}

	private void bindElementHandler(Element element, ElementProperty prop, Set<ElementContainer> elemSet) {
		ElementContainer elemCon = new ElementContainer(element);
		elemCon.setPriority(prop.priority());
		elemSet.add(elemCon);
	}

	private Set<ElementContainer> getElemSet(Class<? extends Event> clazz, LayerContainer LayerContainer) {
		KeyValueObject<Class<? extends Event>, LayerContainer> idx = new KeyValueObject<>(clazz, LayerContainer);
		return HANDING_POOL.computeIfAbsent(idx, i -> new TreeSet<>());
	}

	private LayerContainer getLayerContainer(Class<? extends Layer> layerClazz) {
		LayerProperty prop = layerClazz.getAnnotation(LayerProperty.class);
		
		if(Objects.isNull(prop)) {
			throw DramaException.noSpecialLayerProp(layerClazz);
		}
		
		LayerDescriptor layerDesc = getLayerDescriptor(prop.uuid(), prop.name(), prop.priority(), prop.disabled());
		
		return getLayerContainer(layerClazz, layerDesc);		
	}

	private LayerContainer getLayerContainer(final Class<? extends Layer> clz, LayerDescriptor desc) {
		final UUID identity = UUID.fromString(desc.getUUID());
		
		LayerContainer layerContainer = null;

		for(LayerContainer layerCon : LAYER_CONTAINERS) {
			if(Objects.equals(layerCon.getIdentity(), identity)) {
				layerContainer = layerCon;
				break;
			}
		}

		if (Objects.nonNull(layerContainer)) {
			return layerContainer;
		} else {
			Layer layer = func(layerGenerator, new BiParameterValueObject<>(clz, desc));

			if (Objects.nonNull(layer)) {
				layerContainer = new LayerContainer(layer, identity, desc.getName(), desc.getPriority());
				layerContainer.setDisable(desc.getDisabled());
			}
		}

		return layerContainer;
	}

	private LayerContainer getLayerContainer(ElementProperty elemProp) {
		LayerContainer layerContainer;
		
		Class<? extends Layer> layerClazz = elemProp.layer();

		// 如果指定的 layer 有描述注解，则按描述注解进行查找和分配
		if (!Objects.equals(layerClazz, Layer.Null.class)) {
			layerContainer = getLayerContainer(layerClazz);
		} else {
			// 根据指定的 layer 没有找到，则通过 ElementProperty 提供给的 layerDesc 进行分配
			LayerDescription layerDesc = Objects.requireNonNull(elemProp.layerDesc());

			final String enumTargetName = layerDesc.target();
			
			Optional<LayerDescriptor> optDesc = 
					Arrays.stream(layerDesc.desc().getEnumConstants())
						.map(e -> (LayerDescriptor)e)
						.filter(e -> Objects.equals(e.getName(), enumTargetName))
						.findFirst();
			
			if(optDesc.isPresent()) {
				layerContainer = getLayerContainer(layerClazz, optDesc.get());
			} else {
				throw DramaException.illegalLayerDesc(layerDesc);
			}
		}

		if (Objects.nonNull(layerContainer)) {
			LAYER_CONTAINERS.add(layerContainer);
		}

		return layerContainer;
	}

	private LayerDescriptor getLayerDescriptor(
			final String uuid, final String name, final int priority, final boolean disabled) {

		return new LayerDescriptor() {
			@Override
			public boolean getDisabled() {
				return disabled;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public int getPriority() {
				return priority;
			}

			@Override
			public String getUUID() {
				return uuid;
			}
		};
	}

	@Override
	public final ImmutableSet<Layer> getlayers() {
		return ImmutableSet
				.newInstance(LAYER_CONTAINERS.stream().map(c -> c.getLayer()).collect(Collectors.toSet()));
	}

	@Override
	public void notifyHandler(final Layer layer, final Event event, final Consumer<LayerContainer> onPreHanding, final Consumer<Element> onCompleted) {
		final Class<?> eventClass = event.getClass();

		HANDING_POOL.keySet().stream().filter(k -> Objects.equals(k.getValue().getLayer(), layer)
				&& !k.getValue().getDisabled() && Objects.equals(k.getKey(), eventClass)).forEach(k -> {

					Set<ElementContainer> elemSet = HANDING_POOL.get(k);

					if (CollectionUtils.isEmpty(elemSet)) {
						return;
					}
					
					action(onPreHanding, k.getValue());

					for (ElementContainer elemCon : elemSet) {
						elemCon.setCurrentLayer(layer);
						
						Element elem = elemCon.getInvocator();

						elem.handing(event);

						action(onCompleted, elem);
						
						elemCon.setCurrentLayer(null);

						if (!Objects.equals(elem.getHandingStatus(), HandingStatus.Transmit)) {
							break;
						}
					}
				});
	}

	@Override
	public boolean regeisterEvent(Set<Class<? extends Event>> eventClzs) {
		if (CollectionUtils.isEmpty(eventClzs)) {
			return false;
		}

		eventClzs.forEach((eClz) -> {
			if (!REGISTERED_EVENTS.contains(eClz) && !eClz.equals(Event.class)) {
				REGISTERED_EVENTS.add(eClz);
			}
		});
		return true;
	}

	@Override
	public Layer registerElement(Element element) {
		if (CollectionUtils.isEmpty(REGISTERED_EVENTS)) {
			throw DramaException.emptyRegisterEvents();
		}

		ElementProperty prop = Objects.requireNonNull(element.getClass().getAnnotation(ElementProperty.class));
		LayerContainer LayerContainer = Objects.requireNonNull(getLayerContainer(prop));
		Class<? extends Event>[] events = Objects.requireNonNull(prop.events());

		// 注册全局元素
		if (ArrayUtils.contains(events, Event.class)) {
			if(events.length != 1) {
				throw DramaException.onlyGlobaleEvent(element.getClass());
			}
			REGISTERED_EVENTS.forEach(clazz -> {
				Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
				bindElementHandler(element, prop, elemSet);
			});
		} else {
			// 注册非全局元素
			for (Class<? extends Event> clazz : events) {
				REGISTERED_EVENTS.forEach(registeredEvent -> {
					if (Objects.equals(clazz, registeredEvent)) {
						Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
						bindElementHandler(element, prop, elemSet);
					}
				});
			}
		}
		return LayerContainer.getLayer();
	}
}
