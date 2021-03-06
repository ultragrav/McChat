package net.ultragrav.chat.builder;

import net.ultragrav.chat.components.Component;
import net.ultragrav.chat.components.TextComponent;
import net.ultragrav.chat.converters.Converter;
import net.ultragrav.chat.converters.FullLegacyConverter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;

public class ChatBuilder {
    private Component comp;
    private String lastColours = "";

    /**
     * Create a blank ChatBuilder
     */
    public ChatBuilder() {
        comp = TextComponent.of("");
    }

    /**
     * Create a ChatBuilder from some text
     *
     * @param text Text
     */
    public ChatBuilder(String text) {
        comp = fromString(text);
        //this.lastColours = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * Convert a String to a colorized TextComponent
     *
     * @param text String
     * @return TextComponent
     */
    private Component fromString(String text) {
        return FullLegacyConverter.INSTANCE.convert(text);
    }

    /**
     * Add a TextComponent to this
     *
     * @param component Component
     * @return this
     */
    public ChatBuilder addComponent(Component component) {
        comp.addExtra(component);
        return this;
    }

    /**
     * Add some text
     *
     * @param str Text
     * @return this
     */
    public ChatBuilder addText(String str) {
        return addText(str, null, null);
    }

    /**
     * Add the contents of another ChatBuilder
     *
     * @param builder ChatBuilder
     * @return this
     */
    public ChatBuilder addBuilder(ChatBuilder builder) {
        return addComponent(builder.comp);
    }

    /**
     * Add some text with a ClickEvent
     *
     * @param str Text
     * @param e   ClickEvent
     * @return this
     */
    public ChatBuilder addText(String str, ClickEvent e) {
        return addText(str, null, e);
    }

    /**
     * Add some text with a HoverEvent
     *
     * @param str Text
     * @param e   HoverEvent
     * @return this
     */
    public ChatBuilder addText(String str, HoverEvent e) {
        return addText(str, e, null);
    }

    /**
     * Add some text with a ClickEvent and HoverEvent
     *
     * @param str Text
     * @param e   HoverEvent
     * @param e2  ClickEvent
     * @return this
     */
    public ChatBuilder addText(String str, HoverEvent e, ClickEvent e2) {
        Component c = fromString(str);
        c.setHoverEvent(e);
        c.setClickEvent(e2);
        return addComponent(c);
    }

    /**
     * Convert this ChatBuilder into a TextComponent for spigot
     *
     * @return TextComponent
     */
    public Component build() {
        return comp;
    }

    /**
     * Convert this component into a different format
     *
     * @param converter Converter
     * @param <T>       Format Type
     * @return Converted component
     */
    public <T> T convert(Converter<T> converter) {
        return converter.convert(build());
    }
}
