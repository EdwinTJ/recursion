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
            return treeName + "\n" + toString(root, "", true);
    }

    /**
     * Helper method to build the string tree
     * @param node BinaryNode<E>
     * @param indent String Current Level
     * @param isRight   Indicate if the node is a right child
     * @return String representation of tree
     */
    private String toString(BinaryNode<E> node, String indent, boolean isRight) {
        // Base case: If the current node is null, return an empty string
        if (node == null) {
            return "";
        }

        // StringBuilder to build the resulting string
        StringBuilder result = new StringBuilder();

        // Recursively build the string for the right subtree
        String rightSubtree;
        if (isRight) {
            rightSubtree = toString(node.right, indent + "        ", true);
        } else {
            rightSubtree = toString(node.right, indent + " |      ", true);
        }        result.append(rightSubtree);  // Print right

        result.append(indent);

        // Append a representation of the link between the current node and its parent
        if (isRight) {
            result.append(" / ");
        } else {
            result.append(" \\ ");
        }

        // Append the value of the current node and a newline character
        result.append(node.element + "\n");  // Print current node

        // Recursively build the string for the left subtree
        String leftSubtree;
        if (isRight) {
            leftSubtree = toString(node.left, indent + " |      ", false);
        } else {
            leftSubtree = toString(node.left, indent + "        ", false);
        }        result.append(leftSubtree);  // Print left

        // Return the final string representation of the subtree
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
     * This routine runs in O(n)
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
     * The complexity of finding the deepest node is O(n)
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
            // If both not null, return higher level
            return ( left.element.compareTo(right.element) >= 0 ) ? left : right;
        }
    }


    /**
     * The complexity of finding the flip is O(n)
     * reverse left and right children recursively
     */
    public void flip() {
        flip(root);
    }

    /**
     * Helper method to recursively reverse the left and right children of a subtree rooted at the given node.
     *
     * @param node The root of the current subtree being flipped.
     */
    private void flip(BinaryNode<E> node){
        if (node == null) return;

        BinaryNode<E> temp = node.left;
        // Swap left and right childrens
        node.left = node.right;
        node.right = temp;

        // Recursively flip left and right subtree
        flip(node.left);
        flip(node.right);
    }


    /**
     * Counts number of nodes in specified level
     * The complexity of nodesInLevel is O(2^n)
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
     * @return Count - of number of nodes at specified level
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
     * The complexity of printAllPaths is O(2^n)
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
     *  The complexity of countBST is O(n)
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        return countBST(root).count;
    }

    /**
     * Helper method for countBST.
     *
     * @param current The current node
     * @return CountBSTResult information of binary search trees in the subtree.
     */
    private CountBSTResult countBST(BinaryNode<E> current) {
        if (current == null) {
            return new CountBSTResult(0, null, null, true);
        }

        // Process the left and right subtrees
        CountBSTResult leftResult = countBST(current.left);
        CountBSTResult rightResult = countBST(current.right);

        if (leftResult.isBST && rightResult.isBST &&
                (leftResult.maxValue == null || leftResult.maxValue.compareTo(current.element) < 0) &&
                (rightResult.minValue == null || rightResult.minValue.compareTo(current.element) > 0)) {
            // If the current subtree is a BST, increment the count
            int totalCount = 1 + leftResult.count + rightResult.count;
            E minValue = (leftResult.minValue == null) ? current.element : leftResult.minValue;
            E maxValue = (rightResult.maxValue == null) ? current.element : rightResult.maxValue;
            return new CountBSTResult(totalCount, minValue, maxValue, true);
        } else {
            // If the current subtree is not a BST, return a result indicating that
            return new CountBSTResult(leftResult.count + rightResult.count, null, null, false);
        }
    }

    /**
     * Helper class for countBST
     * Result class to store information about the binary search trees in a subtree.
     */
    private class CountBSTResult {
        int count;      // Number of BSTs in the subtree
        E minValue;     // Minimum value in the subtree
        E maxValue;     // Maximum value in the subtree
        boolean isBST;  // Whether the subtree is a BST

        CountBSTResult(int count, E minValue, E maxValue, boolean isBST) {
            this.count = count;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.isBST = isBST;
        }
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

        // Insert x into the left or right subtree
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
        // search in the left or right
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
        root = pruneK(root, sum);
    }

    private BinaryNode<E> pruneK(BinaryNode<E> current, Integer sum) {
        if (current == null) {
            return null;
        }

        // Calculate the sum for the current node
        Integer currentSum = (Integer) current.element;

        // Recursively prune the left and right subtrees
        current.left = pruneK(current.left, sum - currentSum);
        current.right = pruneK(current.right, sum - currentSum);

        // If the current node is a leaf and the sum is less than or equal to 0, prune it
        if (current.left == null && current.right == null && sum > 0) {
            return null;
        }

        return current;
    }

    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        if (inOrder.length != preOrder.length) {
            throw new IllegalArgumentException("Input arrays must have the same length");
        }

        root = buildTreeTraversals(inOrder, preOrder, 0, inOrder.length - 1, 0);
    }

    /**
     * Helper method to build a Binary Tree from inOrder and preOrder traversals.
     *
     * @param inOrder  List of tree nodes in inorder traversal.
     * @param preOrder List of tree nodes in preorder traversal.
     * @param inStart  Start index of the current subtree in inOrder traversal.
     * @param inEnd    End index of the current subtree in inOrder traversal.
     * @param preIndex Current index in preOrder traversal.
     * @return Root node of the constructed subtree.
     */
    private BinaryNode<E> buildTreeTraversals(E[] inOrder, E[] preOrder, int inStart, int inEnd, int preIndex) {
        if (inStart > inEnd || preIndex >= preOrder.length) {
            return null;
        }

        BinaryNode<E> node = new BinaryNode<>(preOrder[preIndex]);

        int inIndex = findIndex(inOrder, inStart, inEnd, node.element);

        node.left = buildTreeTraversals(inOrder, preOrder, inStart, inIndex - 1, preIndex + 1);
        node.right = buildTreeTraversals(inOrder, preOrder, inIndex + 1, inEnd, preIndex + inIndex - inStart + 1);

        return node;
    }

    /**
     * Helper method for buildTreeTraversals
     *
     * @param arr   The array to search in.
     * @param start Starting index for the search.
     * @param end   Ending index for the search.
     * @param value The value to find in the array.
     * @return Index value if found, otherwise -1.
     */
    private int findIndex(E[] arr, int start, int end, E value) {
        for (int i = start; i <= end; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }


    public Integer sumAll(){
        BinaryNode<Integer> r = (BinaryNode<Integer>) root;
        return sumAll(r);
    }
    public Integer sumAll(BinaryNode<Integer> t){
        // Add return so I can run test
        return null;
    }

    public BinaryNode<E> lca(E a, E b) {
        return lca(root, a, b);
    }

    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public BinaryNode<E> lca(BinaryNode<E> current,E a, E b) {
        if (current == null) {
            return null;
        }

        // Compare the values of the current node with the given nodes a and b
        int compareA = a.compareTo(current.element);
        int compareB = b.compareTo(current.element);

        if (compareA < 0 && compareB < 0) {
            // a and b are in the left subtree
            return lca(current.left, a, b);
        } else if (compareA > 0 && compareB > 0) {
            //a and b are in the right subtree
            return lca(current.right, a, b);
        } else {
            // Current node is the LCA, or a,b is equal to the current node
            return current;
        }
    }



    /**
     * Balance the tree
     */
    public void balanceTree() {
        // Get the sorted elements of the tree using in-order traversal
        List<E> sortedElements = new ArrayList<>();
        inOrderTraversal(root, sortedElements);

        // Build a balanced BST from the sorted elements
        root = buildBalancedBST(sortedElements, 0, sortedElements.size() - 1);
    }

    /**
     * Helper method for balanceTree
     * @param node current node traversal.
     * @param elements the list to store the sorted elements.
     */
    private void inOrderTraversal(BinaryNode<E> node, List<E> elements) {
        if (node != null) {
            inOrderTraversal(node.left, elements);
            elements.add(node.element);
            inOrderTraversal(node.right, elements);
        }
    }

    /**
     * Helper method for balanceTree
     * @param elements the list of sorted elements.
     * @param start the start of the list.
     * @param end the end of the list.
     * @return the root of the balanced BST.
     */
    private BinaryNode<E> buildBalancedBST(List<E> elements, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        BinaryNode<E> newNode = new BinaryNode<>(elements.get(mid));

        newNode.left = buildBalancedBST(elements, start, mid - 1);
        newNode.right = buildBalancedBST(elements, mid + 1, end);

        return newNode;
    }


    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        root = keepRange(root, a, b);

    }

    /**
     * Helper method for keepRange
     *
     * @param current current node traversal.
     * @param a       lowest value
     * @param b       highest value
     * @return tree  only nodes between the range [a, b]
     */
    private BinaryNode<E> keepRange(BinaryNode<E> current, E a, E b) {
        if (current == null) {
            return null;
        }

        if (current.element.compareTo(a) < 0) {
            return keepRange(current.right, a, b);
        }
        else if (current.element.compareTo(b) > 0) {
            return keepRange(current.left, a, b);
        }
        // If current is in range
        else {
            current.left = keepRange(current.left, a, b);
            current.right = keepRange(current.right, a, b);
            return current;
        }
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
