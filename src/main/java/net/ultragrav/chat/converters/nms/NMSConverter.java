package net.ultragrav.chat.converters.nms;

import net.ultragrav.chat.components.*;
import net.ultragrav.chat.converters.Converter;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Returns an IChatBaseComponent in the minecraft version currently running
 */
public class NMSConverter implements Converter<Object> {
    public static final NMSConverter INSTANCE = new NMSConverter();

    private String nmsVersion;

    Class<?> iChatBaseComponentClass;
    Class<?> chatComponentTextClass;
    Class<?> chatComponentKeybindClass;
    Class<?> chatComponentScoreClass;
    Class<?> chatComponentSelectorClass;
    Class<?> chatModifierClass;
    Class<?> enumChatFormatClass;
    Class<?> chatClickableClass;
    Class<?> chatClickableEnum;
    Class<?> chatHoverableClass;
    Class<?> chatHoverableEnum;

    Method iChatBaseComponentSetModifier;
    Method iChatBaseComponentAddSibling;
    Method iChatBaseComponentGetModifier;
    Method iChatBaseComponentIterator;

    Method chatModifierSetColor;
    Method chatModifierSetBold;
    Method chatModifierSetItalic;
    Method chatModifierSetUnderline;
    Method chatModifierSetStrikethrough;
    Method chatModifierSetRandom;
    Method chatModifierSetChatClickable;
    Method chatModifierSetChatHoverable;
    Method chatModifierSetInsertion;
    Method chatModifierGetColor;
    Method chatModifierIsBold;
    Method chatModifierIsItalic;
    Method chatModifierIsUnderlined;
    Method chatModifierIsStrikethrough;
    Method chatModifierIsRandom;
    Field chatModifierChatClickable;
    Field chatModifierChatHoverable;

    Constructor<?> chatClickableConstructor;
    Constructor<?> chatHoverableConstructor;

    Field chatClickableAction;
    Field chatClickableValue;

    Field chatHoverableAction;
    Field chatHoverableValue;

    Field chatComponentTextText;

    Field chatComponentKeybindKeybind;

    Field chatComponentScoreName;
    Field chatComponentScoreObjective;
    Field chatComponentScoreValue;

    Field chatComponentSelectorSelector;

    Method enumName;

    private NMSConverter() {
        nmsVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];

        iChatBaseComponentClass = getNMSClass("IChatBaseComponent");
        chatComponentTextClass = getNMSClass("ChatComponentText");
        chatComponentKeybindClass = getNMSClass("ChatComponentKeybind");
        chatComponentScoreClass = getNMSClass("ChatComponentScore");
        chatComponentSelectorClass = getNMSClass("ChatComponentSelector");
        chatModifierClass = getNMSClass("ChatModifier");
        enumChatFormatClass = getNMSClass("EnumChatFormat");
        chatClickableClass = getNMSClass("ChatClickable");
        chatClickableEnum = getNMSClass("ChatClickable.EnumClickAction");
        chatHoverableClass = getNMSClass("ChatHoverable");
        chatClickableEnum = getNMSClass("ChatHoverable.EnumHoverAction");

        try {
            iChatBaseComponentSetModifier = iChatBaseComponentClass.getMethod("setChatModifier", chatModifierClass);
            iChatBaseComponentAddSibling = iChatBaseComponentClass.getMethod("addSibling", iChatBaseComponentClass);
            iChatBaseComponentGetModifier = iChatBaseComponentClass.getMethod("getChatModifier");
            iChatBaseComponentIterator = iChatBaseComponentClass.getMethod("iterator");

            chatModifierSetColor = chatModifierClass.getDeclaredMethod("setColor", enumChatFormatClass);
            chatModifierSetBold = chatModifierClass.getDeclaredMethod("setBold", Boolean.class);
            chatModifierSetItalic = chatModifierClass.getDeclaredMethod("setItalic", Boolean.class);
            chatModifierSetUnderline = chatModifierClass.getDeclaredMethod("setUnderline", Boolean.class);
            chatModifierSetStrikethrough = chatModifierClass.getDeclaredMethod("setStrikethrough", Boolean.class);
            chatModifierSetRandom = chatModifierClass.getDeclaredMethod("setRandom", Boolean.class);
            chatModifierSetChatClickable = chatModifierClass.getDeclaredMethod("setChatClickable", chatClickableClass);
            chatModifierSetChatHoverable = chatModifierClass.getDeclaredMethod("setChatHoverable", chatHoverableClass);
            chatModifierSetInsertion = chatModifierClass.getDeclaredMethod("setInsertion", String.class);
            chatModifierGetColor = chatModifierClass.getDeclaredMethod("getColor");
            chatModifierIsBold = chatModifierClass.getDeclaredMethod("isBold");
            chatModifierIsItalic = chatModifierClass.getDeclaredMethod("isItalic");
            chatModifierIsUnderlined = chatModifierClass.getDeclaredMethod("isUnderlined");
            chatModifierIsStrikethrough = chatModifierClass.getDeclaredMethod("isStrikethrough");
            chatModifierIsRandom = chatModifierClass.getDeclaredMethod("isRandom");
            chatModifierChatClickable = getFieldOfType(chatModifierClass, chatClickableClass);
            chatModifierChatHoverable = getFieldOfType(chatModifierClass, chatHoverableClass);

            chatClickableConstructor = chatClickableClass.getDeclaredConstructor(chatClickableEnum, String.class);
            chatHoverableConstructor = chatHoverableClass.getDeclaredConstructor(chatHoverableEnum, String.class);

            chatClickableAction = chatClickableClass.getDeclaredFields()[0];
            chatClickableAction.setAccessible(true);
            chatClickableValue = chatClickableClass.getDeclaredFields()[1];
            chatClickableValue.setAccessible(true);

            chatHoverableAction = chatHoverableClass.getDeclaredFields()[0];
            chatHoverableAction.setAccessible(true);
            chatHoverableValue = chatHoverableClass.getDeclaredFields()[1];
            chatHoverableValue.setAccessible(true);

            chatComponentTextText = chatComponentTextClass.getDeclaredFields()[0];
            chatComponentTextText.setAccessible(true);

            chatComponentKeybindKeybind = chatComponentKeybindClass.getDeclaredFields()[1]; // TODO: Ensure this is the correct field
            chatComponentKeybindKeybind.setAccessible(true);

            chatComponentScoreName = chatComponentScoreClass.getDeclaredFields()[0];
            chatComponentScoreName.setAccessible(true);
            chatComponentScoreObjective = chatComponentScoreClass.getDeclaredFields()[1];
            chatComponentScoreObjective.setAccessible(true);
            chatComponentScoreValue = chatComponentScoreClass.getDeclaredFields()[2];
            chatComponentScoreValue.setAccessible(true);

            chatComponentSelectorSelector = chatComponentSelectorClass.getDeclaredFields()[0];
            chatComponentSelectorSelector.setAccessible(true);

            enumName = Enum.class.getDeclaredMethod("name");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getNMSClass(String clazz) {
        try {
            // TODO: Check which minecraft version moved from vX_Y_Z to non-versioned net.minecraft.network.chat

            return Class.forName("net.minecraft.server." + nmsVersion + "." + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getEnumConstant(Class<?> enumClass, String name) {
        try {
            return enumClass.getMethod("valueOf", String.class)
                    .invoke(null, name);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field getFieldOfType(Class<?> clazz, Class<?> type) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == type) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    @Override
    public Object convert(Component component) {
        Object comp;
        try {
            if (component instanceof TextComponent) {
                comp = chatComponentTextClass.getConstructor(String.class)
                        .newInstance(((TextComponent) component).getText());
            } else if (component instanceof KeybindComponent) {
                comp = chatComponentKeybindClass.getConstructor(String.class)
                        .newInstance(((KeybindComponent) component).getKeybind());
            } else if (component instanceof ScoreComponent) {
                ScoreComponent sc = (ScoreComponent) component;
                comp = chatComponentScoreClass.getConstructor(String.class, String.class)
                        .newInstance(sc.getName(), sc.getObjective());
            } else if (component instanceof SelectorComponent) {
                comp = chatComponentSelectorClass.getConstructor(String.class)
                        .newInstance(((SelectorComponent) component).getSelector());
            } else {
                throw new IllegalArgumentException("Invalid component, please do not extend Component directly, instead use one of the preexisting implementations!");
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Failed to create component", e);
        }

        Object modifier;
        try {
            modifier = chatModifierClass.getConstructor()
                    .newInstance();

            chatModifierSetColor.invoke(modifier, convertColor(component.getColor()));
            chatModifierSetBold.invoke(modifier, component.getBold());
            chatModifierSetItalic.invoke(modifier, component.getItalic());
            chatModifierSetUnderline.invoke(modifier, component.getUnderlined());
            chatModifierSetStrikethrough.invoke(modifier, component.getStrikethrough());
            chatModifierSetRandom.invoke(modifier, component.getObfuscated());
            chatModifierSetChatClickable.invoke(modifier, convertEventClick(component.getClickEvent()));
            chatModifierSetChatHoverable.invoke(modifier, convertEventHover(component.getHoverEvent()));
            chatModifierSetInsertion.invoke(modifier, component.getInsertion());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Failed to create modifier", e);
        }

        try {
            iChatBaseComponentSetModifier.invoke(comp, modifier);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to set modifier", e);
        }

        if (!component.getExtra().isEmpty()) {
            try {
                for (Component ex : component.getExtra()) {
                    iChatBaseComponentAddSibling.invoke(comp, convert(ex));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Failed to add extra components", e);
            }
        }

        return comp;
    }

    @Override
    public Component convert(Object other) {
        Component comp;
        try {
            if (chatComponentTextClass.isInstance(other)) {
                comp = TextComponent.of((String) chatComponentTextText.get(other));
            } else if (chatComponentKeybindClass.isInstance(other)) {
                comp = KeybindComponent.of((String) chatComponentKeybindKeybind.get(other));
            } else if (chatComponentScoreClass.isInstance(other)) {
                comp = ScoreComponent.of(
                        (String) chatComponentScoreName.get(other),
                        (String) chatComponentScoreObjective.get(other),
                        (String) chatComponentScoreValue.get(other)
                );
            } else if (chatComponentSelectorClass.isInstance(other)) {
                comp = SelectorComponent.of((String) chatComponentSelectorSelector.get(other));
            } else {
                throw new IllegalArgumentException("Invalid component, please make sure you do not modify the component system in spigot!");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to create component", e);
        }

        try {
            Object modifier = iChatBaseComponentGetModifier.invoke(other);

            comp.setColor(convertColor(chatModifierGetColor.invoke(modifier)));
            comp.setBold((Boolean) chatModifierIsBold.invoke(modifier));
            comp.setItalic((Boolean) chatModifierIsItalic.invoke(modifier));
            comp.setUnderlined((Boolean) chatModifierIsUnderlined.invoke(modifier));
            comp.setStrikethrough((Boolean) chatModifierIsStrikethrough.invoke(modifier));
            comp.setObfuscated((Boolean) chatModifierIsRandom.invoke(modifier));
            comp.setClickEvent(convertEventClick(chatModifierChatClickable.get(modifier)));
            comp.setHoverEvent(convertEventHover(chatModifierChatHoverable.get(modifier)));
            // TODO: Convert insertion
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to get modifier", e);
        }

        List<Component> extra = new ArrayList<>();
        try {
            Iterator<Object> siblings = (Iterator<Object>) iChatBaseComponentIterator.invoke(other);
            for (Iterator<Object> it = siblings; it.hasNext(); ) {
                Object sibling = it.next();
                extra.add(convert(sibling));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to get extra components", e);
        }
        comp.setExtra(extra);

        return comp;
    }

    private Object convertColor(TextColor color) {
        if (color == null) return null;
        return getEnumConstant(enumChatFormatClass, color.name());
    }

    private TextColor convertColor(Object color) {
        if (color == null) return null;
        try {
            return TextColor.valueOf((String) enumName.invoke(color));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to create color", e);
        }
    }

    private Object convertEventClick(ClickEvent event) {
        if (event == null) return null;

        try {
            return chatClickableConstructor.newInstance(
                    getEnumConstant(chatClickableEnum, event.getAction().name()),
                    event.getValue()
            );
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Failed to create click event", e);
        }
    }

    private ClickEvent convertEventClick(Object event) {
        if (event == null) return null;

        try {
            return new ClickEvent(
                    ClickEvent.Action.valueOf((String) enumName.invoke(chatClickableAction.get(event))),
                    (String) chatClickableValue.get(event)
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to create click event", e);
        }
    }

    private Object convertEventHover(HoverEvent event) {
        if (event == null) return null;

        try {
            return chatHoverableConstructor.newInstance(
                    getEnumConstant(chatHoverableEnum, event.getAction().name()),
                    event.getValue()
            );
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalStateException("Failed to create hover event", e);
        }
    }

    private HoverEvent convertEventHover(Object event) {
        if (event == null) return null;
        try {
            return new HoverEvent(
                    HoverEvent.Action.valueOf((String) enumName.invoke(chatHoverableAction.get(event))),
                    convert(chatHoverableValue.get(event))
            );
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to create hover event", e);
        }
    }
}
