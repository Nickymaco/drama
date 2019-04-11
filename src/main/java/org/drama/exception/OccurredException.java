package org.drama.exception;

import org.drama.common.MessageTemplate;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.vo.BiParameterValueObject;

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
		OccurredException except = new OccurredException(MessageTemplate.inst().getExPlayError(), e);
		except.setTrace(new ExceptionTracer<>(event));
		return except;
	}
	
	public static OccurredException occurredCastLayer(Layer layer) {
		String msg = String.format(MessageTemplate.inst().getExCastLayer(), layer);
		OccurredException except = new OccurredException(msg);
		except.setTrace(new ExceptionTracer<>(layer));
		return except;
	}
	
	public static OccurredException illegalRegisterEvent(Class<?> event) {
		String eventName = event.getSimpleName();
		String message = String.format(MessageTemplate.inst().getExIllegalEvent(), eventName);
		OccurredException except = new OccurredException(message);
		except.setTrace(new ExceptionTracer<>(event));
		return except;
	}
	
	public static OccurredException illegalBroadcastEvent(Layer layer, Event event) {
		String layerName = layer.getClass().getSimpleName();
		String msg = String.format(MessageTemplate.inst().getExIllegalBoradcast(), layerName);
		OccurredException except = new OccurredException(msg);
		except.setTrace(new ExceptionTracer<>(new BiParameterValueObject<>(layer, event)));
		return except;
	}
	
	public static OccurredException occurredHandingError(Throwable e, Layer layer, Element elem) {
		String layerName = layer.getClass().getSimpleName();
		String elemName = elem.getClass().getSimpleName();
		String msg = String.format(MessageTemplate.inst().getExElemHandingError(), layerName, elemName);
		OccurredException except = new OccurredException(msg, e);
		except.setTrace(new ExceptionTracer<>(new BiParameterValueObject<>(layer, elem)));
		return except;
	}
}
