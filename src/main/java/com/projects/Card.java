package com.projects;

public class Card {
    protected static final String[] ACCEPTABLE_VALUES = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    private final String[] LONG_VALUES = {"Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King","Ace"};
    protected static final String[] ACCEPTABLE_SUITS = {"Clubs","Spades","Hearts","Diamonds"};
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

    private int getNumericValue(boolean isAceHigh) {
        int result = 0;
        for (int i = 0; i < ACCEPTABLE_VALUES.length; i++) {
            if (ACCEPTABLE_VALUES[i].equals(value)) {
                result = i + 2;
            }
        }
        if(!isAceHigh && result == 14) {
            result = 1;
        }
        return result;
    }

    public String getShortSuit() {
        return suit.substring(0,1).toUpperCase();
    }

    public String getShortValueAndSuit() {
        return value+getShortSuit();
    }
    public String getLongValue() {
        for (int i = 0; i < ACCEPTABLE_VALUES.length; i++) {
            if (this.value.equals(ACCEPTABLE_VALUES[i])) {
                return LONG_VALUES[i];
            }
        }
        return "";
    }

    public String getFullName() {
        return this.getLongValue() + " of " + suit;
    }

    public int compareTo(Card secondCard) { // Need overload with isAceHigh boolean
        if (this.getNumericValue(true) > secondCard.getNumericValue(true)) {
            return 1;
        }
        else if (this.getNumericValue(true) < secondCard.getNumericValue(true)) {
            return -1;
        }
        else return 0;
    }

    public boolean isSameSuit (Card secondCard) {
        return this.suit.equals(secondCard.getSuit());
    }
}
