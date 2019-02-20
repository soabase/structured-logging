package io.soabase.structured.logger;

import java.math.BigInteger;

public interface CustomSchema<T extends CustomSchema<T>> {
    T value(BigInteger i);
}
