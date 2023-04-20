package com.rps.app.stage3;
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

enum Outcome { WIN, LOSS, DRAW }

public class Main {
    public static void main(String[] args) {
        while (true) {
            int result = run();
            if (result == -1) break;
        }
        System.out.println("Bye!");
    }

    public static int run() {
        String userInput = getUserInput();
        if (userInput == null) return 0;
        if (userInput.equals("!exit")) return -1;
        Gesture computerInput = getComputerInput();
        Outcome gameResult = getGameResult(userInput.toUpperCase(), computerInput.name());
        printResult(gameResult.name(), computerInput.name().toLowerCase());
        return 0;
    }

    public static Outcome getGameResult(String userInput, String computerInput) {
        Gesture userGesture = Gesture.valueOf(userInput);

        if (userInput.equals(computerInput)) {
            return Outcome.DRAW;
        } else if (userGesture.getWinsAgainst().equals(computerInput.toLowerCase())) {
            return Outcome.WIN;
        } else {
            return Outcome.LOSS;
        }
    }

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            Gesture.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (input.equals("!exit")) {
                return input;
            } else {
                System.out.println("Invalid input");
                return null;
            }
        }
        return input;
    }

    public static Gesture getComputerInput() {
        // Get computer decision
        int randInt = RandomNumGenerator();
        return computeDecision(randInt);
    }

    public static int RandomNumGenerator() {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    public static Gesture computeDecision(int n) {
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

    public static void printResult(String gameResult, String computerInput) {
        if (gameResult.equals(Outcome.WIN.name())) {
            System.out.printf("Well done. The computer chose %s and failed\n", computerInput);
        } else if (gameResult.equals(Outcome.DRAW.name())) {
            System.out.printf("There is a draw (%s)\n", computerInput);
        } else {
            System.out.printf("Sorry, but the computer chose %s\n", computerInput);
        }

    }
}
