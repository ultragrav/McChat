package net.ultragrav.chat.converters;

import net.kyori.text.format.TextDecoration;
import net.ultragrav.chat.components.*;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KyoriConverter implements Converter<net.kyori.text.Component> {
    public static final KyoriConverter INSTANCE = new KyoriConverter();

    private KyoriConverter() {
    }

    @Override
    public net.kyori.text.Component convert(Component component) {
        net.kyori.text.Component comp;
        if (component instanceof TextComponent) {
            comp = net.kyori.text.TextComponent.of(((TextComponent) component).getText());
        } else if (component instanceof KeybindComponent) {
            comp = net.kyori.text.KeybindComponent.of(((KeybindComponent) component).getKeybind());
        } else if (component instanceof ScoreComponent) {
            ScoreComponent sc = (ScoreComponent) component;
            comp = net.kyori.text.ScoreComponent.of(sc.getName(), sc.getObjective(), sc.getValue());
        } else if (component instanceof SelectorComponent) {
            comp = net.kyori.text.SelectorComponent.of(((SelectorComponent) component).getSelector());
        } else if (component instanceof TranslatableComponent) {
            net.kyori.text.Component[] args = ((TranslatableComponent) component).getArgs().stream()
                    .map(this::convert).toArray(net.kyori.text.Component[]::new);
            comp = net.kyori.text.TranslatableComponent.of(((TranslatableComponent) component).getKey(), args);
        } else {
            throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
        }

        comp.color(convertColor(component.getColor()));
        comp.decoration(TextDecoration.BOLD, convertState(component.getBold()));
        comp.decoration(TextDecoration.ITALIC, convertState(component.getItalic()));
        comp.decoration(TextDecoration.UNDERLINE, convertState(component.getUnderlined()));
        comp.decoration(TextDecoration.STRIKETHROUGH, convertState(component.getStrikethrough()));
        comp.decoration(TextDecoration.OBFUSCATED, convertState(component.getObfuscated()));

        comp.clickEvent(convertEvent(component.getClickEvent()));
        comp.hoverEvent(convertEvent(component.getHoverEvent()));

        comp.insertion(component.getInsertion());

        for (Component ex : component.getExtra()) {
            comp.append(convert(ex));
        }

        return comp;
    }

    public Component convert(net.kyori.text.Component component) {
        Component comp;
        if (component instanceof net.kyori.text.TextComponent) {
            comp = TextComponent.of(((net.kyori.text.TextComponent) component).content());
        } else if (component instanceof net.kyori.text.KeybindComponent) {
            comp = KeybindComponent.of(((net.kyori.text.KeybindComponent) component).keybind());
        } else if (component instanceof net.kyori.text.ScoreComponent) {
            net.kyori.text.ScoreComponent sc = (net.kyori.text.ScoreComponent) component;
            comp = ScoreComponent.of(sc.name(), sc.objective(), sc.value());
        } else if (component instanceof net.kyori.text.SelectorComponent) {
            comp = SelectorComponent.of(((net.kyori.text.SelectorComponent) component).pattern());
        } else if (component instanceof net.kyori.text.TranslatableComponent) {
            List<net.kyori.text.Component> args = ((net.kyori.text.TranslatableComponent) component).args();
            comp = TranslatableComponent.of(((net.kyori.text.TranslatableComponent) component).key(), args.stream()
                    .map(this::convert).collect(Collectors.toList()));
        } else {
            throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
        }

        comp.setColor(convertColor(component.color()));
        comp.setBold(convertState(component.decoration(TextDecoration.BOLD)));
        comp.setItalic(convertState(component.decoration(TextDecoration.ITALIC)));
        comp.setUnderlined(convertState(component.decoration(TextDecoration.UNDERLINE)));
        comp.setStrikethrough(convertState(component.decoration(TextDecoration.STRIKETHROUGH)));
        comp.setObfuscated(convertState(component.decoration(TextDecoration.OBFUSCATED)));

        comp.setClickEvent(convertEvent(component.clickEvent()));
        comp.setHoverEvent(convertEvent(component.hoverEvent()));

        comp.setInsertion(component.insertion());

        List<Component> extra = new ArrayList<>();
        for (net.kyori.text.Component ex : component.children()) {
            extra.add(convert(ex));
        }
        comp.setExtra(extra);

        return comp;
    }

    private TextDecoration.State convertState(Boolean bool) {
        if (bool == null) return TextDecoration.State.NOT_SET;
        if (bool) return TextDecoration.State.TRUE;
        return TextDecoration.State.FALSE;
    }

    public Boolean convertState(TextDecoration.State state) {
        if (state == TextDecoration.State.NOT_SET) return null;
        return state == TextDecoration.State.TRUE;
    }

    private net.kyori.text.format.TextColor convertColor(TextColor color) {
        if (color == null) return null;
        return net.kyori.text.format.TextColor.valueOf(color.name());
    }

    private TextColor convertColor(net.kyori.text.format.TextColor color) {
        if (color == null) return null;
        return TextColor.valueOf(color.name());
    }

    private net.kyori.text.event.ClickEvent convertEvent(ClickEvent event) {
        if (event == null) return null;
        return new net.kyori.text.event.ClickEvent(
                net.kyori.text.event.ClickEvent.Action.valueOf(event.getAction().name()),
                event.getValue()
        );
    }

    private ClickEvent convertEvent(net.kyori.text.event.ClickEvent event) {
        if (event == null) return null;
        return new ClickEvent(
                ClickEvent.Action.valueOf(event.action().name()),
                event.value()
        );
    }

    private net.kyori.text.event.HoverEvent convertEvent(HoverEvent event) {
        if (event == null) return null;
        return new net.kyori.text.event.HoverEvent(
                net.kyori.text.event.HoverEvent.Action.valueOf(event.getAction().name()),
                convert(event.getValue())
        );
    }

    private HoverEvent convertEvent(net.kyori.text.event.HoverEvent event) {
        if (event == null) return null;
        return new HoverEvent(
                HoverEvent.Action.valueOf(event.action().name()),
                convert(event.value())
        );
    }
}