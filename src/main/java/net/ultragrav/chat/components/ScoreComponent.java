package net.ultragrav.chat.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.List;

@Getter
public class ScoreComponent extends Component {
    private String name;
    private String objective;
    private String value;

    protected ScoreComponent(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String font, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra, String name, String objective, String value) {
        super(color, bold, italic, underlined, strikethrough, obfuscated, font, clickEvent, hoverEvent, insertion, extra);
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    protected ScoreComponent(Builder builder) {
        super(builder);
        this.name = builder.name;
        this.objective = builder.objective;
        this.value = builder.value;
    }

    public static Builder builder(String name, String objective, String value) {
        return new Builder(name, objective, value);
    }

    public static ScoreComponent of(String name, String objective, String value) {
        return builder(name, objective, value).build();
    }

    @Override
    public String toString() {
        return value + super.toString();
    }

    @AllArgsConstructor
    public static class Builder extends Component.Builder<ScoreComponent, Builder> {
        private String name;
        private String objective;
        private String value;

        protected Builder(ScoreComponent component) {
            super(component);
            this.name = component.name;
            this.objective = component.objective;
            this.value = component.value;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder objective(String objective) {
            this.objective = objective;
            return this;
        }
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public ScoreComponent build() {
            return new ScoreComponent(this);
        }
    }
}
