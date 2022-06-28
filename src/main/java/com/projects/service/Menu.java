package com.projects.service;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    public final String[] LOGIN_MENU_CHOICES = {"exit", "User Login", "Create New User"};
    public final String[] GAME_MENU_CHOICES = {"logout", "War", "Go Fish"};
    private final Scanner keyboard;
    public Menu() {
        keyboard = new Scanner(System.in);
    }
    public void flush() {
        keyboard.nextLine();
    }
    // General prompt for input from keyboard
    public String promptForString() {
        return keyboard.nextLine();
    }
    // Display menu choices
    public int promptForChoice(String header, String[] choices) {
        while(true) {
            System.out.println("\n##########################");
            // TODO: 6/28/2022 Can I automatically center the header given some max length?
            System.out.println("       " + header);
            System.out.println("##########################");
            for (int i = 1; i < choices.length; i++) {
                System.out.println(i + ": " + choices[i]);
            }
            System.out.println("--------------------------");
            System.out.println("0: " + choices[0]);
            System.out.println("##########################");
            System.out.print("\nPlease enter your choice: ");
            try {
                int input = keyboard.nextInt();
                if(input < 0 || (input >= choices.length && input != 99)) {
                    throw new InputMismatchException();
                }
                flush();
                return input;
            } catch (InputMismatchException ex) {
                System.out.println("That's not a valid choice...");
                flush();
            } catch (Exception ex) {
                System.out.println("Internal Error: " + ex.getMessage());
                flush();
            }
        }
    }
}
