/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SplitLand;

/**
 *
 * @author Admin
 */
public class BruteForceBinary {

    private Node root;
    private int subDiv; // subdivision cost.
    private int bestValueWidth = 0;
    private int bestValueHeight = 0;
    private int bestValue = 0;
    private Node plotNode = null;
    public static final int[][] landValues = { // Land values.
            {20, 40, 100, 130, 150, 200},
            {40, 140, 250, 320, 400, 450},
            {100, 250, 350, 420, 450, 500},
            {130, 320, 420, 500, 600, 700},
            {150, 400, 450, 600, 700, 800},
            {200, 450, 500, 700, 800, 900}
    };

    public int[][] plotMap;
    public int id = 1;

    public static void main(String[] args) {
        BruteForceBinary brute = new BruteForceBinary();
        brute.createBinaryTree(3,3, 20);
        System.out.println(brute.plotMap);
        System.out.println(brute.bestValue);
        //  brute.calculateWidthSubDiv(brute.root);
    }

    public Node calculateWidthSubDiv(Node node, int width, int height, int value, int max) {
        if (node != null) {
            //calculating the subdivision width length getting the cost values.
            //for(int i = 0; i < width; i++){
            if (node.width == width && node.height == height) {
                max = node.value;
                System.out.println("NO SUBDIVISION");
                node.printInfo();
                return node;
            }
            //System.out.println(node.width);
            if (node.height == height) {
                if (node.width <= height) {
                    node.printInfo();
                    // System.out.println(landValues[3-1][5-1]);
                    int cost = (node.height * subDiv);
                    System.out.println("fixed values:" + (width - node.width) + " " + height);
                    System.out.println("value: " + (node.value + landValues[(height - 1)][(width - node.width - 1)] - cost));
                    System.out.println("Value: " + " " + node.value + " + " + landValues[(height - 1)][(width - node.width - 1)]+ " + "+cost+" = " +(node.value + landValues[(height - 1)][(width - node.width - 1)] - cost));
                    //calculating bestvalue
                    if ((node.value + landValues[(height - 1)][(width - node.width - 1)] - cost) > bestValueWidth) {
                        value = (node.value + landValues[(height - 1)][(width - node.width - 1)] - cost);
                        System.out.println("BEST VALUE ::" + value);
                        plotNode = node;
                        bestValueWidth = value;
                    }
                }
            }
            Node checkLeft = calculateWidthSubDiv(node.left, width, height, value, max);
            Node checkRight = calculateWidthSubDiv(node.right, width, height, value, max);
        }
        return node;
    }

    // subdivision for height
    public Node calculateHegihtSubDiv(Node node, int width, int height, int value, int max) {
        if (node != null) {
            //calculating the subdivision width length getting the cost values.
            //for(int i = 0; i < width; i++){
            if (node.width == width && node.height == height) {
                max = node.value;
                System.out.println("NO SUBDIVISION");
                node.printInfo();
                return node;
            }
            //System.out.println(node.width);
            if (node.width == width) {
                if (node.height <= height) {
                    System.out.println("fixed Values" + width + " " + (height - node.height));
                    node.printInfo();

                    int cost = (node.width * subDiv);
                    System.out.println("Value: " + " " + node.value + " + " + landValues[width - 1][(height - node.height) - 1] + " + "+cost+" = " +(node.value + landValues[width - 1][(height - node.height) - 1] - cost));


                    if ((node.value + landValues[width - 1][(height - node.height) - 1] - cost) > bestValueHeight) {
                        value = (node.value + landValues[width - 1][(height - node.height) - 1] - cost);
                        System.out.println("BEST VALUE::: " + value);
                        plotNode = node;
                        bestValueHeight = value;
                    }
                }

            }
            Node checkLeft = calculateHegihtSubDiv(node.left, width, height, value, max);
            Node checkRight = calculateHegihtSubDiv(node.right, width, height, value, max);
        }
        return node;
    }

    private BruteForceBinary createBinaryTree(int height, int width, int subDiv) {
        BruteForceBinary bt = new BruteForceBinary();
        bt.subDiv = subDiv;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(landValues[i][j] + " ");
                bt.add(landValues[i][j], (i + 1), (j + 1));

            }
            System.out.println("");
        }
        bt.calculateWidthSubDiv(bt.root, height, width, 0, 0);
        bt.calculateHegihtSubDiv(bt.root, height, width, 0, 0);
        if (Math.max(bt.bestValueHeight, bt.bestValueWidth) < landValues[height-1][width-1]) {
            bestValue = landValues[height-1][width-1];
            System.out.println("BestValue:: " + landValues[height-1][width-1]);
        } else {
            bestValue = Math.max(bt.bestValueHeight,bt.bestValueWidth);
            System.out.println("BestValue:: " + Math.max(bt.bestValueHeight,bt.bestValueWidth));
        }
        plotMap =  new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0;j < height; j++){
                plotMap[i][j] = 0;
            }
        }

        //insert plot map based on maxValue
        System.out.println(bt.plotNode.height);
        System.out.println(bt.plotNode.width);
        for(int m = 0; m < bt.plotNode.width; m++){
            for(int n = 0; n < bt.plotNode.height; n++ ){
                plotMap[n][m] = 1;
            }
        }
        //plot map
        System.out.println("Plot Map:");
        for(int i = 0; i < width; i++){
            for(int j = 0;j <height; j++){
                System.out.print(plotMap[i][j] +" ");
            }
            System.out.println();
        }
        return bt;
    }

    public void add(int value, int width, int height) {
        root = addBruteRecursive(root, value, width, height);
    }

    private Node addBruteRecursive(Node current, int value, int width, int height) {
        if (current == null) {
            Node temp = new Node(value, width, height);
            // temp.printInfo();
            return temp;
        }
        if (value <= current.value) {
            current.left = addBruteRecursive(current.left, value, width, height);
        } else if (value >= current.value) {
            current.right = addBruteRecursive(current.right, value, width, height);
        }
        return current;
    }
}

class Node {

    int value;
    int width;
    int height;
    int count;
    Node left;
    Node right;

    Node(int value, int width, int height) {
        this.value = value;
        this.width = width;
        this.height = height;

        left = null;
        right = null;
    }

    public void printInfo() {
        System.out.println("INFO: value: " + value + "  " + width + " " + height);
    }
}
