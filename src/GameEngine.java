import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {

    public enum Scene { TOWN_GATE, CROSSROAD, NORTH, EAST, WEST, FIGHT, GAME_OVER, ENDING }

    private final Random random = new Random();

    // Player / game state
    private int playerHP;
    private String playerName;
    private String playerWeapon;
    private int monsterHP;
    private boolean hasSilverRing;
    private int healthPotions;

    private Scene currentScene;
    private String lastMessage = "";

    public GameEngine() {
    }

    public void reset(String name) {
        playerName = (name == null || name.trim().isEmpty()) ? "Adventurer" : name.trim();
        playerHP = 10;
        monsterHP = 15;
        playerWeapon = "Dagger";
        hasSilverRing = false;
        healthPotions = 0;
        currentScene = Scene.TOWN_GATE;
        lastMessage = "";
    }

    public String getPlayerName() { return playerName; }
    public int getPlayerHP() { return playerHP; }
    public int getPotions() { return healthPotions; }
    public String getWeapon() { return playerWeapon; }
    public boolean hasSilverRing() { return hasSilverRing; }

    public String getSceneText() {
        switch (currentScene) {
            case TOWN_GATE:
                if (hasSilverRing) {
                    return "anime: Oh, you returned with the silver ring! Welcome to our town, " + playerName + "!\n\nFuck you";
                }
                return "You are at the gate of the town. An anime girl is standing in front of you." + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case CROSSROAD:
                return "You are at a crossroad. Which direction do you go?" + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case NORTH:
                return "There is a peaceful river. You fapped and recover some health." + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case EAST:
                return "You walk into a forest and find an old condom with a better weapon and sometimes a meth." + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case WEST:
                return "A woman appears from the cave and looks hostile!" + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case FIGHT:
                return "The battle begins!" + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case GAME_OVER:
                return "Game Over." + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            case ENDING:
                return "Thank you for helping our people. you make a harem in the town and the end." + (lastMessage.isEmpty() ? "" : "\n\n" + lastMessage);
            default:
                return "";
        }
    }

    public List<String> getOptions() {
        List<String> opts = new ArrayList<>();
        switch (currentScene) {
            case TOWN_GATE:
                if (hasSilverRing) {
                    opts.add("Enter the town (ending)");
                    opts.add("Play again");
                    opts.add("Exit");
                } else {
                    opts.add("Talk to the anime");
                    opts.add("Try to bribe the anime");
                    opts.add("Leave to the crossroad");
                }
                break;
            case CROSSROAD:
                opts.add("North (piss)");
                opts.add("East (grass)");
                opts.add("West (cave)");
                opts.add("Back to town gate");
                break;
            case NORTH:
                opts.add("Go back to the crossroad");
                break;
            case EAST:
                opts.add("Go back to the crossroad");
                break;
            case WEST:
                opts.add("Show your young condom");
                opts.add("Run back to the crossroad");
                break;
            case FIGHT:
                opts.add("Attack");
                opts.add("Use item (old condom)");
                opts.add("Run");
                break;
            case GAME_OVER:
                opts.add("Restart");
                opts.add("Exit");
                break;
            case ENDING:
                opts.add("git gud");
                opts.add("loser");
                break;
        }
        return opts;
    }

    public void chooseOption(int index) {
        switch (currentScene) {
            case TOWN_GATE:
                if (hasSilverRing) {
                    if (index == 0) {
                        currentScene = Scene.ENDING;
                    } else if (index == 1) {
                        reset(playerName);
                    } else {
                        currentScene = Scene.GAME_OVER; // use GAME_OVER as exit sink
                    }
                } else {
                    if (index == 0) {
                        // talk
                        lastMessage = "anime: yo " + playerName + ". We don't allow strangers into the town without a token of trust.\nanime: If you can prove yourself (maybe help me lose my virginity), I may let you in.";
                    } else if (index == 1) {
                        // bribe
                        lastMessage = "You offer the anime a small amount of gold, but she declines politely.\nanime: now that i think about it, i feel like a cheap anime so no.";
                    } else if (index == 2) {
                        lastMessage = "";
                        currentScene = Scene.CROSSROAD;
                    }
                }
                break;
            case CROSSROAD:
                if (index == 0) currentScene = Scene.NORTH;
                else if (index == 1) currentScene = Scene.EAST;
                else if (index == 2) currentScene = Scene.WEST;
                else currentScene = Scene.TOWN_GATE;
                break;
            case NORTH:
                if (index == 0) {
                    playerHP = Math.min(playerHP + 2, 20);
                    lastMessage = "You recovered 2 HP.\nYour HP: " + playerHP;
                    currentScene = Scene.CROSSROAD;
                }
                break;
            case EAST:
                if (index == 0) {
                    playerWeapon = "old condom";
                    healthPotions += 1;
                    lastMessage = "You picked up a " + playerWeapon + ". You also found a meth.";
                    currentScene = Scene.CROSSROAD;
                }
                break;
            case WEST:
                if (index == 0) {
                    currentScene = Scene.FIGHT;
                    monsterHP = 12;
                } else {
                    currentScene = Scene.CROSSROAD;
                }
                break;
            case FIGHT:
                if (index == 0) { // Attack
                    int playerAttack = (playerWeapon.equalsIgnoreCase("Dagger")) ? 2 : 4;
                    int variance = random.nextInt(3) - 1; // -1..1
                    playerAttack = Math.max(1, playerAttack + variance);
                    monsterHP -= playerAttack;
                    if (monsterHP <= 0) {
                        hasSilverRing = true;
                        StringBuilder sb = new StringBuilder();
                        sb.append("You attack and deal ").append(playerAttack).append(" damage.\n");
                        sb.append("You defeated the woman! The woman drops a small silver ring.");
                        if (random.nextBoolean()) {
                            healthPotions += 1;
                            sb.append(" The woman also dropped another condom!");
                        }
                        lastMessage = sb.toString();
                        currentScene = Scene.CROSSROAD;
                    } else {
                        // monster attacks
                        int goblinAttack = 3 + (random.nextInt(3) - 1);
                        playerHP -= goblinAttack;
                        lastMessage = "You attack and deal " + playerAttack + " damage.\nThe woman attacks and deals " + goblinAttack + " damage.";
                        if (playerHP <= 0) {
                            lastMessage += "\nYou have been defeated by the woman.";
                            currentScene = Scene.GAME_OVER;
                        }
                    }
                } else if (index == 1) { // Use potion
                    if (healthPotions > 0) {
                        int before = playerHP;
                        playerHP = Math.min(20, playerHP + 5);
                        healthPotions -= 1;
                        lastMessage = "You used a Health Potion. HP: " + before + " -> " + playerHP;
                    } else {
                        lastMessage = "You don't have any Health Potions right now.";
                    }
                } else { // Run
                    lastMessage = "You run back to the crossroad.";
                    currentScene = Scene.CROSSROAD;
                }
                break;
            case GAME_OVER:
                if (index == 0) {
                    reset(playerName);
                } else {
                    // Exit: move to ENDING to allow UI to close or show message
                    currentScene = Scene.ENDING;
                }
                break;
            case ENDING:
                if (index == 0) {
                    reset(playerName);
                } else {
                    currentScene = Scene.GAME_OVER;
                }
                break;
        }
    }

    public Scene getCurrentScene() { return currentScene; }
}
