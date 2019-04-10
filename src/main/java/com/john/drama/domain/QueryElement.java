package com.john.drama.domain;

import com.john.drama.core.Broken;
import com.john.drama.core.Element;
import com.john.drama.core.Layer;
import com.john.drama.event.Event;
import com.john.drama.event.EventArgument;
import com.john.drama.event.EventResult;
import com.john.drama.event.EventResultValue;
import com.john.drama.vo.KeyValueObject;

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
            String keyName = String.format("%s::%s", event.getClass().getSimpleName(), objectClass.getSimpleName());
            eventResult.addResult(keyName, new EventResultValue(new KeyValueObject<>("QueryResult", queryResult)));
        }
    }

    protected void setCancelable(Broken cancelable) {
        this.cancelable = cancelable;
    }
}
