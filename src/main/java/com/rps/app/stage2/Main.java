package com.rps.app.stage2;
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
        String userInput = getUserInput();
        Gesture computerInput = getComputerInput();
        Outcome gameResult = getGameResult(userInput.toUpperCase(), computerInput.name());
        printResult(gameResult.name(), computerInput.name().toLowerCase());
    }

    public static Outcome getGameResult(String userInput, String computerInput) {
        Gesture userGesture = Gesture.valueOf(userInput);
        Gesture computerGesture = Gesture.valueOf(computerInput);

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
        return scanner.nextLine();
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
            System.out.printf("Well done. The computer chose %s and failed", computerInput);
        } else if (gameResult.equals(Outcome.DRAW.name())) {
            System.out.printf("There is a draw (%s)", computerInput);
        } else {
            System.out.printf("Sorry, but the computer chose %s", computerInput);
        }
    }
}
