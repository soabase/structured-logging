package io.soabase.structured.logger.generation;

@SuppressWarnings("WeakerAccess")
public class Instance {
    public Object[] arguments;

    public void _InternalSetValueAtIndex(int index, Object value) {
        arguments[index] = value;
    }

    public void _InternalFormattedAtIndex(int index, String format, Object[] args) {
        _InternalSetValueAtIndex(index, String.format(format, args));
    }
}
