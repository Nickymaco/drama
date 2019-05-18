# Drama

**Hey guys, when you are reading this document point out the syntax error if you found, thanks. If you interesting this project, fork and join me build togther** :-D

## Introdution 

Drama is a lightweight develop libaray base on event driving. It does not replace any framework what you are using. It just add an event driving logic scene on the application and help you build N-Layers logic code struct.  <br /><br />Your 90% of applicatoins maybe base on spring framework and the code struct could be three layers at least, it looks like “DAO Layer”, “Service Layer”, “Presentation Layer”. It’s greate in a simple project, but when become large, it created more and more module packages and you could see that too much services in a mvc controller or too much models or dao in a service. So A lot of modules had duplication code and strong coupling and difficult to controll the business flow etc. Coding some would be need to modify many other associated code in a same time. OMG !  <br /><br />You can try drama to build a new logic code struct. It will help you focused event data flow and reduce “layer struct” hard code design. Core objects on below:

+ Event: encapsulate parameters or data which you focused. 
+ Stage: receive events and notify the layer objects. At the end stage will be output a render object.
+ layer: a logic layer object, broadcast event to the element objects. 
+ Element: handing the events which it listening. 

For example, assume that you are developing a application with spring mvc framework. In a controller method, you can new an event object and then invoke the stage object to receive and start play. The event will be broadcasted by the logic layers in an order. In current layer any element if listening the event will be handing in an order prioriy. It looks like the image on below:

<img src="https://raw.githubusercontent.com/Nickymaco/drama/master/images/drama.png" style="height:400px;width:500px" />

## Request

**Java 1.8+**

## Usage

### Build a stage
```java
@Bean
public org.drama.core.Configuration configuration(
		LoggingFactory loggingFactory, final List<Element> elements, JohncrawlerProperties properties) {
    // any stage object need a configuration
	DramaConfiguration config = new DramaConfiguration();
    // set a element register factory
	config.setRegisterElementFactory(() -> new HashSet<>(elements));
    // set a logging
	config.setLoggingFactory(loggingFactory);
    // set a register event path for scan
	config.setRegisterEventPackage("org.drama.events");
	return config;
}

@Bean
public Stage stage(org.drama.core.Configuration configuration) throws DramaException {
	DramaStage stage = new DramaStage();
    // important! this is help stage initially
	stage.setup(configuration);
	return stage;
}
```

### Element
Just need to implements the ‘Element’ interface.  The annotation “ElementProperty” is important, it tell the stage that element will be registered to which logic layer and listened which events. 
```java
@ElementProperty(any = true, layerDesc = @LayerDescription(desc = LogicLayer.class, target = "Vallidated"))
public class TokenElem implements Element
```

### Layer
Usually, logic layer doesn't need to be created. You just only create a logic layer enumerator and implements LayerDescriptor interface and be indicated in ‘ElementProperty’ annotation.
```java
public enum LogicLayer implements LayerDescriptor {
	DataAccess("DataAccess", 10, "DB640CCD-E18D-442B-BF16-BC54DB6A4A8", false, null),
	Validated("Vallidated", 0, "F16727D8-CE44-4B25-8792-593E7705576E", false, null);
	
	private final String name;
	private final int priority;
	private final String uuid;
	private final boolean disable;
	private final String[] excludeEvent;
	
	LogicLayer(String name, int priority, String uuid, boolean disable, String[] events) {
		this.name = name;
		this.priority = priority;
		this.uuid = uuid;
		this.disable = disable;
		this.excludeEvent = events;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPriority() {
		return priority;
	}


	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public boolean getDisabled() {
		return this.disable;
	}

	@Override
	public String[] getExculdeEvent() {
		return excludeEvent;
	}
}
```
Or you want to custom a logic layer, you can implements the Layer interface or inherit the DramaLayer object override broadcast method. And add a LayerProperty annotation. At the end set the ElementProperty annotation 'layer()' property.
```java
@LayerProperty(name="Some", priority=0, uuid="9B092786-C66B-49B4-8535-0D0EA4D900D2")
public class SomeLayer implements Layer
```

### Event
Usually, it just need to special a string name of registered event and tell the Stage start play. If you want to custom event, please implements the Event interface or inherit the DramaEvent object override which method you want. The EventProperty annotation mean that which event using this object instance.
```java
// Stage instance play a event
stage.wizard().event("CrawlingEvent").play();

// custom event
@EventProperty(aliasFor = {"SomeEvent"})
public class SomeEvent implements Event
or 
@EventProperty(aliasFor = {"SomeEvent"})
public class SomeEvent extends DramaEvent
// Stage instance play a event
stage.wizard().event(SomeEvent.class).play();
```

## License

**This project is licensed under the MIT license. See the [LICENSE](LICENSE) file for more info.**