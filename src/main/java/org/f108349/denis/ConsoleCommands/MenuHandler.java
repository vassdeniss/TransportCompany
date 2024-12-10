package org.f108349.denis.ConsoleCommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner;
    private final Map<String, MenuOption> actions = new HashMap<>();
    private final MenuOption[] todo;
    private int index = 0;
    
    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
        this.todo = new MenuOption[1];
    }
    
    public MenuHandler(Scanner scanner, int size) {
        this.scanner = scanner;
        this.todo = new MenuOption[size];
    }
    
    public void addOption(String key, String description, Runnable action) {
        actions.put(key, new MenuOption(description, action));
    }
    
    public void addOption(String description, Runnable action) {
        this.todo[this.index++] = new MenuOption(description, action); 
    }
    
    public void run() {
        System.out.println("\nPlease select an option:");
        for (Map.Entry<String, MenuOption> entry : actions.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getDescription());
        }
        
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();
        MenuOption option = actions.get(choice);
        if (option != null) {
            option.getAction().run();
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }
    
    public void runTodo() {
        System.out.println("\nPlease select an option:");
        for (int i = 0; i < this.todo.length; i++) {
            System.out.println(i + 1 + ". " + this.todo[i].getDescription());
        }
        
        System.out.print("Your choice: ");
        String choice = this.scanner.nextLine();
        MenuOption option = this.todo[Integer.parseInt(choice)];
        if (option != null) {
            option.getAction().run();
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }
}
