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
                    return "Guard: Oh, you returned with the silver ring! Welcome to our town, " + playerName + "!\n\nTHE END";
                }
                return "You are at the gate of the town. A guard is standing in front of you.";
            case CROSSROAD:
                return "You are at a crossroad. Which direction do you go?";
            case NORTH:
                return "There is a peaceful river. You rest and recover some health.";
            case EAST:
                return "You walk into a forest and find an old sheath with a better weapon and sometimes a potion.";
            case WEST:
                return "A goblin appears from the cave and looks hostile!";
            case FIGHT:
                return "The battle begins!";
            case GAME_OVER:
                return "Game Over.";
            case ENDING:
                return "Thank you for helping our people. THE END.";
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
                    opts.add("Talk to the guard");
                    opts.add("Try to bribe the guard");
                    opts.add("Leave to the crossroad");
                }
                break;
            case CROSSROAD:
                opts.add("North (river)");
                opts.add("East (forest)");
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
                opts.add("Fight");
                opts.add("Run back to the crossroad");
                break;
            case FIGHT:
                opts.add("Attack");
                opts.add("Use item (Health Potion)");
                opts.add("Run");
                break;
            case GAME_OVER:
                opts.add("Restart");
                opts.add("Exit");
                break;
            case ENDING:
                opts.add("Play again");
                opts.add("Exit");
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
                        // remain at gate after message (UI will refresh text)
                    } else if (index == 1) {
                        // bribe
                    } else if (index == 2) {
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
                    currentScene = Scene.CROSSROAD;
                }
                break;
            case EAST:
                if (index == 0) {
                    playerWeapon = "Shortsword";
                    healthPotions += 1;
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
                        if (random.nextBoolean()) {
                            healthPotions += 1;
                        }
                        currentScene = Scene.CROSSROAD;
                    } else {
                        // monster attacks
                        int goblinAttack = 3 + (random.nextInt(3) - 1);
                        playerHP -= goblinAttack;
                        if (playerHP <= 0) {
                            currentScene = Scene.GAME_OVER;
                        }
                    }
                } else if (index == 1) { // Use potion
                    if (healthPotions > 0) {
                        playerHP = Math.min(20, playerHP + 5);
                        healthPotions -= 1;
                    } else {
                        // nothing happens
                    }
                } else { // Run
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
