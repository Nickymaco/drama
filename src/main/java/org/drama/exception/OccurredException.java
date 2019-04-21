package org.drama.exception;

import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;

public class OccurredException extends Exception {
	public static final String EmptyEventMsg = "Without any event registered";
	public static final String EmptyElemensMsg = "Without any elemen registered";
	public static final String ErrorRegisterEventMsg = "Event registered occurred error";
	public static final String ErrorRegisterElemensMsg = "Elemen registered  occurred error";
	public static final	String PlayErrorMsg = "Occurred errer on play";
	public static final String IllegalEventMsg = "Illegal event<$s>. It should be inherit AbstractEvent";
	public static final String IllegalBroadcastMsg = "Illegal broadcast event on layer<%s>";
	public static final String ElemHandingError = "Element occurring error on handing. Layer<%s>-element<%s>";
	
	private static final long serialVersionUID = -7337990653787626209L;
	
	protected OccurredException(String message) {
		super(message);
	}
	
	protected OccurredException(String message, Throwable e) {
		super(message, e);
	}

	
	public static OccurredException emptyRegisterEvents() {
		OccurredException except = new OccurredException(EmptyEventMsg);
		return except;
	}
	
	public static OccurredException emptyRegisterElements() {
		OccurredException except = new OccurredException(EmptyElemensMsg);
		return except;
	}
	
	public static OccurredException errorRegisterEvents() {
		OccurredException except = new OccurredException(ErrorRegisterEventMsg);
		return except;
	}
	
	public static OccurredException errorRegisterElements() {
		OccurredException except = new OccurredException(ErrorRegisterElemensMsg);
		return except;
	}
	
	public static OccurredException occurredPlayError(Throwable e, Event event) {
		OccurredException except = new OccurredException(PlayErrorMsg, e);
		return except;
	}
	
	public static OccurredException illegalRegisterEvent(Class<?> event) {
		String eventName = event.getSimpleName();
		String message = String.format(IllegalEventMsg, eventName);
		OccurredException except = new OccurredException(message);
		return except;
	}
	
	public static OccurredException illegalBroadcastEvent(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String msg = String.format(IllegalBroadcastMsg, layerName);
		OccurredException except = new OccurredException(msg);
		return except;
	}
	
	public static OccurredException occurredHandingError(Throwable e, Layer layer, Element elem) {
		String layerName = layer.getClass().getSimpleName();
		String elemName = elem.getClass().getSimpleName();
		String msg = String.format(ElemHandingError, layerName, elemName);
		OccurredException except = new OccurredException(msg, e);
		return except;
	}
}
