package net.ultragrav.chat.components;

import lombok.Getter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.List;

@Getter
public class TranslatableComponent extends Component {
    private String key;
    private List<Component> args;

    protected TranslatableComponent(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String font, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra, String key, List<Component> args) {
        super(color, bold, italic, underlined, strikethrough, obfuscated, font, clickEvent, hoverEvent, insertion, extra);
        this.key = key;
        this.args = args;
    }

    protected TranslatableComponent(Builder builder) {
        super(builder);
        this.key = builder.key;
    }

    public static Builder builder(String key) {
        return new Builder(key);
    }

    public static TranslatableComponent of(String key) {
        return builder(key).build();
    }

    public static TranslatableComponent of(String key, List<Component> args) {
        return builder(key).args(args).build();
    }

    public static class Builder extends Component.Builder<TranslatableComponent, Builder> {
        private String key;
        private List<Component> args;

        public Builder(String key) {
            super();
            this.key = key;
        }

        protected Builder(TranslatableComponent component) {
            super(component);
            this.key = component.key;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder args(List<Component> args) {
            this.args = args;
            return this;
        }

        public Builder addArg(Component comp) {
            this.args.add(comp);
            return this;
        }

        public TranslatableComponent build() {
            return new TranslatableComponent(this);
        }
    }
}
