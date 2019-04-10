package com.john.drama.exception;

import com.john.drama.core.Element;
import com.john.drama.core.Layer;
import com.john.drama.event.Event;
import com.john.drama.vo.BiParameterValueObject;

public class OccurredException extends Exception {
	static class ExceptionTracer<T> {
		private T target;
		
		public ExceptionTracer() {
		}
		
		public ExceptionTracer(T target) {
			setTarget(target);
		}

		public T getTarget() {
			return target;
		}

		public void setTarget(T target) {
			this.target = target;
		}
	}
	
	private static final long serialVersionUID = -7337990653787626209L;
	private ExceptionTracer<?> trace;
	
	protected OccurredException(String message) {
		super(message);
	}
	
	public OccurredException(String message, Throwable e) {
		super(message, e);
	}

	public ExceptionTracer<?> getTrace() {
		return trace;
	}

	public void setTrace(ExceptionTracer<?> trace) {
		this.trace = trace;
	}
	
	public static OccurredException occurredPlayError(Throwable e, Event event) {
		OccurredException except = new OccurredException("Occurred errer on play", e);
		except.setTrace(new ExceptionTracer<>(event));
		return except;
	}
	
	public static OccurredException occurredCastLayer(Layer layer) {
		String msg = String.format("Occurred errer on cast sliec<%s> to AbstractLayer", layer);
		OccurredException except = new OccurredException(msg);
		except.setTrace(new ExceptionTracer<>(layer));
		return except;
	}
	
	public static OccurredException illegalRegisterEvent(Class<?> event) {
		String eventName = event.getSimpleName();
		String message = String.format("Illegal event<$s>. It should be inherit AbstractEvent", eventName);
		OccurredException except = new OccurredException(message);
		except.setTrace(new ExceptionTracer<>(event));
		return except;
	}
	
	public static OccurredException illegalBroadcastEvent(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String msg = String.format("Illegal broadcast event on layer<%s>", layerName);
		OccurredException except = new OccurredException(msg);
		except.setTrace(new ExceptionTracer<>(new BiParameterValueObject<>(layer, event)));
		return except;
	}
	
	public static OccurredException occurredHandingError(Throwable e, Layer layer, Element elem) {
		String layerName = layer.getClass().getSimpleName();
		String elemName = elem.getClass().getSimpleName();
		String msg = String.format("Element occurring error on handing. Layer<%s>-element<%s>", layerName, elemName);
		OccurredException except = new OccurredException(msg, e);
		except.setTrace(new ExceptionTracer<>(new BiParameterValueObject<>(layer, elem)));
		return except;
	}
}
