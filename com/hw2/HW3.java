package com.hw2;

import sun.plugin.dom.core.CoreConstants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HW3 {
    public static void main(String[] args) throws IOException {


        /* Step 1
         * In this code,
         * we use a StringBuilder to build the binary string representation of the original string.
         * We loop through each character in the string, convert it to its ASCII code using the (int) cast,
         * and then convert that code to an 8-bit binary string using the Integer.toBinaryString() method.
         * We use String.format() to ensure that each binary string is exactly 8 bits long,
         * and then append it to the StringBuilder.
         *
         * */
        //step1. read file
        StringBuilder binaryString = new StringBuilder();
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("com/hw2/WashUHistory.txt"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            for (char c : line.toCharArray()) {
                //convert each char word to binary code
                String binary = Integer.toBinaryString((int) c);
                //append them, replace the space with 0
                binaryString.append(String.format("%8s", binary).replace(' ', '0'));
                ;
            }
        }
//        System.out.println(binaryString);

        /* Step 2.
         * segment and calculate the 16 bits CRC
         * */
        //divide into multiple segments, each segments contains 200 bits
        List<String> binarySegments = new ArrayList<>();
        for (int start = 0; start < binaryString.length(); start = start + 200) {
            int end = start + 200;
            if (end > binaryString.length()) {
                end = binaryString.length();
            }
            binarySegments.add(binaryString.substring(start, end));
        }
        //calculate the CRC16
        List<String> Frames = new ArrayList<>();
        for (String binarySegment : binarySegments) {
            String frame;
            //calculate the 16 bits CRC
            String crc16 = CRC16.calculateCRC16(binarySegment);
            //append them
            frame = binarySegment + crc16;
            Frames.add(frame);
            //output the length to verify its right
//            System.out.println(frame.length()); //output is 216, last one is 20
        }
//        for(String s : Frames){
//            System.out.println(s);
//        }

        /* Step 3.
         *   repeat the code -> 216 length --> 432 length
         * */

        int[][] GENERATOR_MATRIX = {
                {1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 1, 1, 1, 1},
                {0, 0, 1, 1, 0, 0, 1, 1},
                {0, 1, 0, 1, 0, 1, 0, 1},
        };

        List<String> RMFrames = new ArrayList<>();
        for (String frame : Frames) {
            String ReedMullerCode = null;
            for (int i = 0; i < frame.length(); i++) {
                ReedMullerCode = encode(frame);
            }
            RMFrames.add(ReedMullerCode);
        }
        for (String s : RMFrames) {
//            output the length to verify its length
            System.out.println("RMFrames=" + s.length());// output is 432, and the last one is 40
        }


        /*Step 4.
         *Insert the flag pattern (01111110)before each RMFrames coded frame
         * */
        List<String> flagFrames = new ArrayList<>();
        for (int i = 0; i < RMFrames.size(); i++) {
            //append the flag
            String flagFrame = "01111110" + RMFrames.get(i);
            if (i == RMFrames.size() - 1) {//the last one also add a flag in the tail
                flagFrame += "01111110";
            }
            flagFrames.add(flagFrame);
        }
//        for (String s : flagFrames) {
////                verify the length
////            System.out.println(s.length()); //432+8=440
////            System.out.println(flagFrames.size());
//            System.out.println(s.length());
//        }

        /* Step 5
         *
         * */

        //5.1
        //Firstly randomly change the code, the probability is 10%
        /*
        *   First, we define the input string and the probability of changing each bit.
            Next, we create a StringBuilder to store the output string and a Random object to generate random numbers.
            Then, we loop through each character in the input string and check whether to change it or not based on the probability. If the generated random number is less than or equal to the probability, we change the bit to its opposite (0 to 1 or 1 to 0). Otherwise, we keep the bit unchanged.
            Finally, we print the input and output strings.
        *
        * */
        List<String> errorFrames = new ArrayList<>();
        double probability = 0.1; // 10% probability

        for (String flagFrame : flagFrames) {
            StringBuffer sb = new StringBuffer();
            Random random = new Random();
            for (int i = 0; i < flagFrame.length(); i++) {
                if (random.nextDouble() < probability) {
                    char errorChar = flagFrame.charAt(i) == '0' ? '1' : '0';
                    sb.append(errorChar);
                } else {
                    sb.append(flagFrame.charAt(i));
                }
            }
            errorFrames.add(sb.toString());
        }
//        for(String s : errorFrames){//verify length
//            System.out.println(s.length());
//        }

        /*
         * 5.2
         * Next step is to correct the error bit
         * */
        String flag = "01111110";
        List<String> correctFrames = new ArrayList<>();

        for (String errorFrame : errorFrames) {
            StringBuffer correctFrame = new StringBuffer();
            // append the flag
            correctFrame.append(flag);
            //find the error in each 3 bits
            for (int i = flag.length(); i < errorFrame.length(); i = i + 3) {
                char first = errorFrame.charAt(i);
                char second = errorFrame.charAt(i);
                char third = errorFrame.charAt(i);
                if (first != second && second == third) {
                    correctFrame.append(second).append(second).append(second);
                } else if (first != second && first == third) {
                    correctFrame.append(first).append(first).append(first);
                } else if (first == second && second != third) {
                    correctFrame.append(first).append(first).append(first);
                } else {
                    correctFrame.append(first).append(first).append(first);
                }
            }
            correctFrames.add(correctFrame.toString());
        }
        /*
        * After correct the error
        * we can now decode
        *
        * */

        List<String> finalFrames = new ArrayList<>();

        for(String frame : correctFrames){
            String decode = decode(frame);
            finalFrames.add(decode);
        }
        for (String s : finalFrames){
            System.out.println(s.length());
        }

    }


    public static String encode(String input) {
        StringBuilder encodedStringBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i += 4) {
            String block = input.substring(i, i + 4);
            String encodedBlock = encodeBlock(block);
            encodedStringBuilder.append(encodedBlock);
        }

        return encodedStringBuilder.toString();
    }

    public static String encodeBlock(String block) {
        int[] generatorMatrix = {
                0b11111111,
                0b00001111,
                0b00110011,
                0b01010101,
        };

        int encodedBlock = 0;
        for (int i = 0; i < block.length(); i++) {
            if (block.charAt(i) == '1') {
                encodedBlock ^= generatorMatrix[i];
            }
        }
        return String.format("%8s", Integer.toBinaryString(encodedBlock)).replace(' ', '0');
    }

    public static String decode(String encodedString) {
        StringBuilder decodedStringBuilder = new StringBuilder();

        for (int i = 0; i < encodedString.length(); i += 8) {
            String block = encodedString.substring(i, i + 8);
            String decodedBlock = decodeBlock(block);
            decodedStringBuilder.append(decodedBlock);
        }

        return decodedStringBuilder.toString();
    }

    public static String decodeBlock(String block) {
        int[] parityCheckMatrix = {
                0b10001101,
                0b01001011,
                0b00100111,
                0b00011111
        };

        StringBuilder decodedBlock = new StringBuilder();

        for (int i = 0; i < parityCheckMatrix.length; i++) {
            int count = 0;

            for (int j = 0; j < block.length(); j++) {
                if (block.charAt(j) == '1' && ((parityCheckMatrix[i] >> (7 - j)) & 1) == 1) {
                    count++;
                }
            }

            decodedBlock.append(count % 2);
        }
        return decodedBlock.toString();
    }


}