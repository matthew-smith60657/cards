package com.projects;

import com.projects.dao.JdbcUserDao;
import com.projects.dao.UserDAO;
import com.projects.model.User;
import com.projects.service.GoFish;
import com.projects.service.Menu;
import com.projects.service.War;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

public class RunMe {

    User user;
    private final UserDAO userDao;
    private final Menu menu;
    private final DataSource ds;

    public static void main(String[] args) {
        // Initialize local postgresql server
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:postgresql://localhost:5432/Cards");
        bds.setUsername("postgres");
        bds.setPassword("postgres1");
        // Create instance of main loop & UserDAO
        RunMe runMe = new RunMe(bds);
        // Run core program loop
        runMe.run();
    }

    public RunMe(DataSource ds) {
        // Assign instanced userDao
        userDao = new JdbcUserDao(ds);
        this.ds = ds;
        menu = new Menu();
    }

    public void run() {
        int choice;
        // Infinite loop; exit program directly from choice processing method
        while (true) {
            // Login Prompt
            if (user == null) {
                choice = menu.promptForChoice("Login Menu", menu.LOGIN_MENU_CHOICES);
                // Process login choice
                loginSelection(choice);
            } else {
                // Game Menu
                choice = menu.promptForChoice("Game Menu", menu.GAME_MENU_CHOICES);
                // 1: War
                // 2: Go Fish
                // 99: logout (not implemented)
                // 0: logout & exit (set user to null)
                gameSelection(choice);
            }
        }
    }
    // Return boolean to continue game loop with user
    private void gameSelection(int choice) {
        // 0: logout user
        if (choice == 0) {
            user = null;
        }
        // 1: War
        else if (choice == 1) {
            boolean updateUserRecord = user.getWarId() == 0;
            War war = new War(user, menu, ds);
            System.out.println("warID: " + user.getWarId());
            if(updateUserRecord) {
                userDao.updateUserWarID(user);
            }
        }
        // 2: Go Fish
        else if (choice == 2) {
            boolean updateUserRecord = user.getGoFishId() == 0;
            GoFish goFish = new GoFish(user, menu);
            if(updateUserRecord) {
                userDao.updateUserGoFishID(user);
            }
        }
    }

    private void loginSelection(int choice) {
        // 0: exit
        if (choice == 0) {
            System.exit(0);
        }
        // 1: login existing user
        else if (choice == 1) {
            System.out.print("Welcome back, player! Refresh me on your name, please? ");
            String name = menu.promptForString();
            System.out.print("And your super-secret, super-secure password? ");
            String pwd = menu.promptForString();
            // Interact with UserDAO to retrieve user with existing record
            user = userDao.getUserByNameAndPassword(name, pwd);
            // if user is null, no user by that name found
            if (user == null) {
                System.out.println("I'm sorry, but I don't remember you.");
            }
            // else if user.user_id is -1 then the password was incorrect
            else if (user.getUserId() == -1) {
                System.out.println(user.getName() + ", I'm sorry but that wasn't the right super-secret, super-secure password...");
            } else {
                System.out.println("Good to see you again, " + user.getName() + ". Good luck!");
            }
        // 2: create new user
        } else if (choice == 2) {
            System.out.print("Welcome, new player!\nWhat shall I call you? ");
            String name = menu.promptForString();
            // TODO: 6/28/2022 Check if name already exists...
            System.out.print(name + ", is that right? ");
            boolean isAccepted = menu.promptForString().toUpperCase().equals("Y");
            while (!isAccepted) {
                System.out.print("\nMy mistake, what shall I call you? ");
                name = menu.promptForString();
                System.out.print(name + ", is that right? ");
                isAccepted = menu.promptForString().toUpperCase().equals("Y");
            }
            System.out.println("Wonderful! Best of luck, " + name + "!");
            // Reset & initialize for password loop
            isAccepted = false;
            String pwd = "";

            System.out.print("\nBefore you begin, please give a password to secure your account: ");
            while (!isAccepted) {
                // Request for password
                pwd = menu.promptForString();
                // Re-confirm password
                System.out.print("Could you repeat that for me? ");
                if (menu.promptForString().equals(pwd)) {
                    isAccepted = true;
                }
            }
            System.out.println("Marvelous! Best of luck, " + name + ", you're ready to play!");
            // Engage DAO to create new User
            int newId = userDao.createUser(name, pwd);
            // Assign recently created User to user
            user = userDao.getUser(newId);
        }
    }
/* OLD CODE
    public static int promptForInput() {
        final String[] options = {"Exit", "War", "Go Fish"};
        int choice = 0;
        boolean prompt = true;
        Scanner keyboard = new Scanner(System.in);

        while (prompt) {
            menuPrint(options);
            System.out.print("Enter your choice (0 to exit): ");
            try {
                choice = keyboard.nextInt();
                if (choice < 0) {
                    System.out.println("Only positive inputs please...");
                } else if (choice == 0) {
                    prompt = false;
                    System.out.println("Until next time...");
                } else {
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
    }*/
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

// String name = "Player One";
// While input isn't 'Exit'
        /*while (choice != 0) {
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
            }*/