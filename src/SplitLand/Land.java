/**
 * @NOTE: the land looking at it visually
 * from code.
 * Left - Right is the height (inner array)
 * Top - Bottom is the width (outer array)
 */

package SplitLand;

import java.util.Arrays;

public class Land {
    private final int width, height;
    private int landArea;
    private int[][] land;
    private int[][] landCost;

    private float totalCost = 0;

    public Land(int width, int height) {
        this.width = width;
        this.height = height;
        this.landArea = width * height;
        this.land = new int[width][height];
        evaluateLandCost();
        addCostsToLand(land);
    }

    public Land divideVertically(int start, int end) {
        int newWidth = end - start;
        Land dividedLand = new Land(newWidth, this.height);
//        int[][] dividedLand = new int[newWidth][this.height];
//        for (int i = start; i < end; i++) {
//            for (int j = 0; j < this.height; j++) {
//                dividedLand[i - start][j] = this.land[i][j];
//            }
//        }
        return dividedLand;
    }

    public Land divideHorizontally(int start, int end) {
        int newHeight = end - start;
        Land dividedLand = new Land(this.width, newHeight);
//        int[][] dividedLand = new int[this.width][newWidth];
//        for (int i = 0; i < this.width; i++) {
//            for (int j = start; j < end; j++) {
//                dividedLand[i][j - start] = this.land[i][j];
//            }
//        }

        return dividedLand;
    }

    public int[][] getLand() {
        return this.land;
    }

    public int[][] getLandCost() {
        return landCost;
    }

    public int getLandArea() {
        return this.landArea;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private void addCostsToLand(int[][] land) {
        int[][] landCost = getLandCost();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                land[i][j] = landCost[i][j];
            }
        }
    }

    private void evaluateLandCost() {
        this.landCost = new int[][]{
                {20, 40, 100, 130, 150, 200},
                {40, 140, 250, 320, 400, 450},
                {100, 250, 350, 420, 450, 500},
                {130, 320, 420, 500, 600, 700},
                {150, 400, 450, 600, 700, 800},
                {200, 450, 500, 700, 800, 900}};
    }


    public static void main(String[] args) {
        Land land = new Land(5, 6);
        int[][] intLand = land.land;

//        Land verticalLand = land.divideVertically(0, 1);
//        Land verticalLand2 = land.divideVertically(1, land.width);
        Land horizontalDivide = land.divideHorizontally(0, 2);
        Land horizontalDivide2 = land.divideHorizontally(2, land.height);

        System.out.println(Arrays.deepToString(intLand));
//        System.out.println(Arrays.deepToString(verticalLand.getLand()));
//        System.out.println(Arrays.deepToString(verticalLand2.getLand()));
        System.out.println(Arrays.deepToString(horizontalDivide.getLand()));
        System.out.println(Arrays.deepToString(horizontalDivide2.getLand()));
    }
}
