package com.techelevator.projects;

public class Card {
    private final String[] ACCEPTABLE_VALUES = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    private final String[] LONG_VALUES = {"Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King","Ace"};
    private final String[] ACCEPTABLE_SUITS = {"Clubs","Spades","Hearts","Diamonds"};
    private String value;
    private String suit; // How do I restrict suit values

    public Card(String value, String suit) {
        if (isValueAcceptable(value)) {
            this.value = value;
        }
        if (isSuitAcceptable(suit)) {
            this.suit = suit;
        }
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    private boolean isValueAcceptable(String value) {
        for (String acceptable_value : ACCEPTABLE_VALUES) {
            if (acceptable_value.equals(value)) {
                return true;
            }
        }
        System.out.println("Unacceptable value: " + value);
        return false;
    }

    private boolean isSuitAcceptable(String suit) {
        for (String acceptable_suit : ACCEPTABLE_SUITS) {
            if (acceptable_suit.equals(suit)) {
                return true;
            }
        }
        System.out.println("Unacceptable suit: " + suit);
        return false;
    }

    public String getShortSuit() {
        return suit.substring(0,1).toUpperCase();
    }

    public String getShortValueAndSuit() {
        return value+getShortSuit();
    }
}
