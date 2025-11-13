import java.util.Random;
import java.util.Scanner;

public class Game {

    // Single scanner for all input
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();

    // Player / game state
    private int playerHP;
    private String playerName;
    private String playerWeapon;
    private int choice;
    private int monsterHP;
    private boolean hasSilverRing;
    private int healthPotions;

    public static void main(String[] args){
        Game game = new Game();
        game.playerSetUp();
        game.townGate();
    }

    public void playerSetUp() {
        System.out.println("Welcome to the Adventure Game!");
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Adventurer";
        }

        // initialize stats
        playerHP = 10;
        monsterHP = 15;
        playerWeapon = "Dagger";
        hasSilverRing = false;
        healthPotions = 0;

        System.out.println("\nHello " + playerName + ", let's start the adventure!");
        System.out.println("Your HP: " + playerHP);
        System.out.println("Your Weapon: " + playerWeapon);
        System.out.println("Health Potions: " + healthPotions);
    }

    // Helper: get integer input in a range with a prompt
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " ");
            String line = scanner.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val < min || val > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Try again.");
            }
        }
    }

    // Overload that defaults min to 1 (common case)
    private int getIntInput(String prompt, int max) {
        return getIntInput(prompt, 1, max);
    }

    public void townGate(){
        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("You are at the gate of the town.");
        System.out.println("A guard is standing in front of you.");
        System.out.println("\nWhat do you want to do?\n");
        System.out.println("1: Talk to the guard");
        System.out.println("2: Try to bribe the guard");
        System.out.println("3: Leave to the crossroad");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1-3):", 3);

        if (choice == 1){
            if (hasSilverRing){
                ending();
            } else {
                System.out.println("Guard: Hello " + playerName + ". We don't allow strangers into the town without a token of trust.");
                System.out.println("Guard: If you can prove yourself (maybe help someone nearby), I may let you in.");
                townGate();
            }
        } else if (choice == 2) {
            System.out.println("You offer the guard a small amount of gold, but he declines politely.");
            System.out.println("Guard: I can't accept bribery. Do something heroic instead.");
            townGate();
        } else if (choice == 3) {
            crossRoad();
        } else {
            townGate();
        }

    }

    public void crossRoad (){
        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("You are at a crossroad. Which direction do you go?");
        System.out.println("1: North (river)");
        System.out.println("2: East (forest)");
        System.out.println("3: West (a nearby cave)");
        System.out.println("4: Back to the town gate");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1-4):", 4);

        if (choice == 1){
            north();
        } else if (choice == 2) {
            east();
        } else if (choice == 3) {
            west();
        } else if (choice == 4) {
            townGate();
        }else {
            crossRoad();
        }
    }

    public void north(){

        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("There is a peaceful river. You rest and recover some health.");
        System.out.println("Your HP is recovered by 2 points.");
        playerHP = Math.min(playerHP + 2, 20); // cap max HP
        System.out.println("Your HP: " + playerHP);
        System.out.println("\n1: Go back to the crossroad");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1):", 1);

        if (choice == 1){
            crossRoad();
        }else {
            north();
        }
    }

    public void east(){

        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("You walk into a forest and find an old sheath with a better weapon.");
        playerWeapon = "Shortsword";
        // also find a small health potion
        healthPotions += 1;
        System.out.println("You picked up a " + playerWeapon + ". It should help in fights.");
        System.out.println("You also found a Health Potion. (Heals 5 HP)");
        System.out.println("Health Potions: " + healthPotions);
        System.out.println("\n1: Go back to the crossroad");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1):", 1);

        if (choice == 1){
            crossRoad();
        }else {
            east();
        }
    }

    public void west(){

        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("A goblin appears from the cave and looks hostile!");
        System.out.println("1: Fight");
        System.out.println("2: Run back to the crossroad");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1-2):", 2);

        if (choice == 1){
            fight();
        } else if (choice == 2) {
            crossRoad();
        }else {
            west();
        }
    }

    public void fight(){
        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("The battle begins!");

        // initialize goblin HP for this encounter
        monsterHP = 12;

        while (monsterHP > 0 && playerHP > 0) {
            System.out.println("\nYour HP: " + playerHP + " | Goblin HP: " + monsterHP + " | Potions: " + healthPotions);
            System.out.println("1: Attack");
            System.out.println("2: Use item (Health Potion)");
            System.out.println("3: Run");

            choice = getIntInput("Choose (1-3):", 3);

            if (choice == 1) {
                // player attacks
                int playerAttack = (playerWeapon.equalsIgnoreCase("Dagger")) ? 2 : 4;
                // small variance
                int variance = random.nextInt(3) - 1; // -1,0,1
                playerAttack = Math.max(1, playerAttack + variance);
                System.out.println("You attack with your " + playerWeapon + " and deal " + playerAttack + " damage.");
                monsterHP -= playerAttack;

                if (monsterHP <= 0) {
                    System.out.println("You defeated the goblin!");
                    System.out.println("The goblin drops a small silver ring.");
                    hasSilverRing = true;
                    // chance to drop a potion too
                    if (random.nextBoolean()) {
                        healthPotions += 1;
                        System.out.println("The goblin also dropped a Health Potion!");
                    }
                    System.out.println("You obtained the Silver Ring. Return to the guard to enter the town.");
                    crossRoad();
                    return;
                }

                // goblin's turn
                int goblinAttack = 3 + (random.nextInt(3) - 1); // 2..4
                System.out.println("The goblin attacks and deals " + goblinAttack + " damage.");
                playerHP -= goblinAttack;

                if (playerHP <= 0) {
                    System.out.println("You have been defeated by the goblin.");
                    gameOver();
                    return;
                }

            } else if (choice == 2) {
                if (healthPotions > 0) {
                    int heal = 5;
                    int before = playerHP;
                    playerHP = Math.min(20, playerHP + heal);
                    healthPotions -= 1;
                    System.out.println("You used a Health Potion. HP: " + before + " -> " + playerHP);
                } else {
                    System.out.println("You don't have any Health Potions right now.");
                }
            } else if (choice == 3) {
                System.out.println("You run back to the crossroad.");
                crossRoad();
                return;
            }
        }

    }

    public void gameOver(){
        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("Game Over.");
        System.out.println("1: Restart");
        System.out.println("2: Exit");
        System.out.println("\n------------------------------------------------------------------\n");

        choice = getIntInput("Choose (1-2):", 2);
        if (choice == 1) {
            playerSetUp();
            townGate();
        } else {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }

    public void ending() {
        System.out.println("\n------------------------------------------------------------------\n");
        System.out.println("Guard: Oh, you returned with the silver ring! Welcome to our town, " + playerName + "!");
        System.out.println("Guard: Thank you for helping our people.");
        System.out.println("\n\n           THE END                    ");
        System.out.println("\n------------------------------------------------------------------\n");

        System.out.println("1: Play again");
        System.out.println("2: Exit");
        choice = getIntInput("Choose (1-2):", 2);
        if (choice == 1) {
            playerSetUp();
            townGate();
        } else {
            System.out.println("Farewell!");
            System.exit(0);
        }
    }

}
