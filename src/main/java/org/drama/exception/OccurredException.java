package org.drama.exception;

import java.util.Objects;

import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;

public class OccurredException extends RuntimeException {
	static final String EMPTY_EVENT_MSG = "Without any event registered";
	static final String EMPTY_ELEMENS_MSG = "Without any elemen registered";
	static final String ERROR_REGISTER_EVENT_MSG = "Event registered occurred error";
	static final String ERROR_REGISTER_ELEMENS_MSG = "Elemen registered  occurred error";
	static final String PLAYE_RROR_MSG = "Occurred errer on play";
	static final String ILLEGAL_EVENT_MSG = "Illegal event<%s>. It should be inherited AbstractEvent";
	static final String ILLEGAL_BROADCAST_MSG = "Illegal broadcast event on layer<%s>";
	static final String ELEM_HANDING_ERROR = "Element occurring error on handing. Layer<%s>-element<%s>";
	static final String ONLY_GLOBALE_EVENT_MSG = "Element<%s> register more event, Global Event only. don't need any other event";
	static final String NO_SPECIAL_LAYER_PROP_MSG = "Layer<%s> must special a LayerProperty annotaioin";
	
	private static final long serialVersionUID = -7337990653787626209L;
	
	protected OccurredException(String message) {
		super(message);
	}
	
	protected OccurredException(String message, Throwable e) {
		super(message, e);
	}

	
	public static OccurredException emptyRegisterEvents() {
		return new OccurredException(EMPTY_EVENT_MSG);
	}
	
	public static OccurredException emptyRegisterElements() {
		return new OccurredException(EMPTY_ELEMENS_MSG);
	}
	
	public static OccurredException errorRegisterEvents() {
		return new OccurredException(ERROR_REGISTER_EVENT_MSG);
	}
	
	public static OccurredException errorRegisterElements() {
		return new OccurredException(ERROR_REGISTER_ELEMENS_MSG);
	}
	
	public static OccurredException occurredPlayError(Throwable e, Event event) {
		return new OccurredException(PLAYE_RROR_MSG, e);
	}
	
	public static OccurredException illegalRegisterEvent(Class<?> event) {
		String eventName = event.getSimpleName();
		String message = String.format(ILLEGAL_EVENT_MSG, eventName);
		return new OccurredException(message);
	}
	
	public static OccurredException illegalBroadcastEvent(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String msg = String.format(ILLEGAL_BROADCAST_MSG, layerName);
		
		return Objects.isNull(event) ? new OccurredException(msg, new NullPointerException()) : new OccurredException(msg);
	}
	
	public static OccurredException occurredHandingError(Throwable e, Layer layer, Element elem) {
		String layerName = layer.getClass().getSimpleName();
		String elemName = elem.getClass().getSimpleName();
		String msg = String.format(ELEM_HANDING_ERROR, layerName, elemName);
		return new OccurredException(msg, e);
	}
	
	public static OccurredException onlyGlobaleEvent(Class<? extends Element> elem) {
		String message = String.format(ONLY_GLOBALE_EVENT_MSG, elem.getName());
		return new OccurredException(message);
	}
	
	public static OccurredException noSpecialLayerProp(Class<? extends Layer> layer) {
		return new OccurredException(String.format(NO_SPECIAL_LAYER_PROP_MSG, layer.getName()));
	}
}
