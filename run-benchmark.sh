#!/bin/bash

echo compiling...
mvn -q clean compile
echo packaging...
mvn -q -pl structured-logger-benchmark package
echo running benchmark...
java -jar structured-logger-benchmark/target/benchmarks.jar
