package org.f108349.denis.ConsoleCommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner;
    private final Map<String, MenuOption> actions = new HashMap<>();
    
    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void addOption(String key, String description, Runnable action) {
        actions.put(key, new MenuOption(description, action));
    }
    
    public void run() {
        System.out.println("\nPlease select an option:");
        for (Map.Entry<String, MenuOption> entry : actions.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getDescription());
        }
        
        // TODO: test
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        MenuOption option = actions.get(choice);
        if (option != null) {
            option.getAction().run();
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }
}
