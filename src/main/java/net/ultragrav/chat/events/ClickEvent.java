package net.ultragrav.chat.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.event.ActionListener;

@Getter
@AllArgsConstructor
public class ClickEvent {
    private Action action;
    private String value;

    @Getter
    @AllArgsConstructor
    public enum Action {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private String name;
        private boolean readable;

        public String toString() {
            return name;
        }
    }
}
