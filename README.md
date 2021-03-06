# Drama(a lightweight event driving development libaray)

**Hey guys, reading this document point out the syntax error where you found, thanks. Fork if you interesting this project and hope you enjoy** :-D

## Introduction 

Drama is a lightweight development libaray base on event driving. It does not replace any framework what you are using. It just add an event driving logic scene on the application and help you build N-Layers logic code struct.  <br /><br />Your 90% of applicatoins maybe base on spring framework and the code struct has three logic layers normally, it looks like “DAO Layer”, “Service Layer”, “Presentation Layer”. It’s greate in a simple project, but when become large, it created more and more module packages and you could see that too many services in a mvc controller or too many models or dao in a service. A lot of modules have duplication code and strong coupling and difficult to control business flow. Coding one would be need to modify many other associated code in a same time. OMG !  <br /><br />You can try drama to build a new logic code struct. It will help you focused event data flow and reduce logic layer hard code struct design. Core objects on below:

+ Event: Encapsulate parameters or data which you focused. 
+ Stage: Receive events and notify logic layer objects. At the end stage will be output a render object.
+ Layer: A logic layer object that broadcast event to the element objects. 
+ Element: Handing the events which it listening. 

For example, assume that you are developing a application with spring mvc framework. In a controller method that can new an event instance and invoke stage to receive and start play. Event will be broadcasted in a sequence logic layers. Any element listening the current event will execute handing by it’s prioriy. It looks like the image on below:

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
Just need to implements the `Element` interface. The annotation `ElementProperty` is important that tell the stage element will be registered to which logic layer and which events be listening. 
```java
@ElementProperty(any = true, layerDesc = @LayerDescription(desc = LogicLayer.class, target = "Vallidated"))
public class TokenElem implements Element
```

### Layer
Usually, logic layer doesn't need to be created. You just only create a logic layer enumerator and implements `LayerDescriptor` interface and set `ElementProperty` annotation desc and target property.
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
Custom a logic layer just implements the `Layer` interface or inherit the `DramaLayer` object override broadcast method. Add a LayerProperty annotation and set the `ElementProperty` annotation 'layer' property.
```java
@LayerProperty(name="Some", priority=0, uuid="9B092786-C66B-49B4-8535-0D0EA4D900D2")
public class SomeLayer implements Layer
```

### Event
Usually, it just need to special a string name of registered event and tell the Stage start play. Custom event please implements the `Event` interface or inherit the `DramaEvent` object override which method you want. The `EventProperty` annotation mean that which event using this object instance.
```java
// Stage instance play a event
stage.wizard().event("SomeEvent").play();

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