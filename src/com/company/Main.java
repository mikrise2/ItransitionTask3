package com.company;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        List<String> moves = Arrays.asList(args);

        if (!checkListOnCorrectly(moves))
            System.exit(0);
        //get random move for PC
        String pcMove = moves.get(new SecureRandom().nextInt(moves.size()));

        //generating HMAC key
        Random secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);

        //generating HMAC
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA3-256");
        Mac mac = Mac.getInstance("HmacSHA3-256");
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal(pcMove.getBytes());
        //print HMAC
        System.out.println("HMAC:\n" + bytesToString(bytes) + "\n");

        //choose move
        System.out.println("Available moves:");
        for (int i = 0; i < moves.size(); i++) {
            System.out.println((i + 1) + " - " + moves.get(i));
        }
        System.out.println("0 - exit");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your move: ");
        int move = scanner.nextInt();
        if (move == 0)
            System.exit(0);
        System.out.println("Your move: " + moves.get(move - 1));
        System.out.println("Computer move: " + pcMove);

        //game logic
        int f = ((move - 1) + moves.size() / 2) / moves.size();
        int pcMoveIndex = moves.indexOf(pcMove);
        if (move - 1 == pcMoveIndex)
            System.out.println("Draw!");
        else if ((f == 1 && ((move - 1) < pcMoveIndex || ((move - 1) + moves.size() / 2) % moves.size() >= pcMoveIndex)) ||
                (f == 0 && (move - 1) < pcMoveIndex && ((move - 1) + moves.size() / 2) % moves.size() >= pcMoveIndex)) {
            System.out.println("You win!");
        } else {
            System.out.println("Computer wins!");
        }
        System.out.println();

        //print HMAC key
        System.out.println("HMAC key:\n" + bytesToString(key));


    }

    static StringBuilder bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb;
    }

    static boolean checkListOnCorrectly(List<String> list) {
        if (list.size() < 3) {
            System.out.println("size of values must be more than 2 \nFor example : java -jar task3.jar rock paper scissors lizard Spock");
            return false;
        }
        if (list.size() % 2 == 0) {
            System.out.println("the number of values must be odd \nFor example : java -jar task3.jar rock paper scissors lizard Spock");
            return false;
        }
        if (Set.copyOf(list).size() != list.size()) {
            System.out.println("values must be unique \nFor example : java -jar task3.jar rock paper scissors lizard Spock");
            return false;
        }
        return true;
    }

}
