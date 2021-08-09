package net.ultragrav.chat.converters;

import net.ultragrav.chat.components.Component;
import net.ultragrav.chat.components.TextComponent;
import net.ultragrav.chat.formatting.TextColor;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class LegacyConverter implements Converter<String> {
    public static final LegacyConverter MINECRAFT = new LegacyConverter('§');
    public static final LegacyConverter AMPERSAND = new LegacyConverter('&');

    private char colorChar;

    public LegacyConverter(char colorChar) {
        this.colorChar = colorChar;
    }

    @Override
    public String convert(Component component) {
        StringBuilder builder = new StringBuilder();
        convert(builder, component);
        return builder.toString();
    }

    private void convert(StringBuilder builder, Component component) {
        if (component.getColor() != null) {
            builder.append(colorChar).append(component.getColor().getCode());
        }

        if (component.isBold()) {
            builder.append(component).append('l');
        }
        if (component.isItalic()) {
            builder.append(component).append('o');
        }
        if (component.isUnderlined()) {
            builder.append(component).append('n');
        }
        if (component.isStrikethrough()) {
            builder.append(component).append('m');
        }
        if (component.isObfuscated()) {
            builder.append(component).append('k');
        }

        if (component instanceof TextComponent) {
            builder.append(((TextComponent) component).getText());
        }

        for (Component extra : component.getExtra()) {
            convert(builder, extra);
        }
    }

    @Override
    public Component convert(String other) {
        return new LegacyReader(other).read();
    }

    private class LegacyReader {
        private final CharacterIterator it;

        boolean chC = false;

        private StringBuilder builder;
        private TextColor color;
        private boolean bold;
        private boolean italic;
        private boolean underlined;
        private boolean strikethrough;
        private boolean obfuscated;

        public LegacyReader(String str) {
            this.it = new StringCharacterIterator(str);

            reset();
        }

        private void reset() {
            builder = new StringBuilder();

            color = null;
            bold = false;
            italic = false;
            underlined = false;
            strikethrough = false;
            obfuscated = false;
        }

        private Component getAndReset() {
            Component ret = TextComponent.builder(builder.toString())
                    .color(color)
                    .bold(bold)
                    .italic(italic)
                    .underlined(underlined)
                    .strikethrough(strikethrough)
                    .obfuscated(obfuscated)
                    .build();

            reset();

            return ret;
        }

        public Component read() {
            Component container = TextComponent.of("");

            char ch = it.current();
            while (ch != CharacterIterator.DONE) {
                if (chC) {
                    TextColor color = TextColor.getByChar(ch);
                    if (color == null) {
                        builder.append(colorChar);
                        builder.append(ch);
                    } else {
                        if (!builder.toString().isEmpty()) {
                            container.addExtra(getAndReset());
                        }
                        switch (ch) {
                            case 'l':
                                bold = true;
                                break;
                            case 'o':
                                italic = true;
                                break;
                            case 'n':
                                underlined = true;
                                break;
                            case 'm':
                                strikethrough = true;
                                break;
                            case 'k':
                                obfuscated = true;
                                break;
                            default:
                                this.color = color;
                        }
                    }
                    chC = false;
                } else {
                    if (ch == colorChar) {
                        chC = true;
                    } else {
                        builder.append(ch);
                    }
                }
                ch = it.next();
            }
            if (!builder.toString().isEmpty()) {
                container.addExtra(getAndReset());
            }
            return container;
        }
    }
}
