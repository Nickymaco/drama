package org.drama.domain;

import org.drama.core.Broken;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventResult;
import org.drama.event.EventResultIndex;
import org.drama.event.EventResultValue;
import org.drama.vo.KeyValueObject;

public abstract class QueryElement implements Element {
    private Broken cancelable = Broken.None;
    private QueryFactory queryFactory;

    public QueryElement(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Broken cancelable() {
        return this.cancelable;
    }

    @Override
    public void handing(Event event, Layer layer) {
        EventArgument<?> argument = event.getArgument();
        Object object = argument.getArgument();

        if(!(object instanceof Operate)) {
           return;
        }

        Class<?> objectClass= object.getClass();
        Queriable<Object> queriable = queryFactory.getQuerier(objectClass.getSimpleName());

        if(queriable == null) {
            return;
        }

        Operate operate = (Operate)object;
        Object queryResult = null;
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

        if(queryResult == null) {
            return;
        }

        EventResult eventResult = event.getEventResult();

        if(eventResult != null) {
        	EventResultIndex index = new EventResultIndex(event.getClass(), this.getClass());
            eventResult.addResult(index, new EventResultValue(new KeyValueObject<>("QueryResult", queryResult)));
        }
    }

    protected void setCancelable(Broken cancelable) {
        this.cancelable = cancelable;
    }
}
