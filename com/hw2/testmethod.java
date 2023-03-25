package com.hw2;

import java.util.Arrays;
import java.util.Random;

public class testmethod {
    private static final int[][] GENERATOR_MATRIX = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 1, 1, 1},
            {0, 0, 1, 1, 0, 0, 1, 1},
            {0, 1, 0, 1, 0, 1, 0, 1},
    };
    public static void main(String[] args) {
        String input = "00001111";
        String encodedString = encode(input);
        System.out.println("Encoded string: " + encodedString);
    }
    private static String encode(String input) {
        StringBuilder encodedStringBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i += 4) {
            String block = input.substring(i, i + 4);
            String encodedBlock = encodeBlock(block);
            encodedStringBuilder.append(encodedBlock);
        }

        return encodedStringBuilder.toString();
    }

    private static String encodeBlock(String block) {
        int[] generatorMatrix = {
                0b11110000,
                0b11001100,
                0b10101010,
                0b01100110,
        };

        int encodedBlock = 0;
        for (int i = 0; i < block.length(); i++) {
            if (block.charAt(i) == '1') {
                encodedBlock ^= generatorMatrix[i];
            }
        }
        return String.format("%8s", Integer.toBinaryString(encodedBlock)).replace(' ', '0');
    }
}