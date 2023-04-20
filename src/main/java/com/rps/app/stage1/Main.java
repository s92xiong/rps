package com.rps.app.stage1;
import java.util.Random;
import java.util.Scanner;

enum Gesture {
    ROCK("SCISSORS", "PAPER"),
    PAPER("ROCK", "SCISSORS"),
    SCISSORS("PAPER", "ROCK");

    private final String winsAgainst;
    private final String losesTo;
    Gesture(String winsAgainst, String losesTo) {
        this.winsAgainst = winsAgainst;
        this.losesTo = losesTo;
    }

    public String getWinsAgainst() {
        return winsAgainst;
    }

    public String getLosesTo() {
        return losesTo;
    }
}

enum Outcome { WIN, LOSS, DRAW }

public class Main {
    public static void main(String[] args) {
        boolean runProgram = true;
        while (runProgram) {
            String input = getInput();
            String computedResult = computeDecision(input);
            printResult(computedResult);
        }
    }

    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String computeDecision(String s) {
        String result;
        switch (s) {
            case "rock":
                result = Gesture.ROCK.getLosesTo();
                break;
            case "scissors":
                result = Gesture.SCISSORS.getLosesTo();
                break;
            case "paper":
                result = Gesture.PAPER.getLosesTo();
                break;
            default:
                result = "";
        }
        return result.toLowerCase();
    }

    public static void printResult(String s) {
        System.out.printf("Sorry, but the computer chose %s", s);
    }
}
