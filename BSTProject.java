/***************************************************************
* file: BSTProject.java
* author: N.Tran
* class: CS 241 – Data Structure and Algorithms 2
*
* assignment: program 1
* date last modified: 10/16/2017
*
* purpose: build a binary search tree using the input values, 
* print them out in pre, in, and post order.this program also 
* allows the user to inser, delete, find successor and predecessor of a specific value
****************************************************************/ 
import java.util.Scanner;
import java.util.Arrays;

public class BSTProject{
    public static void main(String args[]) {
        System.out.println("Please enter the initial sequence of values:");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] inputArr = input.split(" ");

        // convert user's input of ints to an int array
        int[] initTree = new int[inputArr.length];
        for (int i = 0; i < inputArr.length; i++) {
            try {
                initTree[i] = Integer.parseInt(inputArr[i]);
            } catch (NumberFormatException nfe) {
                System.out.println(nfe);
            }
        }

        // create a BST using the int array
        BST tree = new BST();
        for (int i = 0; i < initTree.length; i++) {
            tree.insert(initTree[i], tree.root, null);
        }

        System.out.println();
        System.out.println("Pre-order: " + tree.preOrder(tree.root, ""));
        System.out.println("In-order: " + tree.inOrder(tree.root, ""));
        System.out.println("Post-order: " + tree.postOrder(tree.root, ""));
        System.out.print("Command? ");

        // while loop to ask for commands
        while (input.charAt(0) != 'E') {
            input = sc.nextLine().toUpperCase();
            System.out.println();

            inputArr = input.split(" ");

            // used a switch-case because it seems intuitive for a given set of
            // commands compared to a long if-else statement
            switch(input.charAt(0)) {
            case 'I' :
                int insert = Integer.parseInt(inputArr[1]);
                if (!tree.contains(insert, tree.root)) {
                    tree.insert(insert, tree.root, null);
                    System.out.println("In-order: " + tree.inOrder(tree.root, ""));
                } else {
                    System.out.println(insert + " already exists, ignore.");
                }
                System.out.print("Command? ");
                break;
            case 'D' :
                int delete = Integer.parseInt(inputArr[1]);
                if (tree.contains(delete, tree.root)) {
                    tree.delete(delete, tree.root);
                    System.out.println("In-order: " + tree.inOrder(tree.root, ""));
                } else {
                    System.out.println(delete + " does not exist!");
                }
                System.out.print("Command? ");
                break;
            case 'P' :
                int predecessor = Integer.parseInt(inputArr[1]);
                if (tree.predecessor(predecessor, tree.root) != null) {
                    System.out.println(tree.predecessor(predecessor, tree.root).getData());
                } else {
                    System.out.println(predecessor + " does not have a predecessor.");
                }
                System.out.print("Command? ");
                break;
            case 'S' :
                int successor = Integer.parseInt(inputArr[1]);
                if (tree.successor(successor, tree.root) != null) {
                    System.out.println(tree.successor(successor, tree.root).getData());
                } else {
                    System.out.println(successor + " does not have a successor.");
                }
                System.out.print("Command? ");
                break;
            case 'H' :
                commandList();
                System.out.print("Command? ");
                break;
            case 'E' :
                System.out.print("Thanks for using my program :)"); 
            
                break;
            default :
                System.out.println("That is an unrecognizable command :/");
                System.out.print("Command? ");
            }       
        }
    }

    // separated the list of commands
    public static void commandList() {
        System.out.println("I Insert a value");
        System.out.println("D Delete a value");
        System.out.println("P Find predecessor");
        System.out.println("S Find successor");
        System.out.println("H Display this message");
        System.out.println("E Exit the program");
    }
}

//BST.java
class BST {
    protected Node root;

    public BST() {
        root = null;
    }

    /**
     * The contains method will check if a given value is within a BST. This is
     * used to prevent inserting duplicate values and deleting non-existent ones
     */
    public boolean contains(int target, Node n) {
        if (n == null) return false;

        if (n.getData() == target) {
            return true;
        } else if (n.getData() > target) {
            return contains(target, n.getLeft());
        } else {
            return contains(target, n.getRight());
        }
    }

    /**
     * This is my implementation of the predecessor. 
     */
    public Node predecessor(int target, Node n) {
        if (root == null) return null;
        if (n == root.getLeftmost()) return null;

        if (n.getData() == target) {
            if (n.getLeft() != null) {
                return n.getLeft().getRightmost();
            } else {
                Node temp = n.parent;

                while (temp != null && n == temp.getLeft()) {
                    n = temp;
                    temp = temp.parent;
                }

                if (temp == null) {
                    return null;
                } else {
                    return temp;
                }
            }
        } else if (n.getData() > target) {
            return predecessor(target, n.getLeft());
        } else {
            return predecessor(target, n.getRight());
        }
    }

    /**
     * This is my implementation of the successor. 
     */
    public Node successor(int target, Node n) {
        if (root == null) return null;
        if (n == root.getRightmost()) return null;

        if (n.getData() == target) {
            if (n.getRight() != null) {
                return n.getRight().getLeftmost();
            } else {
                Node temp = n.parent;
                while (temp != null && n == temp.getRight()) {
                    n = temp;
                    temp = temp.parent;
                }

                if (temp == null) {
                    return null;
                } else {
                    return temp;
                }
            }
        } else if (n.getData() > target) {
            return successor(target, n.getLeft());
        } else {
            return successor(target, n.getRight());
        }
    }

    /**
     * The insert method will insert a new value starting from the root. It will
     * find the right place in the BST to create a new node containing the value.
     * The implementation of this method is recursive.
     */
    public Node insert(int element, Node n, Node p) {
        if (root == null) {
            root = new Node(element, null, null, null);
            return root;
        }

        if (n == null) {
            return new Node(element, null, null, p);
        } else {
            if (element <= n.getData()) {
                //System.out.println("set left");
                n.setLeft(insert(element, n.getLeft(), n));
            } else {
                //System.out.println("set right");
                n.setRight(insert(element, n.getRight(), n));
            }
            return n;
        }
    }

    /**
     * The delete method will delete a node from the BST based on a given value.
     * This method is also recursive.
     */
    public boolean delete(int target, Node n) {

        // case #1: root is empty
        if (n == null) return false;

        if (n.getData() == target) {
            if (n.getLeft() == null) {

                // case #2: target found at root with no left child
                if (n == root) {
                    root = root.getRight();
                    return true;
                }

                // case #3: target found with no left child
                if (n == n.parent.getLeft()) {
                    n.parent.setLeft(n.getRight());
                } else {
                    n.parent.setRight(n.getRight());
                }
                return true;

                // case #4: there's a left child
            } else {
                Node temp = n;
                n.setData(n.getLeft().getRightmost().getData());
                n.setLeft(n.getLeft().removeRightmost());
                return true;
            }
        } else if (n.getData() > target) {
            return delete(target, n.getLeft());
        } else {
            return delete(target, n.getRight());
        }
    }

    /**
     * This is the method that returns a string of the pre-order of the BST.
     */
    public String preOrder(Node n, String s) {
        String print = s;

        // Process the root.
        if (n != null) {
            print += n.getData() + " ";
        }

        // Process the nodes in the left subtree with a recursive call.
        if (n.getLeft() != null) {
            print += preOrder(n.getLeft(), s);
        }
        // Process the nodes in the right subtree with a recursive call.
        if (n.getRight() != null) {
            print += preOrder(n.getRight(), s);
        }

        return print;
    }

    /**
     * This is the method that returns a string of the in-order of the BST.
     */
    public String inOrder(Node n, String s) {
        String print = s;

        // Process the nodes in the left subtree with a recursive call.
        if (n.getLeft() != null) {
            print += inOrder(n.getLeft(), s);
        }

        // Process the root.
        if (n != null) {
            print += n.getData() + " ";
        }

        // Process the nodes in the right subtree with a recursive call.
        if (n.getRight() != null) {
            print += inOrder(n.getRight(), s);
        }

        return print;
    }

    /**
     * This is the method that returns a string of the post-order of the BST.
     */
    public String postOrder(Node n, String s) {
        String print = s;

        // Process the nodes in the left subtree with a recursive call.
        if (n.getLeft() != null) {
            print += postOrder(n.getLeft(), s);
        }

        // Process the nodes in the right subtree with a recursive call.
        if (n.getRight() != null) {
            print += postOrder(n.getRight(), s);
        }

        // Process the root.
        if (n != null) {
            print += n.getData() + " ";
        }

        return print;
    }
}

//Node.java
class Node {
    protected int data;
    protected Node left;
    protected Node right;
    protected Node parent;

    public Node(int initalData, Node initalLeft, Node initalRight, Node initalParent) {
        data = initalData;
        left = initalLeft;
        right = initalRight;
        parent = initalParent;
    }

    public int getData() {
        return data;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setData(int newData) {
        data = newData;
    }

    public void setLeft(Node newLeft) {
        left = newLeft;
    }

    public void setRight(Node newRight) {
        right = newRight;
    }

    public boolean isLeaf() {
        if (left == null && right == null) {
            return true;
        } else {
            return false;
        }
    }

    public Node getLeftmost() {
        if (left != null) {
            return left.getLeftmost();
        } else {
            return this;
        }
    }

    public Node getRightmost() {
        if (right != null) {
            return right.getRightmost();
        } else {
            return this;
        }
    }

    public Node removeLeftmost() {
        if (left == null) {
            return right;
        } else {
            left = left.removeLeftmost();
            return this;
        }
    }

    public Node removeRightmost() {
        if (right == null) {
            return left;
        } else {
            right = right.removeRightmost();
            return this;
        }
    }
}
