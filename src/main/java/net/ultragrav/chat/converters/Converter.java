package net.ultragrav.chat.converters;

import net.ultragrav.chat.components.Component;

public interface Converter<T> {
    T convert(Component component);
    Component convert(T other);
}
