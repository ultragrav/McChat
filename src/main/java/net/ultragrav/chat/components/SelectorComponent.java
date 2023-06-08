package net.ultragrav.chat.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.List;

@Getter
public class SelectorComponent extends Component {
    private String selector;

    protected SelectorComponent(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String font, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra, String selector) {
        super(color, bold, italic, underlined, strikethrough, obfuscated, font, clickEvent, hoverEvent, insertion, extra);
        this.selector = selector;
    }

    protected SelectorComponent(Builder builder) {
        super(builder);
        this.selector = builder.selector;
    }

    public static Builder builder(String text) {
        return new Builder(text);
    }
    public static SelectorComponent of(String text) {
        return builder(text).build();
    }

    @AllArgsConstructor
    public static class Builder extends Component.Builder<SelectorComponent, Builder> {
        private String selector;

        protected Builder(SelectorComponent component) {
            super(component);
            this.selector = component.selector;
        }

        public Builder selector(String selector) {
            this.selector = selector;
            return this;
        }

        public SelectorComponent build() {
            return new SelectorComponent(this);
        }
    }
}
