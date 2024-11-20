import java.util.*;

public class TextAdventureGame {
    static String currentRoom = "Entrance";
    static List<String> inventory = new ArrayList<>();
    static int playerHealth = 100;
    static boolean treasureUnlocked = false;
    static Set<String> achievements = new HashSet<>();

    // there are like four rooms- Enterance,forest,dungeon,treasure room

    static Map<String, String> roomDescriptions = new HashMap<>() {{
        put("Entrance", "You are at the entrance of the dungeon. Exits: north.");
        put("Forest", "You are in a dense forest. Exits: south, east.");
        put("Dungeon", "You are in a dark dungeon. Exits: west, north.");
        put("Treasure Room", "You are in the treasure room. Solve the riddle to unlock the treasure.");
    }};
   // exits is like from which direction you can leave the room
    static Map<String, List<String>> roomExits = Map.of(
            "Entrance", List.of("north"),
            "Forest", List.of("south", "east"),
            "Dungeon", List.of("west", "north"),
            "Treasure Room", List.of("south")
    );
  // In forest you can get key to open treasure
    // In dungeon you can  get health
    static Map<String, String> npcHints = new HashMap<>() {{
        put("Forest", "Old Man: 'A key is hidden in the dungeon, and a riddle guards the treasure.'");
        put("Dungeon", "Warrior Spirit: 'Only the brave can enter the treasure room.'");
    }};

    static Map<String, String> roomItems = new HashMap<>() {{
        put("Dungeon", "key");
        put("Forest", "potion");
    }};
// health of enemies ih these rooms
    static Map<String, Integer> enemies = new HashMap<>() {{
        put("Dungeon", 50);
        put("Treasure Room", 100);
    }};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Enhanced Adventure Game!");
        // help commands
        System.out.println("Type 'help' for a list of commands.\n");

        while (true) {
            if (playerHealth <= 0) {
                // if health is negative you are loose from game
                System.out.println("Game Over! You have been defeated.");
                displayAchievements();
                break;
            }

            if (currentRoom.equals("Treasure Room") && treasureUnlocked) {
                // if you solve riddle and entered treasure room you are the winner
                System.out.println("Congratulations! You found the treasure and won the game!");
                achievements.add("Treasure Hunter");
                displayAchievements();
                break;
            }

            System.out.println(roomDescriptions.get(currentRoom));
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.startsWith("go ")) {
                navigate(command.split(" ")[1]);
            } else if (command.equals("check inventory")) {
                System.out.println("Inventory: " + inventory);
            } else if (command.equals("talk")) {
                talkToNPC();
            } else if (command.equals("attack")) {
                attackEnemy();
            } else if (command.equals("run")) {
                runFromEnemy();
            } else if (command.equals("use potion")) {
                usePotion();
            } else if (command.equals("solve riddle")) {
                solveRiddle();
            } else if (command.equals("save")) {
                saveProgress();
            } else if (command.equals("load")) {
                loadProgress();
            } else if (command.equals("help")) {
                displayHelp();
            } else {
                System.out.println("Invalid command. Type 'help' for a list of commands.");
            }
        }

        scanner.close();
    }

    static void navigate(String direction) {
        if (enemies.containsKey(currentRoom)) {
            // if you enter dungeon or forest you should defeat with enemy first
            System.out.println("An enemy blocks your path! You must defeat it first.");
            return;
        }

        List<String> exits = roomExits.get(currentRoom);
        if (exits.contains(direction)) {
            switch (currentRoom + "-" + direction) {
                case "Entrance-north" -> currentRoom = "Forest";
                case "Forest-south" -> currentRoom = "Entrance";
                case "Forest-east" -> currentRoom = "Dungeon";
                case "Dungeon-west" -> currentRoom = "Forest";
                case "Dungeon-north" -> currentRoom = "Treasure Room";
                case "Treasure Room-south" -> currentRoom = "Dungeon";
            }
            collectItem();
        } else {
            System.out.println("You can't go that way!");
        }
    }

    static void collectItem() {
        if (roomItems.containsKey(currentRoom)) {
            String item = roomItems.get(currentRoom);
            System.out.println("You found a " + item + "!");
            inventory.add(item);
            roomItems.remove(currentRoom);
            achievements.add("Collector");
        }
    }

    static void talkToNPC() {
        if (npcHints.containsKey(currentRoom)) {
            System.out.println(npcHints.get(currentRoom));
        } else {
            System.out.println("There's no one to talk to here.");
        }
    }

    static void attackEnemy() {
        if (enemies.containsKey(currentRoom)) {
            int enemyHealth = enemies.get(currentRoom);
            System.out.println("You attack the enemy!");
            Random random = new Random();
            int damage = random.nextInt(25) + 15; // Random damage
            enemyHealth -= damage;
            playerHealth -= 10;
            System.out.println("Enemy health: " + enemyHealth);
            System.out.println("Your health: " + playerHealth);

            if (enemyHealth <= 0) {
                System.out.println("You defeated the enemy!");
                enemies.remove(currentRoom);
                achievements.add("Warrior");
            } else {
                enemies.put(currentRoom, enemyHealth);
            }
        } else {
            System.out.println("There's nothing to attack here.");
        }
    }

    static void runFromEnemy() {
        if (enemies.containsKey(currentRoom)) {
            System.out.println("You ran away to the previous room.");
            navigate("south");
        } else {
            System.out.println("There's nothing to run from here.");
        }
    }

    static void usePotion() {
        if (inventory.contains("potion")) {
            playerHealth = Math.min(playerHealth + 30, 100);
            inventory.remove("potion");
            System.out.println("You used a potion. Your health is now " + playerHealth);
            achievements.add("Survivor");
        } else {
            System.out.println("You don't have any potions!");
        }
    }
// solve riddle to win the game
    static void solveRiddle() {
        if (!inventory.contains("key")) {
            System.out.println("The treasure room is locked. You need a key.");
            return;
        }
        System.out.println("Riddle: The more of this you take, the more you leave behind. What is it?");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("footsteps")) {
            System.out.println("The riddle is solved! The treasure is unlocked!");
            treasureUnlocked = true;
        } else {
            System.out.println("Wrong answer! Try again.");
        }
    }


    static void saveProgress() {
        // Simulate saving progress (extend with file writing if needed)
        System.out.println("Game progress saved!");
    }

    static void loadProgress() {
        // Simulate loading progress (extend with file reading if needed)
        System.out.println("Game progress loaded!");
    }
  // getting help commands
    static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  go [direction] - Move to a different room (north, south, east, west)");
        System.out.println("  check inventory - Check your inventory");
        System.out.println("  talk - Talk to an NPC if available");
        System.out.println("  attack - Attack an enemy if present");
        System.out.println("  run - Run from an enemy");
        System.out.println("  use potion - Use a potion to restore health");
        System.out.println("  solve riddle - Solve the treasure room's riddle");
        System.out.println("  save - Save your progress");
        System.out.println("  load - Load your progress");
        System.out.println("  help - Display this help message");
    }
// you can what are achievements you won until the end of game
    static void displayAchievements() {
        System.out.println("Achievements unlocked:");
        achievements.forEach(achievement -> System.out.println("  - " + achievement));
    }
}
