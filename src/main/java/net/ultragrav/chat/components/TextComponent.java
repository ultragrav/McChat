package net.ultragrav.chat.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.List;

@Getter
public class TextComponent extends Component {
    private String text;

    protected TextComponent(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra, String text) {
        super(color, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, extra);
        this.text = text;
    }

    protected TextComponent(Builder builder) {
        super(builder);
        this.text = builder.text;
    }

    public static Builder builder(String text) {
        return new Builder(text);
    }
    public static TextComponent of(String text) {
        return builder(text).build();
    }

    @Override
    public String toString() {
        return text + super.toString();
    }

    @AllArgsConstructor
    public static class Builder extends Component.Builder<TextComponent, Builder> {
        private String text;

        protected Builder(TextComponent component) {
            super(component);
            this.text = component.text;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public TextComponent build() {
            return new TextComponent(this);
        }
    }
}
