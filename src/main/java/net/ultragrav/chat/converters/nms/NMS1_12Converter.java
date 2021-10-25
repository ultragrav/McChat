package net.ultragrav.chat.converters.nms;

import net.minecraft.server.v1_12_R1.*;
import net.ultragrav.chat.components.*;
import net.ultragrav.chat.converters.BungeeTextConverter;
import net.ultragrav.chat.converters.Converter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;

import java.util.ArrayList;
import java.util.List;

public class NMS1_12Converter implements Converter<IChatBaseComponent> {
    public static final NMS1_12Converter INSTANCE = new NMS1_12Converter();

    private NMS1_12Converter() {}

    @Override
    public IChatBaseComponent convert(Component component) {
        IChatBaseComponent comp;
        if (component instanceof TextComponent) {
            comp = new ChatComponentText(((TextComponent) component).getText());
        } else if (component instanceof KeybindComponent) {
            comp = new ChatComponentKeybind(((KeybindComponent) component).getKeybind());
        } else if (component instanceof ScoreComponent) {
            ScoreComponent sc = (ScoreComponent) component;
            comp = new ChatComponentScore(sc.getName(), sc.getObjective());
        } else if (component instanceof SelectorComponent) {
            comp = new ChatComponentSelector(((SelectorComponent) component).getSelector());
        } else {
            throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
        }

        ChatModifier modifier = new ChatModifier()
                .setColor(convertColor(component.getColor()))
                .setBold(component.getBold())
                .setItalic(component.getItalic())
                .setUnderline(component.getUnderlined())
                .setStrikethrough(component.getStrikethrough())
                .setRandom(component.getObfuscated())
                .setChatClickable(convertEvent(component.getClickEvent()))
                .setChatHoverable(convertEvent(component.getHoverEvent()))
                .setInsertion(component.getInsertion());

        comp.setChatModifier(modifier);

        if (!component.getExtra().isEmpty()) {
            for (Component ex : component.getExtra()) {
                comp.addSibling(convert(ex));
            }
        }

        return comp;
    }

    @Override
    public Component convert(IChatBaseComponent other) {
        Component comp;
        if (other instanceof ChatComponentText) {
            comp = TextComponent.of(other.getText());
        } else if (other instanceof ChatComponentKeybind) {
            comp = KeybindComponent.of(((ChatComponentKeybind) other).h());
        } else if (other instanceof ChatComponentScore) {
            ChatComponentScore scoreComponent = (ChatComponentScore) other;
            comp = ScoreComponent.of(scoreComponent.g(), scoreComponent.h(), scoreComponent.getText());
        } else if (other instanceof ChatComponentSelector) {
            comp = SelectorComponent.of(((ChatComponentSelector) other).g());
        } else {
            throw new IllegalArgumentException("Invalid component, please make sure you do not modify the component system in spigot!");
        }

        ChatModifier modifier = other.getChatModifier();

        comp.setColor(convertColor(modifier.getColor()));
        comp.setBold(modifier.isBold());
        comp.setItalic(modifier.isItalic());
        comp.setUnderlined(modifier.isUnderlined());
        comp.setStrikethrough(modifier.isStrikethrough());
        comp.setObfuscated(modifier.isRandom());
        comp.setClickEvent(convertEvent(modifier.h()));
        comp.setHoverEvent(convertEvent(modifier.i()));
        comp.setInsertion(modifier.j());

        List<Component> extra = new ArrayList<>();
        for (IChatBaseComponent sibling : other) {
            extra.add(convert(sibling));
        }
        comp.setExtra(extra);

        return comp;
    }

    private EnumChatFormat convertColor(TextColor color) {
        if (color == null) return null;
        return EnumChatFormat.valueOf(color.name());
    }

    private TextColor convertColor(EnumChatFormat color) {
        if (color == null) return null;
        return TextColor.valueOf(color.name());
    }

    private ChatClickable convertEvent(ClickEvent event) {
        if (event == null) return null;
        return new ChatClickable(ChatClickable.EnumClickAction.valueOf(event.getAction().name()), event.getValue());
    }

    private ClickEvent convertEvent(ChatClickable event) {
        if (event == null) return null;
        return new ClickEvent(ClickEvent.Action.valueOf(event.a().name()), event.b());
    }

    private ChatHoverable convertEvent(HoverEvent event) {
        if (event == null) return null;
        return new ChatHoverable(ChatHoverable.EnumHoverAction.valueOf(event.getAction().name()), convert(event.getValue()));
    }

    private HoverEvent convertEvent(ChatHoverable event) {
        if (event == null) return null;
        return new HoverEvent(HoverEvent.Action.valueOf(event.a().name()), convert(event.b()));
    }
}
