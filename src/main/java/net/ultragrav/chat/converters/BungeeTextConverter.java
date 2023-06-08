package net.ultragrav.chat.converters;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.ultragrav.chat.components.*;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BungeeCord converter
 *
 * use this for older versions of minecraft where a lot of components are not present
 */
public class BungeeTextConverter implements Converter<BaseComponent> {
    public static final BungeeTextConverter INSTANCE = new BungeeTextConverter();

    private BungeeTextConverter() {
    }

    @Override
    public BaseComponent convert(Component component) {
        BaseComponent comp;
        if (component instanceof TextComponent) {
            comp = new net.md_5.bungee.api.chat.TextComponent(((TextComponent) component).getText());
        } else {
            throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
        }

        comp.setColor(convertColor(component.getColor()));
        comp.setBold(component.getBold());
        comp.setItalic(component.getItalic());
        comp.setUnderlined(component.getUnderlined());
        comp.setStrikethrough(component.getStrikethrough());
        comp.setObfuscated(component.getObfuscated());

        comp.setFont(component.getFont());

        comp.setClickEvent(convertEvent(component.getClickEvent()));
        comp.setHoverEvent(convertEvent(component.getHoverEvent()));

        if (!component.getExtra().isEmpty()) {
            List<BaseComponent> extra = new ArrayList<>();
            for (Component ex : component.getExtra()) {
                extra.add(convert(ex));
            }
            comp.setExtra(extra);
        }

        return comp;
    }

    @Override
    public Component convert(BaseComponent other) {
        Component comp;
        if (other instanceof net.md_5.bungee.api.chat.TextComponent) {
            comp = TextComponent.of(((net.md_5.bungee.api.chat.TextComponent) other).getText());
        } else {
            throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
        }

        comp.setColor(convertColor(other.getColor()));
        comp.setBold(other.isBoldRaw());
        comp.setItalic(other.isItalicRaw());
        comp.setUnderlined(other.isUnderlinedRaw());
        comp.setStrikethrough(other.isStrikethroughRaw());
        comp.setObfuscated(other.isObfuscatedRaw());

        comp.setFont(other.getFont());

        comp.setClickEvent(convertEvent(other.getClickEvent()));
        comp.setHoverEvent(convertEvent(other.getHoverEvent()));

        comp.setInsertion(other.getInsertion());

        List<Component> extra = new ArrayList<>();
        for (BaseComponent ex : other.getExtra()) {
            extra.add(convert(ex));
        }
        comp.setExtra(extra);

        return comp;
    }


    private ChatColor convertColor(TextColor color) {
        if (color == null) return null;
        return ChatColor.valueOf(color.name());
    }

    private TextColor convertColor(ChatColor color) {
        if (color == null) return null;
        return TextColor.valueOf(color.name());
    }

    private net.md_5.bungee.api.chat.ClickEvent convertEvent(ClickEvent event) {
        if (event == null) return null;
        return new net.md_5.bungee.api.chat.ClickEvent(
                net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(event.getAction().name()),
                event.getValue()
        );
    }

    private ClickEvent convertEvent(net.md_5.bungee.api.chat.ClickEvent event) {
        if (event == null) return null;
        return new ClickEvent(
                ClickEvent.Action.valueOf(event.getAction().name()),
                event.getValue()
        );
    }

    private net.md_5.bungee.api.chat.HoverEvent convertEvent(HoverEvent event) {
        if (event == null) return null;
        return new net.md_5.bungee.api.chat.HoverEvent(
                net.md_5.bungee.api.chat.HoverEvent.Action.valueOf(event.getAction().name()),
                new BaseComponent[]{convert(event.getValue())}
        );
    }

    private HoverEvent convertEvent(net.md_5.bungee.api.chat.HoverEvent event) {
        if (event == null) return null;
        return new HoverEvent(
                HoverEvent.Action.valueOf(event.getAction().name()),
                convert(new net.md_5.bungee.api.chat.TextComponent(event.getValue()))
        );
    }
}
