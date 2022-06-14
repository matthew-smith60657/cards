package com.projects;

import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    public static void main(String[] args) {
        Card card = new Card("A","Spades");
        Deque<Card> hand = new ArrayDeque<>();
        int[] locationArray = new int[100];
        int locationSum = 0;

        for (int i = 0; i < locationArray.length; i++) {
            Deck deck = new Deck();
            int count = 0;
            do {
                card = deck.pullTopCard(true);
                count++;
            } while (!card.getFullName().equals("Ace of Spades"));
            System.out.println("Ace of Spades found at position " + count);
            locationArray[i] = count;
        }
        for (int location : locationArray) {
            locationSum += location;
        }
        System.out.println("Over " + locationArray.length + " initializations, the average position of the Ace of Spaces was " + (locationSum / locationArray.length));
    }
}