package org.drama.domain;

import static org.drama.delegate.Delegator.action;

import java.util.function.Consumer;

import org.drama.core.Broken;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.vo.BiParameterValueObject;

public abstract class QueryElement implements Element {
    private Broken cancelable = Broken.None;
    private QueryFactory queryFactory;
    private Consumer<BiParameterValueObject<Event, Object>> resultRenderHandler;

    public QueryElement(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Broken cancelable() {
        return this.cancelable;
    }

    @Override
    public void handing(Event event, Layer layer) {
        Object queryResult = doQuery(event);

        action(getResultRenderHandler(), new BiParameterValueObject<>(event, queryResult));
    }

	protected Object doQuery(Event event) {
		EventArgument<?> argument = event.getArgument();
        Object object = argument.getArgument();

        if(!(object instanceof Operate)) {
           return null;
        }

        Class<?> objectClass= object.getClass();
        Queriable<Object> queriable = queryFactory.getQuerier(objectClass);

        if(queriable == null) {
            return null;
        }

        Object queryResult = null;
        Operate operate = (Operate)object;
        
        switch (operate.operate()) {
            case Create:
                queryResult = queriable.create(object);
                break;
            case Delete:
                queryResult = queriable.delete(object);
                break;
            case Update:
                queryResult = queriable.update(object);
                break;
            case Retrieve:
                queryResult = queriable.retrieve(object);
                break;
            case RetrieveList:
                queryResult = queriable.retrieveList(object);
                break;
        }
        
		return queryResult;
	}

	protected void setCancelable(Broken cancelable) {
        this.cancelable = cancelable;
    }

	public Consumer<BiParameterValueObject<Event, Object>> getResultRenderHandler() {
		return resultRenderHandler;
	}

	protected void setResultRenderHandler(Consumer<BiParameterValueObject<Event, Object>> resultRenderHandler) {
		this.resultRenderHandler = resultRenderHandler;
	}
}
