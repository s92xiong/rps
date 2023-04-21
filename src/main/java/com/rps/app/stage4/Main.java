package com.rps.app.stage4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

enum Gesture {
    ROCK("scissors", "paper", "rock"),
    PAPER("rock", "scissors", "paper"),
    SCISSORS("paper", "rock", "scissors");

    private final String winsAgainst;
    private final String losesTo;
    private final String draw;
    Gesture(String winsAgainst, String losesTo, String draw) {
        this.winsAgainst = winsAgainst;
        this.losesTo = losesTo;
        this.draw = draw;
    }

    public String getWinsAgainst() {
        return winsAgainst;
    }

    public String getLosesTo() {
        return losesTo;
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

    private Outcome(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class Main {
    private final Map<String, Integer> ratings;
    private final String username;
    private int userRating;

    private Main(Map<String, Integer> ratings, String username) {
        if (!ratings.containsKey(username)) {
            ratings.put(username, 0);
        }
        this.ratings = ratings;
        this.username = username;
        this.userRating = ratings.get(username);
    }

    public static void main(String[] args) {
        String fileName = "src/main/java/com/rps/app/stage4/rating.txt";
        Main app = new Main(readRatingsFromFile(fileName), getUsername());
        app.greetUser();
        while (true) {
            int result = app.run();
            if (result == -1) break;
        }
        System.out.println("Bye!");
    }

    private int run() {
        String userInput = getUserInput();
        if (userInput == null) {
            return 0;
        } else if (userInput.equals("!exit")) {
            return -1;
        } else if (userInput.equals(Gesture.ROCK.getDraw()) || userInput.equals(Gesture.PAPER.getDraw()) || userInput.equals(Gesture.SCISSORS.getDraw())) {
            playGame(userInput);
        } else if (userInput.equals("!rating")) {
            int rating = getUserRating();
            System.out.printf("Your rating: %d\n", rating);
            return 0;
        }
        return 0;
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

    private static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("!exit") || input.equals("!rating")) return input;
        try {
            Gesture.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid input\n");
            return null;
        }
        return input;
    }

    private static Gesture getComputerInput() {
        // Get computer decision
        int randInt = RandomNumGenerator();
        return computeDecision(randInt);
    }

    private static int RandomNumGenerator() {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    private static Gesture computeDecision(int n) {
        switch (n) {
            case 1:
                return Gesture.ROCK;
            case 2:
                return Gesture.PAPER;
            case 3:
                return Gesture.SCISSORS;
        }
        return null;
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
        return scanner.nextLine();
    }

    private void greetUser() {
        System.out.printf("Hello, %s\n", username);
    }

    private int getUserRating() {
        return userRating;
    }

    private void setUserRating(int value) {
        userRating += value;
    }
}
