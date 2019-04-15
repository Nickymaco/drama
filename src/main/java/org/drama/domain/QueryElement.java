package org.drama.domain;

import org.drama.core.Broken;
import org.drama.core.Element;
import org.drama.core.Layer;
import org.drama.event.Event;
import org.drama.event.EventArgument;
import org.drama.event.EventResult;
import org.drama.event.EventResultBuilder;

public abstract class QueryElement implements Element {
    private Broken cancelable = Broken.None;
    private QueryFactory queryFactory;
    private EventResultBuilder resultBuild;

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
        Queriable<Object> queriable = queryFactory.getQuerier(objectClass);

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

        RenderResult(event.getEventResult(), queryResult);
    }

    protected void RenderResult(EventResult eventResult, Object queryResult) {
    	if(queryResult == null || eventResult == null) {
			return;
		}
		
		Class<? extends Event> evetMeta = eventResult.getEvent().getClass();
		Class<?> sourceMeta = this.getClass();
		String namespace = getNamespace();
		String group = getGroup();
		String artifact = getArtifact();
		Boolean output = getOutput(eventResult.getEvent(), queryResult);
		
		getResultBuild()
			.setNamespace(namespace)
			.setArtifact(artifact)
			.setGroup(group)
			.setEventMeta(evetMeta)
			.setSourceMeta(sourceMeta)
			.setResult(queryResult)
			.build(eventResult, output);
    }

	protected Boolean getOutput(Event event, Object queryResult) {
		return true;
	}

	protected abstract String getArtifact();

	protected abstract String getGroup();

	protected abstract String getNamespace();

	protected void setCancelable(Broken cancelable) {
        this.cancelable = cancelable;
    }

	public EventResultBuilder getResultBuild() {
		if(resultBuild == null) {
			resultBuild = new EventResultBuilder();
		}
		return resultBuild;
	}

	protected void setResultBuild(EventResultBuilder resultBuild) {
		this.resultBuild = resultBuild;
	}
}
