package com.projects;

import java.util.ArrayDeque;
import java.util.Deque;

public class Deck {
    private Deque<Card> deck = new ArrayDeque<>();
    private boolean isAceHigh = true;
    private final int STANDARD_AMOUNT_OF_SHUFFLES = 7;

    // Initialize a standard deck of 52 playing cards
    public Deck() {
        for (String suit : Card.ACCEPTABLE_SUITS) {
            for (String value : Card.ACCEPTABLE_VALUES) {
                Card card = new Card (value, suit);
                this.deck.addFirst(card);
            }
        }
        for (int i = 0; i < STANDARD_AMOUNT_OF_SHUFFLES; i++) {
            this.shuffle();
            if(Math.random() > .75) {
                this.cutDeckAndReturn();
            }
        }
        this.shuffle();
        this.cutDeckAndReturn();
    }
    public int size() {
        return this.deck.size();
    }

    public Card pullTopCard(boolean isQuiet) {
        Card card = deck.removeFirst();
        if(!isQuiet) {
            System.out.println("You pulled the " + card.getFullName());
        }
        return card;
    }
    // Return top half of deck, up to (exclusive) card index # cutPoint
    public Deque<Card> cutDeck(int cutPoint) {
        Deque<Card> splitDeck = new ArrayDeque<>();
        for (int i = 0; i < cutPoint; i++) {
            splitDeck.addLast(this.deck.removeFirst());
        }
        return splitDeck;
    }
    public void cutDeckAndReturn() {
        int cutPoint = this.deck.size() / 2 + (int)(Math.random() * deck.size() / 10 - deck.size() / 20);
        Deque<Card> splitDeck = this.cutDeck(cutPoint);
        for (Card card :
                splitDeck) {
            this.deck.addLast(splitDeck.removeFirst());
        }
        // System.out.println("I cut the deck!");
    }

    public void shuffle() {
        int cutPoint = this.deck.size() / 2 + (int)(Math.random() * deck.size() / 10 - deck.size() / 20);
        Deque<Card> splitDeck = this.cutDeck(cutPoint);
        Deque<Card> remainingDeck = this.cutDeck(this.deck.size());
        boolean useSplitDeck = true;
        // System.out.println("I shuffled the deck");
        while(!(splitDeck.isEmpty()  && remainingDeck.isEmpty())) {
            if(splitDeck.isEmpty()) {
                this.deck.addLast(remainingDeck.removeFirst());
            }
            else if(remainingDeck.isEmpty()) {
                this.deck.addLast(splitDeck.removeFirst());
            }
            else {
                double rndForMisfire = Math.random();
                if(rndForMisfire < .34) {
                    // on misfire, take from previous deck instead of alternating
                    useSplitDeck = !useSplitDeck;
                }
                if(useSplitDeck) {
                    this.deck.addFirst(splitDeck.removeLast());
                }
                else {
                    this.deck.addFirst(remainingDeck.removeLast());
                }
                // Alternate source deck
                useSplitDeck = !useSplitDeck;

            }
        }
    }
}
