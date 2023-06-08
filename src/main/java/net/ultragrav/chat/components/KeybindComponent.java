package net.ultragrav.chat.components;

import lombok.Getter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.List;

@Getter
public class KeybindComponent extends Component {
    private String keybind;

    protected KeybindComponent(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String font, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra, String keybind) {
        super(color, bold, italic, underlined, strikethrough, obfuscated, font, clickEvent, hoverEvent, insertion, extra);
        this.keybind = keybind;
    }

    protected KeybindComponent(Builder builder) {
        super(builder);
        this.keybind = builder.keybind;
    }

    public static Builder builder(String keybind) {
        return new Builder(keybind);
    }

    public static KeybindComponent of(String keybind) {
        return builder(keybind).build();
    }

    public static class Builder extends Component.Builder<KeybindComponent, Builder> {
        private String keybind;

        protected Builder(String keybind) {
            super();
            this.keybind = keybind;
        }

        protected Builder(KeybindComponent component) {
            super(component);
            this.keybind = component.keybind;
        }

        public Builder keybind(String keybind) {
            this.keybind = keybind;
            return this;
        }

        public KeybindComponent build() {
            return new KeybindComponent(this);
        }
    }
}
