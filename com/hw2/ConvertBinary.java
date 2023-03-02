package com.hw2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertBinary {
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
        //read file
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
            }
        }

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
        for(String binarySegment : binarySegments){
            String frame;
            //calculate the 16 bits CRC
            String crc16 = CRC16.calculateCRC16(binarySegment);
            //append them
            frame = binarySegment + crc16;
            Frames.add(frame);
            //output the length to verify its right
//            System.out.println(frame.length()); //output is 216, last one is 20
        }
        /* Step 3.
        *   repeat the code -> 216 length --> 648 length
        * */
        List<String> FECFrames = new ArrayList<>();
        for(String frame : Frames){
            StringBuffer FECFrame = new StringBuffer();
            for(int i = 0; i < frame.length(); i++){
                char c = frame.charAt(i);
                FECFrame.append(c).append(c).append(c);
            }
            FECFrames.add(FECFrame.toString());
        }
//        for (String s : FECFrames){
            //output the length to verify its length
//            System.out.println(s.length());// output is 648, and the last one is 60
//        }
        /*Step 4.
        *Insert the flag pattern (01111110)before each FEC coded frame
        *
        * */
        List<String> flagFrames = new ArrayList<>();
        for(int i = 0; i < FECFrames.size();i++){
            String flagFrame = "01111110" + FECFrames.get(i);
            if(i == FECFrames.size()-1){
                flagFrame += "01111110";
            }
            flagFrames.add(flagFrame);
        }
        for (String s : flagFrames){
                //verify the length
            System.out.println(s.length()); //656 = 648+8; the last one is 76=8+60+8
//            System.out.println(flagFrames.size());
        }
        /*
        * over here we can get the article as binary code
        * 46-frames, each frame contains:
        *   a flag 8 bits (01111110)
        *   + 200 bits repeating 3 time so totally 600 bits
        *   + 16 bits CRC code with repeating so 48bits
        * so totally 656 bits and the last one is different
        * last one: 20->repeating:60->flag:8+60+60
        * */

        /* Step 5
        *
        * */



    }
}

