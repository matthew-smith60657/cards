package com.projects;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int choice = -1;
        String name = "Player One";
        // While input isn't 'Exit'
        while (choice != 0) {
            // Display options, prompt for input, resolve input until acceptable
            choice = promptForInput();
            // Do the action
            // 1: War
            if (choice == 1) {
                War war = new War(name);
            }
            // 2: Go Fish
            if (choice == 2) {
                GoFish goFish = new GoFish(name);
            }
        }
    }

    public static int promptForInput() {
        final String[] options = {"Exit", "War", "Go Fish"};
        int choice = 0;
        boolean prompt = true;
        Scanner keyboard = new Scanner(System.in);

        while(prompt) {
            menuPrint(options);
            System.out.print("Enter your choice (0 to exit): ");
            try {
                choice = keyboard.nextInt();
                if (choice < 0) {
                    System.out.println("Only positive inputs please...");
                }
                else if (choice == 0) {
                    prompt = false;
                    System.out.println("Until next time...");
                }
                else {
                    prompt = false;
                    System.out.println("Initializing " + options[choice] + "!\n");
                }
            } catch (Exception ex) {
                System.out.println("Only numeric inputs please...");
                keyboard.nextLine();
            }
        }
        return choice;
    }
    public static void menuPrint(String[] options) {

        System.out.println("*** Let's Play Some Card Games!!! ***\n");

        for (int i = 1; i < options.length; i++) {
            System.out.println(i + ": " + options[i]);
        }
        System.out.println("-------------------");
        System.out.println("0: " + options[0] + "\n");
    }
/*
        Card card = new Card("A","Spades");
        Deque<Card> hand = new ArrayDeque<>();
        int[] locationArray = new int[100];
        int locationSum = 0;
        int maxPosition = 0;
        int minPosition = 53;

        for (int i = 0; i < locationArray.length; i++) {
            Deck deck = new Deck();
            int count = 0;
            do {
                card = deck.pullTopCard(true);
                count++;
            } while (!card.getFullName().equals("Ace of Spades"));
            System.out.println("Ace of Spades found at position " + count);
            if (count < minPosition) {
                minPosition = count;
            }
            if (count > maxPosition) {
                maxPosition = count;
            }
            locationArray[i] = count;
        }
        for (int location : locationArray) {
            locationSum += location;
        }
        System.out.println("Over " + locationArray.length + " initializations, the average position of the Ace of Spaces was " + (locationSum / locationArray.length));
        System.out.println("The max position was " + maxPosition + " and the min position was " + minPosition);
        Deck deck = new Deck();
*/
}