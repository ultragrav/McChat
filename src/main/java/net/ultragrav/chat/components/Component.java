package net.ultragrav.chat.components;

import lombok.*;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@EqualsAndHashCode(exclude = "parent")
public abstract class Component {
    @Setter(AccessLevel.NONE)
    Component parent;

    // Formatting
    private TextColor color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;

    private String font;

    // Events
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;

    private String insertion;

    private List<Component> extra;

    protected Component(TextColor color, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, String font, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, List<Component> extra) {
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;

        this.font = font;

        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;

        this.insertion = insertion;

        this.extra = extra;
    }

    protected Component(Builder<?, ?> builder) {
        this.color = builder.color;
        this.bold = builder.bold;
        this.italic = builder.italic;
        this.underlined = builder.underlined;
        this.strikethrough = builder.strikethrough;
        this.obfuscated = builder.obfuscated;

        this.font = builder.font;

        this.clickEvent = builder.clickEvent;
        this.hoverEvent = builder.hoverEvent;

        this.insertion = builder.insertion;

        this.extra = builder.extra;

        for (Component component : builder.extra) {
            component.parent = this;
        }
    }

    public boolean isBold() {
        if (bold != null) {
            return bold;
        } else {
            return parent != null && parent.isBold();
        }
    }

    public boolean isItalic() {
        if (italic != null) {
            return italic;
        } else {
            return parent != null && parent.isItalic();
        }
    }

    public boolean isUnderlined() {
        if (underlined != null) {
            return underlined;
        } else {
            return parent != null && parent.isUnderlined();
        }
    }

    public boolean isStrikethrough() {
        if (strikethrough != null) {
            return strikethrough;
        } else {
            return parent != null && parent.isStrikethrough();
        }
    }

    public boolean isObfuscated() {
        if (obfuscated != null) {
            return obfuscated;
        } else {
            return parent != null && parent.isObfuscated();
        }
    }

    public void setExtra(List<Component> comp) {
        this.extra.addAll(comp);
        for (Component component : comp) {
            component.parent = this;
        }
    }
    public void addExtra(Component comp) {
        this.extra.add(comp);
        comp.parent = this;
    }

    public String toString() {
        return extra.stream().map(Component::toString).collect(Collectors.joining());
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends Component, B extends Builder<T, B>> {
        // Formatting
        private TextColor color;
        private Boolean bold;
        private Boolean italic;
        private Boolean underlined;
        private Boolean strikethrough;
        private Boolean obfuscated;

        private String font;

        // Events
        private ClickEvent clickEvent;
        private HoverEvent hoverEvent;

        private String insertion;

        private List<Component> extra;

        protected Builder() {
            this.extra = new ArrayList<>();
        }

        protected Builder(Component c) {
            this.color = c.color;
            this.bold = c.bold;
            this.italic = c.italic;
            this.underlined = c.underlined;
            this.strikethrough = c.strikethrough;
            this.obfuscated = c.obfuscated;

            this.font = c.font;

            this.clickEvent = c.clickEvent;
            this.hoverEvent = c.hoverEvent;

            this.insertion = c.insertion;

            this.extra = c.extra;
        }

        public B color(TextColor color) {
            this.color = color;
            return (B) this;
        }

        public B bold(boolean bold) {
            this.bold = bold;
            return (B) this;
        }

        public B italic(boolean italic) {
            this.italic = italic;
            return (B) this;
        }

        public B underlined(boolean underlined) {
            this.underlined = underlined;
            return (B) this;
        }

        public B strikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return (B) this;
        }

        public B obfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return (B) this;
        }

        public B font(String font) {
            this.font = font;
            return (B) this;
        }

        public B clickEvent(ClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return (B) this;
        }

        public B hoverEvent(HoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return (B) this;
        }

        public B insertion(String insertion) {
            this.insertion = insertion;
            return (B) this;
        }

        public B extra(List<Component> extra) {
            this.extra = extra;
            return (B) this;
        }

        public B addExtra(Component comp) {
            this.extra.add(comp);
            return (B) this;
        }

        public abstract T build();
    }
}
