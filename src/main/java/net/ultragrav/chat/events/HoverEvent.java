package net.ultragrav.chat.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ultragrav.chat.components.Component;

@Getter
@AllArgsConstructor
public class HoverEvent {
    private Action action;
    private Component value;

    @Getter
    @AllArgsConstructor
    public enum Action {
        SHOW_TEXT("show_text", true),
        SHOW_ITEM("show_item", false),
        SHOW_ENTITY("show_entity", true);

        private String name;
        private boolean readable;

        public String toString() {
            return name;
        }
    }
}
