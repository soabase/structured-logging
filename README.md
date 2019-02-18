# Structured Logging

Per Thoughtworks (https://www.thoughtworks.com/radar/techniques/structured-logging) we should log in a structured manner

...

### Without Structured Logging

```java
Logger log = LoggerFactory.getLogger(...);

...

private void myOperation(String id, String eventName, int qty) {
    ...
    
    log.info("Something happened where id={} eventname={}and qty = {}", id, qty, eventName);    // note mistakes misspellings
}
```

### With Structured Logging

```java

public interface LogSchema extends Id<LogSchema>, Event<LogSchema>, Qty<LogSchema>{}

...

StructuredLogger<LogSchema> log = StructuredLoggerFactory.structured(LogSchema.class);  // note: the library auto-generates the schema instance class

...

private void myOperation(String id, String eventName, int qty) {
    ...
    
    log.info("Something happened", schema -> schema.event(eventName).id(id).qty(qty));
}
```

Logs similar to: `id=7892323 event=EventName qty=100 Something happened`
