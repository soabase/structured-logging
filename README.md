# Structured Logging

Per Thoughtworks (https://www.thoughtworks.com/radar/techniques/structured-logging) we should log in a structured manner

...

## TL;DR

### With Structured Logging And Preprocessor

```java
@LoggerSchema({Id.class, Qty.class, Event.class})  // note: the library preprocessor generates the schema
public class MyClass {
    private static final StructuredLogger<MyClassSchema> log = StructuredLoggerFactory.structured(MyClassSchema.class);  // note: the library auto-generates the schema instance class

    private void myOperation(String id, String eventName, int qty) {
    ...
    
        log.info("Something happened", schema -> schema.event(eventName).id(id).qty(qty));
    }
}
```

Logs similar to: `id=7892323 event=EventName qty=100 Something happened`

### With Just Structured Logging

```java

public interface LogSchema {
    LogSchema id(String id);
    
    LogSchema qty(int qty);
    
    LogSchema event(String name);
}

...

StructuredLogger<LogSchema> log = StructuredLoggerFactory.structured(LogSchema.class);  // note: the library auto-generates the schema instance class

...

private void myOperation(String id, String eventName, int qty) {
    ...
    
    log.info("Something happened", schema -> schema.event(eventName).id(id).qty(qty));
}
```

Logs similar to: `id=7892323 event=EventName qty=100 Something happened`

### Without Structured Logging

```java
Logger log = LoggerFactory.getLogger(...);

...

private void myOperation(String id, String eventName, int qty) {
    ...
    
    log.info("Something happened where id={} eventnme={}and qty = {}", id, qty, eventName);    // note mistakes misspellings
}
```

### Create a Registry With All the Logging Schema For Your Project

```java
@LoggerSchemas({
        @LoggerSchema(value = {Id.class, Code.class}, schemaName = "LoggingSchemaIdCode"),
        @LoggerSchema(value = {Id.class, Event.class}, schemaName = "LoggingSchemaIdEvent"),
        @LoggerSchema(value = Id.class, schemaName = "LoggingSchemaId"),
        @LoggerSchema(value = {Id.class, CustomSchema.class}, schemaName = "LoggingSchemaIdCustom")
})
public class LoggingSchema {
}
```

## Under The Hood

- The schema concrete instance is generated from the interface using Byte Buddy here: [Generator.java](https://github.com/soabase/structured-logging/blob/master/structured-logger-core/src/main/java/io/soabase/structured/logger/generation/Generator.java)
- The logging facade forwards directly to SLF4J (or whatever). This is not a new logging library.
- If writing a little interface schema is too much trouble, there's a preprocessor that will generate one from "mixins". See the example here: [TestGenerated.java](https://github.com/soabase/structured-logging/blob/master/structured-logger-generator-test/src/test/java/io/soabase/structured/logger/TestGenerated.java)
- The "message" that is passed to SLF4J that has the replacement tokens is produced via a proc that can be changed. We could develop standard ones that do validation, etc.
