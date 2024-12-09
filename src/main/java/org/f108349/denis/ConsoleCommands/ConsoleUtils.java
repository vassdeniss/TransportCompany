package org.f108349.denis.ConsoleCommands;

import java.util.Scanner;

public class ConsoleUtils {
    private final Scanner scanner;

    public ConsoleUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid integer. Try again: ");
            }
        }
    }

    public java.sql.Date promptDate(String prompt) {
        System.out.print(prompt);
        while (true) {
            String input = scanner.nextLine();
            try {
                return java.sql.Date.valueOf(input);
            } catch (IllegalArgumentException ex) {
                System.out.print("Invalid date format (expected yyyy-mm-dd). Try again: ");
            }
        }
    }
}
