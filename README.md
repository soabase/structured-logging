[![Build Status](https://api.travis-ci.org/soabase/structured-logging.svg?branch=master)](https://travis-ci.org/soabase/structured-logging)
[![Maven Central](https://img.shields.io/maven-central/v/io.soabase.structured-logger/structured-logger-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cstructured-logger)

# Structured Logging

[Per Thoughtworks](https://www.thoughtworks.com/radar/techniques/structured-logging) we should log in a structured manner...

[Per Splunk](http://dev.splunk.com/view/logging/SP-CAAAFCK): "Use clear key-value pairs. One of the most powerful features of Splunk software is its ability to extract fields from events when you search, creating structure out of unstructured data."

[Per Elasticsearch](https://www.elastic.co/blog/structured-logging-filebeat): "[Logging] works best when the logs are pre-parsed in a structured object, so you can search and aggregate on individual fields." It can already be done in Go or Python so why not Java?

If you export your logs to a centralized indexer, structuring your logging will make the indexer's job much easier and you will be able to get more and better information out of your logs. Manual structured logging is error prone and requires too much discipline. We can do better.

### With Structured Logging And Preprocessor

```java
@LoggerSchema({Id.class, Qty.class, Event.class})  // note: the library preprocessor generates the schema
public class MyClass {
    private static final StructuredLogger<MyClassSchema> log = StructuredLoggerFactory.structured(MyClassSchema.class);  // note: the library auto-generates the schema instance class

    private void myOperation() {
    ...
    
        log.info("Something happened", schema -> schema.event("creation event").id(10064).qty(100));
    }
}
```

Logs similar to: `id="10064" event="creation event" qty="100" Something happened`

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

private void myOperation() {
    ...
    
        log.info("Something happened", schema -> schema.event("creation event").id(10064).qty(100));
}
```

Logs similar to: `id="10064" event="creation event" qty="100" Something happened`

### Without Structured Logging

```java
Logger log = LoggerFactory.getLogger(...);

...

private void myOperation(String id, String eventName, int qty) {
    ...
    
        // note mistakes misspellings
    log.info("Something happened where id={} eventnme={}and qty = {}", id, qty, eventName);
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

### Make Some Values Required

If you would like to require certain schema values to not be omitted, annotate them with `@Required`. E.g.

```java
public interface MySchema {
    @Required
    MySchema auth(String authValue);
}
```

The Structured Logger will throw `MissingSchemaValueException` if no value is set for required values. Note: if you want to only use this in development or pre-production, you can globally disable required value checking by calling `StructuredLoggerFactory.setRequiredValuesEnabled(false)`.

### Change Ordering

By default, schema values are output in alphabetical order. Add `@SortOrder` annotations to change this. E.g.

```java
public interface SchemaWithSort {
    SchemaWithSort id(String id);

    SchemaWithSort bool(boolean b);

    @SortOrder(1)
    SchemaWithSort qty(int qty);

    @SortOrder(0)
    SchemaWithSort zero(int z);
}
```
This schema will be output ala: `zero=xxx qty=xxx bool=xxx id=xxx`

### Formatting

The formatting of the log message is customizable. Two formatters are provided, `DefaultLoggingFormatter` and `GelfLoggingFormatter`.

_DefaultLoggingFormatter_

The DefaultLoggingFormatter formats the log in `field=value` pairs and has several options. Values can be quoted and/or escaped and the log main message can appear at the beginning or the end of the log string.

_GelfLoggingFormatter_

The GelfLoggingFormatter formats in the [GELF](http://docs.graylog.org/en/2.5/pages/gelf.html) 1.1 JSON format.

You change the logging formatter used by default by calling `StructuredLoggerFactory.setDefaultLoggingFormatter(...)`. You can also specify a logging formatter when creating structured logger instances.

## Under The Hood

- The schema concrete instance is generated from the interface using Byte Buddy here: [Generator.java](https://github.com/soabase/structured-logging/blob/master/structured-logger-core/src/main/java/io/soabase/structured/logger/generation/Generator.java)
  - Don't like generators/ByteBuddy? Write your own: [SPI](https://github.com/soabase/structured-logging/tree/master/structured-logger-core/src/main/java/io/soabase/structured/logger/spi)
- The logging facade forwards directly to SLF4J (or whatever). This is not a new logging library.
- If writing a little interface schema is too much trouble, there's a preprocessor that will generate one from "mixins". See the example here: [TestGenerated.java](https://github.com/soabase/structured-logging/blob/master/structured-logger-generator-test/src/test/java/io/soabase/structured/logger/TestGenerated.java)
- You can create "factories" that generate multiple/all of your logging schema in one place. Example here: [SampleSchemaFactory.java](https://github.com/soabase/structured-logging/blob/master/structured-logger-generator-test/src/test/java/io/soabase/structured/logger/SampleSchemaFactory.java)
- A set of common "mixin" logging schema are included: [Included Schema](https://github.com/soabase/structured-logging/tree/master/structured-logger-core/src/main/java/io/soabase/structured/logger/schemas)
- Very small (< 50K) library with minimal third party dependencies (only ByteBuddy and SLF4J)

## Performance / Benchmarks

Performance is nearly identical to raw SLF4J:

```
Ops/s            Min    Avg    Max   
Raw SLF4J:     301711 305972 310233
Structured:    273331 293237 313143
(Results obtained via JMH)
```


## Usage

| Group ID | Artifact ID | Description |
| -------- | ----------- | ----------- |
| `io.soabase.structured-logger` | `structured-logger-core` | The main library. You will also need to add SLF4J to your dependency manager |
| `io.soabase.structured-logger` | `structured-logger-generator` | Annotation processor for building Schema. Make sure your IDE or build tool has annotation processing enabled |

