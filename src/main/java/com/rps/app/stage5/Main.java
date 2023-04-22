package com.rps.app.stage5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

enum Gesture {
    ROCK("scissors", "rock"),
    PAPER("rock", "paper"),
    SCISSORS("paper", "scissors");

    private final String winsAgainst;
    private final String draw;
    Gesture(String winsAgainst, String draw) {
        this.winsAgainst = winsAgainst;
        this.draw = draw;
    }

    public String getWinsAgainst() {
        return winsAgainst;
    }

    public String getDraw() {
        return draw;
    }
}

enum Outcome {
    WIN(100),
    LOSS(0),
    DRAW(50);

    private final int value;

    Outcome(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class Main {
    private int userRating;
    private final List<String> options; // Input options [rock, paper, scissors, lizard, spock]
    private final boolean playOG; // Play the OG rps game or the new game with `options`

    private Main(Map<String, Integer> ratings, String username, String options) {
        if (!ratings.containsKey(username)) {
            ratings.put(username, 0);
        }
        this.userRating = ratings.get(username);
        this.playOG = options.isEmpty();
        if (!playOG) {
            this.options = Arrays.asList(options.split(","));
            Collections.reverse(this.options);
        } else {
            this.options = null;
        }
        System.out.print("Okay, let's start\n");
    }

    public static void main(String[] args) {
        String fileName = "src/main/java/com/rps/app/stage5/rating.txt";
        Map<String, Integer> ratings = readRatingsFromFile(fileName);
        String name = getUsername();
        String options = getUserOptions();
        Main app = new Main(ratings, name, options);

        while (true) {
            int result = app.run();
            if (result == -1) break;
        }
        System.out.println("Bye!");
    }

    private int run() {
        String userInput = getUserInput();

        // Check for invalid inputs, exit, and rating
        if (userInput == null) {
            return 0;
        } else if (userInput.equals("!exit")) {
            return -1;
        } else if (userInput.equals("!rating")) {
            int rating = getUserRating();
            System.out.printf("Your rating: %d\n", rating);
            return 0;
        }

        if (playOG) {
            if (userInput.equals(Gesture.ROCK.getDraw()) ||
                    userInput.equals(Gesture.PAPER.getDraw()) ||
                    userInput.equals(Gesture.SCISSORS.getDraw())) {
                playGame(userInput);
            }
        } else {
            playGameWithOptions(userInput);
        }

        return 0;
    }

    private void playGameWithOptions(String input) {
        int userIndex = options.indexOf(input);
        int computerIndex = randomNumGenerator(options.size()) - 1;

        String computerChoice = options.get(computerIndex);

        // Determine winning and losing options
        int firstHalf = (options.size() - 1) / 2;
        int secondHalf = (options.size() - 1) - firstHalf;

        int diff;
        boolean userWin = true;

        if (userIndex < computerIndex) {
            // Look forward from userIndex position
            diff = userIndex + firstHalf;
            // The user wins if the computerIndex is within the halfway range AHEAD of the current user position
            userWin = computerIndex <= diff;
        } else if (userIndex > computerIndex) {
            // Look backwards from userIndex position
            diff = userIndex - secondHalf;
            // The user wins if the computerIndex is outside the halfway range BEHIND the current user position
            userWin = computerIndex < diff;
        }

        if (input.equals(computerChoice)) {
            System.out.printf("There is a draw (%s)\n", computerChoice);
            userRating+=50;
        } else if (userWin) {
            System.out.printf("Well done. The computer chose %s and failed\n", computerChoice);
            userRating+=100;
        } else {
            System.out.printf("Sorry, but the computer chose %s\n", computerChoice);
        }
    }

    private void playGame(String input) {
        Gesture computerInput = getComputerInput();
        Outcome gameResult = getGameResult(input.toUpperCase(), computerInput.name());
        printResult(gameResult.name(), computerInput.name().toLowerCase());
        setUserRating(gameResult.getValue());
        System.out.println(userRating);
    }

    private static Outcome getGameResult(String userInput, String computerInput) {
        Gesture userGesture = Gesture.valueOf(userInput);

        if (userInput.equals(computerInput)) {
            return Outcome.DRAW;
        } else if (userGesture.getWinsAgainst().equals(computerInput.toLowerCase())) {
            return Outcome.WIN;
        } else {
            return Outcome.LOSS;
        }
    }

    // TIGHTLY COUPLED
    private String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("!exit") || input.equals("!rating")) return input;
        try {
            if (playOG) {
                Gesture.valueOf(input.toUpperCase());
            } else if (!options.contains(input)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid input\n");
            return null;
        }
        return input;
    }

    private static String getUserOptions() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static Gesture getComputerInput() {
        // Get computer decision
        int randInt = randomNumGenerator(3);
        return computeDecision(randInt);
    }

    private static int randomNumGenerator(int n) {
        Random random = new Random();
        return random.nextInt(n) + 1;
    }

    private static Gesture computeDecision(int n) {
        return switch (n) {
            case 1 -> Gesture.ROCK;
            case 2 -> Gesture.PAPER;
            case 3 -> Gesture.SCISSORS;
            default -> null;
        };
    }

    private static void printResult(String gameResult, String computerInput) {
        if (gameResult.equals(Outcome.WIN.name())) {
            System.out.printf("Well done. The computer chose %s and failed\n", computerInput);
        } else if (gameResult.equals(Outcome.DRAW.name())) {
            System.out.printf("There is a draw (%s)\n", computerInput);
        } else {
            System.out.printf("Sorry, but the computer chose %s\n", computerInput);
        }
    }

    private static Map<String, Integer> readRatingsFromFile(String fileName) {
        // HashMap for storing ratings
        Map<String, Integer> map = new HashMap<>();
        // Get the lines in string format from the text file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s");
                String name = parts[0];
                int rating = Integer.parseInt(parts[1]);
                map.put(name, rating);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return map;
    }

    public static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String input = scanner.nextLine();
        greetUser(input);
        return input;
    }

    private static void greetUser(String name) {
        System.out.printf("Hello, %s\n", name);
    }

    private int getUserRating() {
        return userRating;
    }

    private void setUserRating(int value) {
        userRating += value;
    }
}
