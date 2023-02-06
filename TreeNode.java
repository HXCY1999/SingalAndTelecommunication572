package HW1;

public class TreeNode {
    String prefix;
    int destinationNumber;
    int length;
    TreeNode left;
    TreeNode right;

    public TreeNode(String prefix, int destinationNumber) {
        this.prefix = prefix;
        this.destinationNumber = destinationNumber;
        this.length = prefix.length();
    }
}
