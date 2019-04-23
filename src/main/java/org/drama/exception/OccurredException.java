package org.drama.exception;

import java.util.Objects;

import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;

public class OccurredException extends RuntimeException {
	static final String EmptyEventMsg = "Without any event registered";
	static final String EmptyElemensMsg = "Without any elemen registered";
	static final String ErrorRegisterEventMsg = "Event registered occurred error";
	static final String ErrorRegisterElemensMsg = "Elemen registered  occurred error";
	static final String PlayErrorMsg = "Occurred errer on play";
	static final String IllegalEventMsg = "Illegal event<%s>. It should be inherited AbstractEvent";
	static final String IllegalBroadcastMsg = "Illegal broadcast event on layer<%s>";
	static final String ElemHandingError = "Element occurring error on handing. Layer<%s>-element<%s>";
	static final String OnlyGlobaleEventMsg = "Element<%s> register more event, Global Event only. don't need any other event";
	static final String NoSpecialLayerPropMsg = "Layer<%s> must special a LayerProperty annotaioin";
	
	private static final long serialVersionUID = -7337990653787626209L;
	
	protected OccurredException(String message) {
		super(message);
	}
	
	protected OccurredException(String message, Throwable e) {
		super(message, e);
	}

	
	public static OccurredException emptyRegisterEvents() {
		return new OccurredException(EmptyEventMsg);
	}
	
	public static OccurredException emptyRegisterElements() {
		return new OccurredException(EmptyElemensMsg);
	}
	
	public static OccurredException errorRegisterEvents() {
		return new OccurredException(ErrorRegisterEventMsg);
	}
	
	public static OccurredException errorRegisterElements() {
		return new OccurredException(ErrorRegisterElemensMsg);
	}
	
	public static OccurredException occurredPlayError(Throwable e, Event event) {
		return new OccurredException(PlayErrorMsg, e);
	}
	
	public static OccurredException illegalRegisterEvent(Class<?> event) {
		String eventName = event.getSimpleName();
		String message = String.format(IllegalEventMsg, eventName);
		return new OccurredException(message);
	}
	
	public static OccurredException illegalBroadcastEvent(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String msg = String.format(IllegalBroadcastMsg, layerName);
		
		return Objects.isNull(event) ? new OccurredException(msg, new NullPointerException()) : new OccurredException(msg);
	}
	
	public static OccurredException occurredHandingError(Throwable e, Layer layer, Element elem) {
		String layerName = layer.getClass().getSimpleName();
		String elemName = elem.getClass().getSimpleName();
		String msg = String.format(ElemHandingError, layerName, elemName);
		return new OccurredException(msg, e);
	}
	
	public static OccurredException onlyGlobaleEvent(Class<? extends Element> elem) {
		String message = String.format(IllegalEventMsg, elem.getName());
		return new OccurredException(message);
	}
	
	public static OccurredException noSpecialLayerProp(Class<? extends Layer> layer) {
		return new OccurredException(String.format(NoSpecialLayerPropMsg, layer.getName()));
	}
}
