
public class BinaryTree {
    TreeNode root;

    public void insert(String prefix, int destinationNumber) {
        root = insert(root, prefix, destinationNumber);
    }

    public TreeNode insert(TreeNode node, String prefix, int destinationNumber) {
        if (node == null) {
            return new TreeNode(prefix, destinationNumber);
        }

        int compare = prefix.compareTo(node.prefix);

        if (compare < 0) {
            node.left = insert(node.left, prefix, destinationNumber);
        } else if (compare > 0) {
            node.right = insert(node.right, prefix, destinationNumber);
        } else {
            node.destinationNumber = destinationNumber;
        }
        return node;
    }

    public int findRoutingDestination(String destinationAddress) {
        TreeNode node = root;
        while (node != null) {
            int compare = destinationAddress.compareTo(node.prefix);
            if (compare == 0) {
                return node.destinationNumber;
            } else if (compare < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return -1;
    }

}
