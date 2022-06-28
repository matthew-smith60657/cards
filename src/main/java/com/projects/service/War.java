package com.projects.service;

import com.projects.model.Card;
import com.projects.model.Deck;
import com.projects.model.User;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class War {
    private int playCount = 0;
    private User player;
    // using Menu.keyboard for all input now
    private Menu menu;
    // private Scanner keyboard = new Scanner(System.in);
    // Any need to make these Decks instead of generic Deques? I could have a secret readout option if they were.
    private Deque<Card> playerOneHand = new ArrayDeque<>();
    private Deque<Card> playerTwoHand = new ArrayDeque<>();

    // Once we've emptied out the initial deck, we should be able to use this for holding the kitty on ties
    private Deck stack = new Deck();

    public War(User player, Menu menu) {
        boolean isAskingForInput = true;
        this.player = player;
        this.menu = menu;
        System.out.println("Welcome to WAR, " + player.getName() + ". Good luck!");
        // Deal out cards evenly, starting randomly with user (1) or computer (2)
        dealInitialStack();
        // While neither player's hand is empty, the game continues --
        while (!(playerOneHand.isEmpty() || playerTwoHand.isEmpty())) {
            // Prompt for user input:
            // Peek at top card, count cards in hand (approximate flavor text!), play card?
            if (isAskingForInput) {
                isAskingForInput = promptForInput();
            }
            // Resolve playing a card:
            // win, randomly put both cards & any cards in stack on bottom of player 1's deck
            //       announce cards in stack before adding to bottom
            resolvePlay();
            // lose, randomly put both cards & any cards in stack on bottom of player 2's deck

            // tie, put both top cards in stack along with 3 quiet cards from both tops in stack
            //      loop this resolution until win or lose
        }
        // End while -- declare someone the victor!
        System.out.println("After " + playCount + " plays, a victor has emerged.");
        if (playerOneHand.isEmpty()) {
            System.out.println("I'm so sorry, " + player.getName() + ", but you have lost...");
        }
        else if (playerTwoHand.isEmpty()){
            System.out.println("Congratulations, " + player.getName() + ", you've won!!");
        }
        else {
            System.out.println("Something has gone wrong...");
        }
        // Add name to the Hall of Fame & display
    }

    private void resolvePlay() {
        int resolution = playerOneHand.getFirst().compareTo(playerTwoHand.getFirst());
        System.out.println("You play the " + playerOneHand.getFirst().getFullName() + ".");
        System.out.println("I play the " + playerTwoHand.getFirst().getFullName() + ".");
        if(resolution > 0) {
            // Player one wins
            System.out.println("No problem, you win this one...\n");
            resolveWinner(true);
            if(stack.size() > 0) {
                resolveKitty(true);
            }
        }
        else if(resolution < 0) {
            System.out.println("Nice, I win this one.\n");
            resolveWinner(false);
            if(stack.size() > 0) {
                resolveKitty(false);
            }
        }
        else {
            System.out.println("A tie...!");
            System.out.println("Put 3 cards in the kitty and I will, too.\n");
            for (int i = 0; i < 3; i++) {
                if(Math.random() >= 0.5) {
                    stack.addFirst(playerOneHand.removeFirst());
                    stack.addFirst(playerTwoHand.removeFirst());
                }
                else {
                    stack.addFirst(playerTwoHand.removeFirst());
                    stack.addFirst(playerOneHand.removeFirst());
                }
            }
        }
        playCount++;

    }
    private void resolveKitty(boolean isPlayerOneWinner) {
        // Reveal the cards in the kitty/stack and add to the bottom of the winner's hand
        if(isPlayerOneWinner) {
            System.out.println("Well done, you win the kitty.\nGo ahead, and add these to the bottom of your hand...");
            while(stack.size() > 0) {
                System.out.println("The " + stack.peekTopCard());
                playerOneHand.addLast(stack.pullTopCard(true));
            }
        } else {
            System.out.println("Too bad, I win the kitty!!\n");
            while(stack.size() > 0) {
                playerTwoHand.addLast(stack.pullTopCard(true));
            }
        }

    }

    private void resolveWinner(boolean isPlayerOneWinner) {
        if(isPlayerOneWinner) {
            // add each players' top cards randomly to the bottom of the winners stack
            if(Math.random() >= 0.5) {
                playerOneHand.addLast(playerOneHand.removeFirst());
                playerOneHand.addLast(playerTwoHand.removeFirst());
            }
            else {
                playerOneHand.addLast(playerTwoHand.removeFirst());
                playerOneHand.addLast(playerOneHand.removeFirst());
            }
        }
        else {
            if(Math.random() >= 0.5) {
                playerTwoHand.addLast(playerOneHand.removeFirst());
                playerTwoHand.addLast(playerTwoHand.removeFirst());
            }
            else {
                playerTwoHand.addLast(playerTwoHand.removeFirst());
                playerTwoHand.addLast(playerOneHand.removeFirst());
            }
        }
    }

    public boolean promptForInput() {
        boolean isReadyToPlay = false;
        // Print approximate size of Player 1's deck
        describeHandSize(playerOneHand);
        //
        while(true) {
            System.out.println("Do you want to (P)eek at your top card or are you ready to get on with it?");
            String command = menu.promptForString();
            if(command.toLowerCase().startsWith("p")) {
                // Peek at top card in hand
                // TODO: 6/14/2022 add some flavor text to the top card
                System.out.println("Your top card is the " + playerOneHand.getFirst().getFullName() + ".");
            }
            else if (command.equals("status")) {
                System.out.println("** Current Status **");
                System.out.println("You are holding " + playerOneHand.size() + " cards.");
                System.out.println("I am holding " + playerTwoHand.size() + " cards.");
            }
            else if (command.equals("loop")) {
                return false;
            }
            // else if secret menu option to report out both hands?
            else {
                break;
            }

        }
        return true;
    }

    private void describeHandSize(Deque<Card> playerOneHand) {
        if (playerOneHand.size() == 51) {
            System.out.println("Your opponent is down to his last card!!");
        }
        else if (playerOneHand.size() > 45) {
            System.out.println("You feel like you're practically holding a full deck!");
        }
        else if(playerOneHand.size() > 35) {
            System.out.println("You're holding a substantial amount of cards.");
        }
        else if (playerOneHand.size() > 27) {
            System.out.println("It looks like you're holding more cards than your opponent.");
        }
        else if (playerOneHand.size() > 24) {
            System.out.println("You seem to have as many cards as your opponent.");
        }
        else if (playerOneHand.size() > 16) {
            System.out.println("It looks like your opponent is holding more cards than you...");
        }
        else if (playerOneHand.size() > 6) {
            System.out.println("You're not holding a whole lot of cards...");
        }
        else if (playerOneHand.size() > 1) {
            System.out.println("You're down to your last " + playerOneHand.size() + " cards...");
        }
        else {
            System.out.println("This is it, your last card...");
        }
    }

    public void dealInitialStack() {
        boolean dealPlayerOneNext = Math.random() >= 0.5;

        while (stack.size() > 0) {
            if (dealPlayerOneNext) {
                playerOneHand.addFirst(stack.pullTopCard(true));
            }
            else {
                playerTwoHand.addFirst(stack.pullTopCard(true));
            }
            dealPlayerOneNext = !dealPlayerOneNext;
        }
    }
}
