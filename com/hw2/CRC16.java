package com.hw2;

public class CRC16 {

    private static final int POLYNOMIAL = 0x11021;
    private static final int INITIAL_VALUE = 0xFFFF;
    private static final int FINAL_XOR_VALUE = 0xFFFF;

    public static String calculateCRC16(String data) {
        int crc = INITIAL_VALUE;

        for (int i = 0; i < data.length(); i++) {
            char b = data.charAt(i);
            crc ^= (b & 0xFF) << 8;

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }
        }

        crc ^= FINAL_XOR_VALUE;
        String crcBinary = Integer.toBinaryString(crc);
        //some are not 16-bits so we need replace the blank with 0
        return String.format("%16s", crcBinary).replace(' ', '0');
    }
}
