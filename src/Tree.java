// ******************ERRORS********************************
// Throws UnderflowException as appropriate
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    private BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create tree from list
     * @param arr   List of elements
     * @param label Name of tree
     * @ordered true if want an ordered tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }


    /**
     * Build a NON BST tree by inorder
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }


    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + "\n" + toString(root, "");
    }
    /**
     * Helper method to build the string tree
     * @param node BinaryNode<E>
     * @param indent string
     */
    private String toString(BinaryNode<E> node, String indent) {
        if (node == null)
            return "";

        // In-order traversal
        StringBuilder result = new StringBuilder();
        result.append(toString(node.right, indent + "  "));  // Print right
        result.append(indent + node.element + "\n");        // Print current node
        result.append(toString(node.left, indent + "  "));   // Print left

        return result.toString();
    }
    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    public String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }


    /**
     * The complexity of finding the deepest node is O(???)
     * @return
     */
    public E deepestNode() {
        BinaryNode<E> deepest = deepestNode(root, 0);
        return (deepest != null) ? deepest.element : null;    }
    /**
     * Helper method for deepestNode
     * @param current current node traversal.
     * @param currentLevel current level of current node.
     * @return deepest node
     */
    private BinaryNode<E> deepestNode(BinaryNode<E> current,int currentLevel){
        if (current == null) {
            return null;
        }
        // Find the deepest node in left and right
        BinaryNode<E> left = deepestNode(current.left, currentLevel + 1);
        BinaryNode<E> right = deepestNode(current.right, currentLevel + 1);
        
        // Compare the level of left and right
        if(left == null && right == null){
            // If both are null, current is the deepest
            return current;
        } else if (left == null) {
            return right;
        } else if(right == null){
            return left;
        }else{
            return ( left.element.compareTo(right.element) >= 0 ) ? left : right;
        }
    }

    /**
     * The complexity of finding the flip is O(???)
     * reverse left and right children recursively
     */
    public void flip() {
        flip(root);
    }
    private void flip(BinaryNode<E>t){
        if (t == null) return;

        BinaryNode<E> temp = t.left;
        // Swap left and right childrens
        t.left = t.right;
        t.right = temp;

        // Recursively flip left and right subtree
        flip(t.left);
        flip(t.right);
    }

    /**
     * Counts number of nodes in specified level
     * The complexity of nodesInLevel is O(???)
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        return nodesInLevel(root, 0, level);
    }
    /**
     * Helper method for nodesInLevel
     * @param current current node traversal.
     * @param currentLevel current level of current node.
     * @param targetLevel target level for counting nodes.
     * @return count of number of nodes at specified level
     */
    private int nodesInLevel(BinaryNode<E> current, int currentLevel, int targetLevel) {
        if (current == null) {
            return 0;
        }

        if (currentLevel == targetLevel) {
            //  count this node at level
            return 1;
        }
        // Count nodes in the left and right
        int leftCount = nodesInLevel(current.left, currentLevel + 1, targetLevel);
        int rightCount = nodesInLevel(current.right, currentLevel + 1, targetLevel);

        return leftCount + rightCount;
    }


    /**
     * Print all paths from root to leaves
     * The complexity of printAllPaths is O(???)
     */
    public void printAllPaths() {
        printAllPaths(root, new ArrayList<>());

    }
    /**
     * Helper method to printAllPaths
     * @param current the current node in the traversal.
     * @param pathList the list to store the current path.
     */
    private void printAllPaths(BinaryNode<E> current, List<E> pathList) {
        if (current == null) {
            return;
        }

        // Add the current node to the path
        pathList.add(current.element);

        // Check if the current node is a leaf (both left and right children are null)
        if (current.left == null && current.right == null) {
            // Print the path when a leaf is reached
            printPath(pathList);
        } else {
            // Recursively traverse the left and right subtrees
            printAllPaths(current.left, new ArrayList<>(pathList));
            printAllPaths(current.right, new ArrayList<>(pathList));
        }
    }
    /**
     * Helper method to printAllPaths
     * @param pathList the list representing a path from root to leaf.
     */
    private void printPath(List<E> pathList) {
        StringBuilder path = new StringBuilder();
        for (E element : pathList) {
            path.append(element).append(" ");
        }
        System.out.println(path.toString().trim());
    }
    /**
     * Counts all non-null binary search trees embedded in tree
     *  The complexity of countBST is O(???)
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;
        return -1;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * The complexity of bstInsert depends on the tree.  If it is balanced the complexity is O(log n)
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }
    /**
     * Remove all paths from tree that sum to less than given value
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        root = pruneK(root, 0, sum);
    }

    private BinaryNode<E> pruneK(BinaryNode<E> current, int currentSum, Integer targetSum) {
        if (current == null) {
            return null;
        }

        // Update the current sum
        currentSum += ((Integer) current.element);

        // Check if the current node is a leaf (both left and right children are null)
        if (current.left == null && current.right == null) {
            // If the current path sum is less than the target sum, remove the node
            if (currentSum < targetSum) {
                return null;
            } else {
                return current;
            }
        } else {
            // Recursively prune the left and right subtrees
            current.left = pruneK(current.left, currentSum, targetSum);
            current.right = pruneK(current.right, currentSum, targetSum);

            // Return the modified current node
            return current;
        }
    }


    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        root = null;
    }

    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public BinaryNode<E> lca(BinaryNode<E> t,E a, E b) {
    // Add return so I can run test
        return null;
    }
    public Integer sumAll(){
        BinaryNode<Integer> r = (BinaryNode<Integer>) root;
        return sumAll(r);
    }
    public Integer sumAll(BinaryNode<Integer> t){
        // Add return so I can run test
        return null;
    }

    public E lca(E a, E b) {
        // Add return so I can run test
        return null;
    }
    /**
     * Balance the tree
     */
    public void balanceTree() {
        //root = balanceTree(root);
    }

    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
    }

    // Basic node stored in unbalanced binary  trees
    public static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }


}
