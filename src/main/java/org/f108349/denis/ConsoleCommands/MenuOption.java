package org.f108349.denis.ConsoleCommands;

public class MenuOption {
    private final String description;
    private final Runnable action;

    public MenuOption(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public Runnable getAction() {
        return action;
    }
}

