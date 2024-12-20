package org.f108349.denis.ConsoleCommands;

import java.util.Scanner;

public class ConsoleUtils {
    public static String promptString(Scanner scanner,  String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int promptInt(Scanner scanner, String prompt) {
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

    public static java.sql.Date promptDate(Scanner scanner, String prompt) {
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
    
    public static double promptDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (true) {
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                System.out.print("Invalid double. Try again: ");
            }
        }
    }
}
