package HW1;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HashmapRouting {

    private static HashMap<String, Integer> prefixMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        storePrefix();

        getDestination();

    }

    private static void getDestination() throws IOException {

        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("src/HW1/input.txt"));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] substring = line.split("\\s+");
            String prefix = substring[0];
            Integer destination = prefixMap.get(prefix);
            System.out.println("Routing Destination of " + prefix + " is: " +destination);
        }
    }

    private static void storePrefix() throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("src/HW1/testdoc.txt"));

        String line;
        while((line = bufferedReader.readLine()) != null){
            //separate the prefix, length and destinationAddress
            String[] substring = line.split("\\s+");
            String prefix = substring[0];
            //convert string to int
            int destinationNumber;
            destinationNumber = Integer.parseInt(substring[1]);
            prefixMap.put(prefix, destinationNumber);
        }
    }

}
