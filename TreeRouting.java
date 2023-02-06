package HW1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TreeRouting {
    public static void main(String[] args) throws IOException {

        BinaryTree tree = new BinaryTree();

        readFile(tree);

        findDestination(tree);

    }

    public static BinaryTree readFile(BinaryTree tree) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/HW1/testdoc.txt"));

        String line;
        while((line = bufferedReader.readLine()) != null){
            //separate the prefix, length and destinationAddress
            String[] substring = line.split("\\s+");
            String prefix = substring[0];
            //convert string to int
            int destinationNumber;
            destinationNumber = Integer.parseInt(substring[1]);
            tree.insert(prefix, destinationNumber);
        }
        return tree;
    }

    private static void findDestination(BinaryTree tree) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("src/HW1/input.txt"));

        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] substring = line.split("\\s+");
            String prefix = substring[0];
            int routingDestination = tree.findRoutingDestination(prefix);
            System.out.println(prefix.length());
            System.out.println("Routing Destination of " + prefix + " is: " +routingDestination);
        }

    }
}
