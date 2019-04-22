package org.drama.core;

import static org.drama.delegate.Delegator.action;
import static org.drama.delegate.Delegator.func;
import static org.joor.Reflect.on;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.drama.annotation.ElementProperty;
import org.drama.annotation.LayerProperty;
import org.drama.collections.ImmutableSet;
import org.drama.event.Event;
import org.drama.vo.BiParameterValueObject;
import org.drama.vo.KeyValueObject;

/**
 * Stage 和 layer 的运转内核
 * 
 * @author john
 *
 */
class DramaKernel implements Kernel {
	private static final Set<Class<? extends Event>> registeredEvents = new HashSet<>();
	private static final Map<KeyValueObject<Class<? extends Event>, LayerContainer>, Set<ElementContainer>> eventHandingPool = new HashMap<>();
	private static final Set<LayerContainer> layerContainers = new TreeSet<>();

	private Function<BiParameterValueObject<Class<? extends Layer>, LayerProperty>, Layer> layerGenerator;

	@Override
	public boolean regeisterEvent(Set<Class<? extends Event>> eventClzs) {
		if (CollectionUtils.isEmpty(eventClzs)) {
			return false;
		}

		eventClzs.forEach((eClz) -> {
			if (!registeredEvents.contains(eClz) && !eClz.equals(Event.class)) {
				registeredEvents.add(eClz);
			}
		});
		return true;
	}

	@Override
	public Layer registerElement(Element element) {
		if (CollectionUtils.isEmpty(registeredEvents)) {
			return null;
		}

		ElementProperty prop = element.getClass().getAnnotation(ElementProperty.class);

		if (Objects.isNull(prop)) {
			return null;
		}

		Class<? extends Event>[] events = prop.events();

		if (ArrayUtils.isEmpty(events)) {
			return null;
		}

		LayerContainer LayerContainer = getLayerContainer(prop);

		if (Objects.isNull(LayerContainer)) {
			return null;
		}

		// 注册全局元素
		if (ArrayUtils.contains(events, Event.class)) {
			registeredEvents.forEach((clazz) -> {
				Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
				bindElementHandler(element, prop, elemSet);
			});
		} else {
			// 注册非全局元素
			for (Class<? extends Event> clazz : events) {
				registeredEvents.forEach((registeredEvent) -> {
					if (Objects.equals(clazz, registeredEvent)) {
						Set<ElementContainer> elemSet = getElemSet(clazz, LayerContainer);
						bindElementHandler(element, prop, elemSet);
					}
				});
			}
		}

		return LayerContainer.getLayer();
	}

	protected Set<ElementContainer> getElemSet(Class<? extends Event> clazz, LayerContainer LayerContainer) {
		KeyValueObject<Class<? extends Event>, LayerContainer> idx = new KeyValueObject<>(clazz, LayerContainer);
		Set<ElementContainer> elemSet = eventHandingPool.get(idx);

		if (elemSet == null) {
			elemSet = new TreeSet<>();
			eventHandingPool.put(idx, elemSet);
		}
		return elemSet;
	}

	protected LayerContainer getLayerContainer(ElementProperty prop) {
		LayerContainer layerContainer = null;

		Class<? extends Layer> layerClazz = prop.layer();

		if (Objects.isNull(layerClazz)) {
			return layerContainer;
		}

		LayerProperty layerProp = layerClazz.getAnnotation(LayerProperty.class);

		// 如果指定的 layer 有描述注解，则按描述注解进行查找和分配
		if (Objects.nonNull(layerProp)) {
			layerContainer = getLayerContainer(layerClazz, layerProp);
		} else {
			// 根据指定的 layer 没有找到，则通过 ElementProperty 提供给的 layerInfo 进行分配
			layerProp = prop.layerInfo();
			layerContainer = getLayerContainer(layerClazz, layerProp);
		}

		if (Objects.nonNull(layerContainer) && !layerContainers.contains(layerContainer)) {
			layerContainers.add(layerContainer);
		}

		return layerContainer;
	}

	private LayerContainer getLayerContainer(final Class<? extends Layer> layerClz, final LayerProperty layerProp) {
		LayerContainer layerContainer = null;

		final UUID identity = UUID.fromString(layerProp.uuid());

		layerContainer = layerContainers.stream().filter((c) -> Objects.equals(c.getIndentity(), identity)).findFirst()
				.get();

		if (Objects.nonNull(layerContainer)) {
			return layerContainer;
		} else {
			Layer layer = null;

			if (Objects.nonNull(layerGenerator)) {
				layer = func(layerGenerator, new BiParameterValueObject<>(layerClz, layerProp));
			}

			if (Objects.isNull(layer)) {
				layer = on(layerClz).get();
			}

			if (Objects.nonNull(layer)) {
				layer.setKernel(this);

				layerContainer = new LayerContainer(layer, UUID.fromString(layerProp.uuid()));
				layerContainer.setName(layerProp.name());
				layerContainer.setPriority(layerProp.priority());
				layerContainer.setDisable(layerProp.disabled());
			}
		}

		return layerContainer;
	}

	@Override
	public void addLayerGenerator(
			Function<BiParameterValueObject<Class<? extends Layer>, LayerProperty>, Layer> generator) {
		layerGenerator = generator;
	}

	private void bindElementHandler(Element element, ElementProperty prop, Set<ElementContainer> elemSet) {
		ElementContainer elemCon = new ElementContainer(element);
		elemCon.setPriority(prop.priority());
		
		if (!elemSet.contains(elemCon)) {
			elemSet.add(elemCon);
		}
	}

	@Override
	public void notifyHandler(final Layer layer, final Event event, final Consumer<Element> onCompleted) {
		final Class<?> eventClass = event.getClass();

		eventHandingPool.keySet().stream().filter((k) -> Objects.equals(k.getValue().getLayer(), layer)
				&& !k.getValue().getDisabled() && Objects.equals(k.getKey(), eventClass)).forEach((k) -> {
					
					Set<ElementContainer> elemSet = eventHandingPool.get(k);

					if (CollectionUtils.isEmpty(elemSet)) {
						return;
					}

					for (ElementContainer elemCon : elemSet) {
						Element elem = elemCon.getInvocator();
						
						elem.handing(event);
						
						action(onCompleted, elem);
						
						if(!Objects.equals(elem.getHandingStatus(), HandingStatus.Transmit)) {
							break;
						}
					}
				});
	}

	@Override
	public final ImmutableSet<Layer> getlayers() {
		return ImmutableSet
				.newInstance(layerContainers.stream().sorted().map((c) -> c.getLayer()).collect(Collectors.toSet()));
	}
}
