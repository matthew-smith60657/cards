package com.projects;

import java.util.*;

public class GoFish {
    private final int STARTING_HAND_SIZE = 7;
    private final int MAX_COMPUTER_MEMORY_SIZE = 5;
    // Remember last few selections to avoid over-guessing a specific value
    // Let's do a list to treat it like a queue, but with easy searching functionality
    private List<String> previouslySelectedByComputer = new ArrayList<>();
    private List<String> guessedByUser = new ArrayList<>();
    private Deck deck = new Deck();
    private Hand playerOneHand;
    private Hand playerTwoHand;
    public GoFish(String name) {
        playerOneHand = new Hand(name, true);
        playerTwoHand = new Hand("Computer", false);

        boolean isPlayerOneTurn = (Math.random() > .5);

        // Deal five cards to each hand
        for (int i = 0; i < STARTING_HAND_SIZE; i++) {
            playerOneHand.addCard(deck.pullTopCard(false));
            playerTwoHand.addCard(deck.pullTopCard(true));
        }
        playerOneHand.sortHand(false);

        // Play loop, end game when one player's hand is empty
        boolean isPlayerOneVictorious = playGame(isPlayerOneTurn, name);
    }
    private boolean playGame(boolean isPlayerOneTurn, String name) {
        boolean isValueFound;
        String fishValueFromKeyboard;

        // Announce who's going first
        System.out.println("Let's flip a coin to determine who goes first!");
        if (isPlayerOneTurn) {
            System.out.println("Well done, " + name + ". You're up first.");
        }
        else {
            System.out.println("Looks like it's my turn first!");
        }

        while (!(playerOneHand.isEmpty() || playerTwoHand.isEmpty())) {
            // If user's turn, prompt for input
            if(isPlayerOneTurn) {
                System.out.print("Your hand: ");
                playerOneHand.listHand();
                System.out.println("\nYour turn, " + name + ". What shall you fish for?");
                fishValueFromKeyboard = getInputFromKeyboard();
                // Search the other hand for value from keyboard
                isValueFound = playerTwoHand.isValueInHand(fishValueFromKeyboard);
                // Resolve search result: Give all matching values in opposing hand to fisher, else draw from deck if not empty
                if(!isValueFound) {
                    System.out.println("GO FISH!!!");
                    goFish(playerOneHand, false);
                    // On adding a card, check if this makes 4-of-a-kind
                    boolean isFound = removeFourOfAKind(playerTwoHand, fishValueFromKeyboard);
                    if(isFound) {
                        System.out.println("Wow, you made a 4-of-a-kind with " + fishValueFromKeyboard + "s!");
                    }
                    playerOneHand.sortHand(false);
                } else {
                    System.out.println("Bah, yes...");
                    while (isValueFound) {
                        isValueFound = takeValueFromHand(fishValueFromKeyboard, playerOneHand, playerTwoHand);
                        // On adding a card, check if this makes 4-of-a-kind
                    }
                    boolean isFound = removeFourOfAKind(playerTwoHand, fishValueFromKeyboard);
                    if(isFound) {
                        System.out.println("Wow, you made a 4-of-a-kind with " + fishValueFromKeyboard + "s!");
                    }
                    playerOneHand.sortHand(false);
                }
            }
            else {
                // Have computer select a value from its hand
                String selectedValue = selectValueForComputer(playerTwoHand);
                System.out.println("Do you have any " + selectedValue + "s?");
                // Search P1 hand for selected value
                isValueFound = playerOneHand.isValueInHand(selectedValue);
                // Resolve search: go fish or take all values from P! hand
                if(!isValueFound) {
                    System.out.println("Time to go fishing...");
                    goFish(playerTwoHand, true);
                    // On adding a card, check if this makes 4-of-a-kind
                    boolean isFound = removeFourOfAKind(playerTwoHand, selectedValue);
                    if(isFound) {
                        System.out.println("I made a 4-of-a-kind with " + selectedValue + "s!!!");
                    }
                } else {
                    while (isValueFound) {
                        isValueFound = takeValueFromHand(selectedValue, playerTwoHand, playerOneHand);
                        // On adding a card, check if this makes 4-of-a-kind
                        boolean isFound = removeFourOfAKind(playerTwoHand, selectedValue);
                        if(isFound) {
                            System.out.println("I made a 4-of-a-kind with " + selectedValue + "s!!!");
                        }
                    }
                }
            }
            // alternate
            isPlayerOneTurn = !isPlayerOneTurn;
        }
        // Determine victor
        return playerOneHand.isEmpty();
    }
    private boolean removeFourOfAKind(Hand hand, String value) {
        int valueCount = 0;
        for(int i = 0; i < hand.size(); i++) {
            if(hand.isValueInHand(value)) {
                valueCount++;
            }
        }
        if(valueCount == 4) {
            for(int i = 0; i < hand.size(); i++) {
                if(hand.isValueInHand((value))) {
                    hand.discardCardByIndex(i);
                    // hand.size should be one element smaller now, so redo this index check
                    i--;
                }
            }
            return true;
        }
        return false;
    }

    private String selectValueForComputer(Hand playerTwoHand) {
        int index = 0;
        boolean isGoodChoice = false;
        int loopCount = 0;
        while(!isGoodChoice) {
            // Get a random index from the hand
            index = (int) (Math.random() * (double) playerTwoHand.size());
            System.out.println("Let's look at index" + index + ": " + playerTwoHand.getCardByIndex(index).getFullName());
            // Check against memory & redo if needed; add to memory if not found
            isGoodChoice = !isInMemory(playerTwoHand.getCardByIndex(index).getValue(), false);
            System.out.println("Is that value in memory? " + !isGoodChoice);
            if(loopCount > 5) {
                // After five tries, pick the last
                isGoodChoice = isInMemory(playerTwoHand.getCardByIndex(0).getValue(), true);
            }
            loopCount++;
        }
        // Return value
        System.out.println("Returning " + playerTwoHand.getCardByIndex(index).getLongValue());
        return playerTwoHand.getCardByIndex(index).getValue();
    }
    private boolean isInMemory(String check, boolean forceUse) {
        for (String s : previouslySelectedByComputer) {
            System.out.println("Checking " + s + " equality to " + check + ": " + s.equals(check));
            if(s.equals(check) && !forceUse) {
                System.out.println("Too bad, found " + check + " in memory.");
                return true;
            }
        }
        // Not in memory so add check to List, and remove index 0 if GT MAX
        System.out.println("I didn't find " + check + " in memory.  Adding now");
        // Is the card available to be selected?
        previouslySelectedByComputer.add(check);
        if(previouslySelectedByComputer.size() > MAX_COMPUTER_MEMORY_SIZE) {
            System.out.println("Memory too large, forgetting " + previouslySelectedByComputer.get(0) + " from index 0.");
            previouslySelectedByComputer.remove(0);
        }
        return false;
    }

    private void goFish(Hand hand, boolean isQuiet) {
        hand.addCard(deck.pullTopCard(isQuiet));
    }
    private boolean takeValueFromHand(String value, Hand playerHand, Hand opponentHand) {
        Card takenCard = opponentHand.playFirstCardWithValue(value);
        System.out.println("Moving " + takenCard.getFullName() + " from " + opponentHand.getName() + " to " + playerHand.getName());
        playerHand.addCard(takenCard);
        return opponentHand.isValueInHand(value);
    }
    private String getInputFromKeyboard() {
        Scanner keyboard = new Scanner(System.in);
        boolean isGoodInput = false;
        String keyboardInput = "";

        while(!isGoodInput) {
            // Could put try & catch exceptions, but any that occur here are program-breaking anyway
            keyboardInput = keyboard.nextLine();
            // Check if input is a card; return error message
            if(!Card.isMyValueAcceptable(keyboardInput)) {
                System.out.print("I'm sorry, but " + keyboardInput + " isn't an acceptable value...\nHere are the acceptable card values -- ");
                Card.printAcceptableValues();
                System.out.print("Your hand: ");
                playerOneHand.listHand();
            }
            // Else, check if input is in hand; return rules message
            else if(!playerOneHand.isValueInHand(keyboardInput)) {
                System.out.println("You can only fish for values already in your hand...");
                System.out.print("Your hand: ");
                playerOneHand.listHand();
            }
            // Break Loop
            else {
                isGoodInput = true;
            }
        }
        return keyboardInput;
    }
}
