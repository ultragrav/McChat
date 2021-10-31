package net.ultragrav.chat.serialization;

import net.ultragrav.chat.components.*;
import net.ultragrav.chat.events.ClickEvent;
import net.ultragrav.chat.events.HoverEvent;
import net.ultragrav.chat.formatting.TextColor;
import net.ultragrav.serializer.GravSerializer;
import net.ultragrav.serializer.Serializer;
import net.ultragrav.serializer.Serializers;

import java.util.ArrayList;
import java.util.List;

public class ComponentSerialization {
    public static void init() {
        Serializer<Component> ser;
        Serializers.registerSerializer(Component.class, ser = new Serializer<Component>() {
            @Override
            public void serialize(GravSerializer serializer, Object o) {
                Component comp = (Component) o;
                if (comp instanceof TextComponent) {
                    serializer.writeByte((byte) 0);
                    serializer.writeString(((TextComponent) comp).getText());
                } else if (comp instanceof KeybindComponent) {
                    serializer.writeByte((byte) 1);
                    serializer.writeString(((KeybindComponent) comp).getKeybind());
                } else if (comp instanceof ScoreComponent) {
                    ScoreComponent sc = ((ScoreComponent) comp);
                    serializer.writeByte((byte) 2);
                    serializer.writeString(sc.getName());
                    serializer.writeString(sc.getObjective());
                    serializer.writeString(sc.getValue());
                } else if (comp instanceof SelectorComponent) {
                    serializer.writeByte((byte) 3);
                    serializer.writeString(((SelectorComponent) comp).getSelector());
                } else if (comp instanceof TranslatableComponent) {
                    TranslatableComponent tc = ((TranslatableComponent) comp);
                    serializer.writeByte((byte) 4);
                    serializer.writeString(tc.getKey());
                    writeComponentList(serializer, tc.getExtra());
                }

                serializer.writeInt(comp.getColor().ordinal());
                serializer.writeObject(comp.getBold());
                serializer.writeObject(comp.getItalic());
                serializer.writeObject(comp.getUnderlined());
                serializer.writeObject(comp.getStrikethrough());
                serializer.writeObject(comp.getObfuscated());

                serializer.writeObject(comp.getClickEvent());
                serializer.writeObject(comp.getHoverEvent());

                serializer.writeString(comp.getInsertion());

                writeComponentList(serializer, comp.getExtra());
            }

            @Override
            public Component deserialize(GravSerializer serializer, Object... objects) {
                Component c;
                byte type = serializer.readByte();
                switch (type) {
                    case 0:
                        c = TextComponent.of(serializer.readString());
                        break;
                    case 1:
                        c = KeybindComponent.of(serializer.readString());
                        break;
                    case 2:
                        c = ScoreComponent.of(serializer.readString(), serializer.readString(), serializer.readString());
                        break;
                    case 3:
                        c = SelectorComponent.of(serializer.readString());
                        break;
                    case 4:
                        c = TranslatableComponent.builder(serializer.readString()).args(readComponentList(serializer)).build();
                        break;
                    default:
                        throw new IllegalStateException("Found invalid component type while deserializing");
                }

                c.setColor(TextColor.values()[serializer.readInt()]);
                c.setBold(serializer.readObject());
                c.setItalic(serializer.readObject());
                c.setUnderlined(serializer.readObject());
                c.setStrikethrough(serializer.readObject());
                c.setObfuscated(serializer.readObject());

                c.setClickEvent(serializer.readObject());
                c.setHoverEvent(serializer.readObject());

                c.setInsertion(serializer.readString());

                c.setExtra(readComponentList(serializer));

                return c;
            }

            private List<Component> readComponentList(GravSerializer serializer) {
                int size = serializer.readInt();
                List<Component> comps = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    comps.add(deserialize(serializer));
                }
                return comps;
            }

            private void writeComponentList(GravSerializer serializer, List<Component> components) {
                serializer.writeInt(components.size());
                for (Component comp : components) {
                    serialize(serializer, comp);
                }
            }
        });
        Serializers.registerSerializer(ClickEvent.class, new Serializer<ClickEvent>() {
            @Override
            public void serialize(GravSerializer serializer, Object o) {
                ClickEvent ev = (ClickEvent) o;
                serializer.writeInt(ev.getAction().ordinal());
                serializer.writeString(ev.getValue());
            }

            @Override
            public ClickEvent deserialize(GravSerializer serializer, Object... objects) {
                return new ClickEvent(ClickEvent.Action.values()[serializer.readInt()], serializer.readString());
            }
        });
        Serializers.registerSerializer(HoverEvent.class, new Serializer<HoverEvent>() {
            @Override
            public void serialize(GravSerializer serializer, Object o) {
                HoverEvent ev = (HoverEvent) o;
                serializer.writeInt(ev.getAction().ordinal());
                ser.serialize(serializer, ev.getValue());
            }

            @Override
            public HoverEvent deserialize(GravSerializer serializer, Object... objects) {
                return new HoverEvent(HoverEvent.Action.values()[serializer.readInt()], ser.deserialize(serializer));
            }
        });
    }
}
