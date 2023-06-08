package net.ultragrav.chat.converters;

import net.ultragrav.chat.components.*;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;
import net.ultragrav.serializer.JsonMeta;

import java.util.List;
import java.util.stream.Collectors;

public class JsonMetaConverter implements Converter<JsonMeta> {
    public static final JsonMetaConverter INSTANCE = new JsonMetaConverter();

    private JsonMetaConverter() {}

    @Override
    public JsonMeta convert(Component component) {
        JsonMeta meta = new JsonMeta();

        if (component instanceof TextComponent) {
            meta.set("text", ((TextComponent) component).getText());
        } else if (component instanceof KeybindComponent) {
            meta.set("keybind", ((KeybindComponent) component).getKeybind());
        } else if (component instanceof ScoreComponent) {
            ScoreComponent sc = (ScoreComponent) component;
            meta.set("score.name", sc.getName());
            meta.set("score.objective", sc.getObjective());
            if (sc.getValue() != null) {
                meta.set("score.value", sc.getValue());
            }
        } else if (component instanceof SelectorComponent) {
            meta.set("selector", ((SelectorComponent) component).getSelector());
        } else if (component instanceof TranslatableComponent) {
            meta.set("translate", ((TranslatableComponent) component).getKey());
            List<Component> args = ((TranslatableComponent) component).getArgs();
            if (!args.isEmpty()) {
                meta.set("with", args.stream().map(this::convert).collect(Collectors.toList()));
            }
        }

        if (component.getColor() != null) {
            meta.set("color", component.getColor().getName());
        }

        if (component.getBold() != null) {
            meta.set("bold", component.isBold());
        }
        if (component.getItalic() != null) {
            meta.set("italic", component.isItalic());
        }
        if (component.getUnderlined() != null) {
            meta.set("underlined", component.isUnderlined());
        }
        if (component.getStrikethrough() != null) {
            meta.set("strikethrough", component.isStrikethrough());
        }
        if (component.getObfuscated() != null) {
            meta.set("obfuscated", component.isObfuscated());
        }

        if (component.getFont() != null) {
            meta.set("font", component.getFont());
        }

        if (component.getClickEvent() != null) {
            meta.set("clickEvent", convertClick(component.getClickEvent()));
        }
        if (component.getHoverEvent() != null) {
            meta.set("hoverEvent", convertHover(component.getHoverEvent()));
        }

        if (component.getInsertion() != null) {
            meta.set("insertion", component.getInsertion());
        }

        if (!component.getExtra().isEmpty()) {
            meta.set("extra", component.getExtra().stream().map(this::convert).collect(Collectors.toList()));
        }

        return meta;
    }

    @Override
    public Component convert(JsonMeta other) {
        Component.Builder<?, ?> builder;

        if (other.get("text") != null) {
            builder = TextComponent.builder(other.get("text"));
        } else if (other.get("keybind") != null) {
            builder = KeybindComponent.builder(other.get("keybind"));
        } else if (other.get("score") != null) {
            builder = ScoreComponent.builder(
                    other.get("score.name"),
                    other.get("score.objective"),
                    other.get("score.value")
            );
        } else if (other.get("selector") != null) {
            builder = SelectorComponent.builder(other.get("selector"));
        } else if (other.get("translate")) {
            builder = TranslatableComponent.builder(other.get("translate"));
            if (other.get("with") != null) {
                List<Component> args = other.<List<JsonMeta>>get("with").stream()
                        .map(this::convert).collect(Collectors.toList());
                ((TranslatableComponent.Builder) builder).args(args);
            }
        } else {
            throw new IllegalArgumentException("Invalid chat component");
        }

        if (other.get("color") != null) {
            builder.color(TextColor.valueOf(other.<String>get("color").toUpperCase()));
        }

        if (other.get("bold") != null) {
            builder.bold(other.get("bold"));
        }
        if (other.get("italic") != null) {
            builder.italic(other.get("italic"));
        }
        if (other.get("underline") != null) {
            builder.underlined(other.get("underlined"));
        }
        if (other.get("strikethrough") != null) {
            builder.strikethrough(other.get("strikethrough"));
        }
        if (other.get("obfuscated") != null) {
            builder.obfuscated(other.get("obfuscated"));
        }

        if (other.get("font") != null) {
            builder.font(other.get("font"));
        }

        if (other.get("clickEvent") != null) {
            builder.clickEvent(convertClick(other.<JsonMeta>get("clickEvent")));
        }
        if (other.get("hoverEvent") != null) {
            builder.hoverEvent(convertHover(other.<JsonMeta>get("hoverEvent")));
        }

        if (other.get("insertion") != null) {
            builder.insertion(other.get("insertion"));
        }

        if (other.get("extra") != null) {
            List<JsonMeta> arr = other.get("extra");
            List<Component> components = arr.stream().map(this::convert).collect(Collectors.toList());
            builder.extra(components);
        }

        return builder.build();
    }

    private JsonMeta convertClick(ClickEvent ev) {
        JsonMeta meta = new JsonMeta();
        meta.set("action", ev.getAction().getName());
        meta.set("value", ev.getValue());
        return meta;
    }

    private ClickEvent convertClick(JsonMeta meta) {
        ClickEvent.Action action = ClickEvent.Action.valueOf(meta.<String>get("action").toUpperCase());
        String value = meta.get("value");
        return new ClickEvent(action, value);
    }

    private JsonMeta convertHover(HoverEvent ev) {
        JsonMeta meta = new JsonMeta();
        meta.set("action", ev.getAction().getName());
        meta.set("value", convert(ev.getValue()));
        return meta;
    }

    private HoverEvent convertHover(JsonMeta meta) {
        HoverEvent.Action action = HoverEvent.Action.valueOf(meta.<String>get("action").toUpperCase());
        JsonMeta value = meta.get("value");
        return new HoverEvent(action, convert(value));
    }
}
