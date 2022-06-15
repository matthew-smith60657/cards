package com.projects;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class GoFish {

    private final int STARTING_HAND_SIZE = 7;
    Deck deck = new Deck();
    List<Card> playerOneHand = new ArrayList<>();
    public GoFish(String name) {
        // Deal five cards to each hand
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            playerOneHand.add(deck.pullTopCard(false));
        }

    }
}
