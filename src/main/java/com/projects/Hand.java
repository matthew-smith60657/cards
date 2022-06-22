package com.projects;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private String name;
    private boolean isUser;
    private List<Card> hand = new ArrayList<>();

    public Hand(String name, boolean isUser) {
        this.name = name;
        this.isUser = isUser;
    }
    public String getName() {
        if(isUser) {
            return name + "'s hand";
        }
        else {
            return "my hand";
        }
    }

    public void listHand() {
        for (Card card : hand) {
            System.out.print(card.getShortValueAndSuit() + " ");
        }
    }
    public void addCard(Card card) {
        hand.add(card);
    }
    public void discardCardByIndex(int index) {
        hand.remove(index);
    }
    public int size() {
        return hand.size();
    }
    public Card getCardByIndex(int index) {
        return hand.get(index);
    }
    public int findFirstCardByValue(String value) {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getValue().equals(value)) {
                return i;
            }
        }
        return -1;
    }
    public Card playFirstCardWithValue(String value) {
        // Find index of card
        int index = this.findFirstCardByValue(value);
        if(index == -1) {
            return null;
        }
        // Remove card by index
        return hand.remove(index);
    }
    public boolean isEmpty() {
        return this.hand.isEmpty();
    }

    public boolean isValueInHand(String s) {
        for (Card card : hand) {
            for (String suit : Card.ACCEPTABLE_SUITS)
            if(card.compareTo(new Card(s, suit)) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean isInHand(Card searchCard) {
        for (Card card : hand) {
            if(card.compareToWithSuit(searchCard) == 0){
                return true;
            }
        }
        return false;
    }

    public void sortHand(boolean isSmallToLarge) {
        // Do an n^2 / 2 sort
        for(int i = 0; i < this.hand.size() - 1; i++) {
            int index = i;
            for(int j = i + 1; j < this.hand.size(); j++) {
                int comparison = hand.get(index).compareToWithSuit(hand.get(j));
                // Find smallest/largest card
                if(comparison > 0 && isSmallToLarge) {
                    index = j;
                }
                else if(comparison < 0 && !isSmallToLarge) {
                    index = j;
                }
            }
            // Move smallest/largest card to position i, if it isn't already there
            if (index != i) {
                Card sortedCard = hand.remove(index);
                hand.add(i, sortedCard);
            }
        }
    }

    public int countValueInHand(String value) {
        int count = 0;
        for(Card card : hand) {
            if (card.getValue().equals(value)) {
                count++;
            }
        }
        return count;
    }
}
