package SplitLand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LandDivider implements Runnable {
    public enum Method {
        BRUTE_FORCE, GREEDY_TECHNIQUE, EXACT_APPROACH
    }

    private final Land land;
    private Method method;

    private int splitCost;
    private float solution;
    private int splits;
    private boolean running;

    public LandDivider(int width, int height) {
        this.land = new Land(width, height);
        this.splits = 0;
        this.solution = 0;
        this.running = false;
    }

    public int getSplitCost() {
        return this.splitCost;
    }

    public void setSplitCost(int splitCost) {
        this.splitCost = splitCost;
    }

    public int getSplits() {
        return this.splits;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    private void resetSplits() {
        this.splits = 0;
    }


    public ArrayList<LandPossibility> bruteForceSplit(Land land) {
        resetSplits();
        ArrayList<LandPossibility> possibilities = new ArrayList<>();
        // split horizontally
        splitSingleLand(land, 1, false, possibilities);

        // calculate split cost

        //split vertically
        splitSingleLand(land, 1, true, possibilities);

        // calculate split cost

        return possibilities;
    }

    public float greedyApproachSplit(Land land) {
        resetSplits();
        HashMap<ArrayList<Integer>, Integer> memoizedLands = new HashMap<>();

        int solution = Integer.MAX_VALUE;

        int solution1 = splitLandGreedy(land, solution, 1, false, memoizedLands);
        int solution2 = splitLandGreedy(land, solution, 1, true, memoizedLands);

        if (solution > solution1) {
            solution = solution1;
        }
        if (solution > solution2) {
            solution = solution2;
        }

        return solution;
    }

    public float exactApproachSplit(Land land) {
        return 0.0f;
    }

    /**
     * @param {Land} land - Overall land to be split
     * @param {int}  solution - Overall cost of all possible solutions
     * @param {int}  splitIndex - Index at where it splits
     * @param {bool} side - true = vertical, false = horizontal
     */
    private ArrayList<LandPossibility> splitSingleLand(Land land, int splitIndex, boolean side, ArrayList<LandPossibility> possibilities) {
        if ((splitIndex >= land.getWidth() && side) || (splitIndex >= land.getHeight() && !side)) {
            return possibilities;
        }

        Land[] splitLands = new Land[2];

        int splitSolution = 0;

        // When split vertically
        if (side == true) {
            Land leftLand = land.divideVertically(0, splitIndex);
            Land rightLand = land.divideVertically(splitIndex, land.getWidth());
            splitLands[0] = leftLand;
            splitLands[1] = rightLand;

            splitSolution += leftLand.getLand()[leftLand.getWidth() - 1][leftLand.getHeight() - 1];
            splitSolution += rightLand.getLand()[rightLand.getWidth() - 1][rightLand.getHeight() - 1];
            splitSolution += this.splitCost * land.getWidth();
        }
        // When split horizontally
        else {
            Land topLand = land.divideHorizontally(0, splitIndex);
            Land bottomLand = land.divideHorizontally(splitIndex, land.getHeight());
            splitLands[0] = topLand;
            splitLands[1] = bottomLand;

            splitSolution += topLand.getLand()[topLand.getWidth() - 1][topLand.getHeight() - 1];
            splitSolution += bottomLand.getLand()[bottomLand.getWidth() - 1][bottomLand.getHeight() - 1];
            splitSolution += this.splitCost * land.getHeight();
        }

        LandPossibility possibility = new LandPossibility(splitIndex, side, splitSolution);
        possibilities.add(possibility);

        this.splits++;

        System.out.println(Arrays.deepToString(splitLands[0].getLand()));
        System.out.println(Arrays.deepToString(splitLands[1].getLand()));
        System.out.println(possibility);
        System.out.println("-----------------------");

        // Divide by original land
        return splitSingleLand(land, splitIndex + 1, side, possibilities);
    }

    private ArrayList<LandPossibility> splitMultipleLand(Land land, int solution, int splitIndex, boolean side, ArrayList<LandPossibility> possibilities) {
        System.out.println(splitIndex);
//        System.out.println(land.getHeight());
        if ((splitIndex >= land.getWidth() && side) || (splitIndex >= land.getHeight() && !side)) {
            return possibilities;
        }

        Land[] splitLands = new Land[2];

        // When split vertically
        if (side == true) {
            Land leftLand = land.divideVertically(0, splitIndex);
            Land rightLand = land.divideVertically(splitIndex, land.getWidth());
            splitLands[0] = leftLand;
            splitLands[1] = rightLand;
            System.out.println(Arrays.deepToString(leftLand.getLand()));
            System.out.println(Arrays.deepToString(rightLand.getLand()));
            System.out.println("-----------------------");
        }
        // When split horizontally
        else {
            Land topLand = land.divideHorizontally(0, splitIndex);
            Land bottomLand = land.divideHorizontally(splitIndex, land.getHeight());
            splitLands[0] = topLand;
            splitLands[1] = bottomLand;
            System.out.println(Arrays.deepToString(topLand.getLand()));
            System.out.println(Arrays.deepToString(bottomLand.getLand()));
            System.out.println("-----------------------");
        }

        // Divide inner lands
        splitMultipleLand(splitLands[0], solution, splitIndex, true, possibilities);
        splitMultipleLand(splitLands[0], solution, splitIndex, false, possibilities);
        splitMultipleLand(splitLands[1], solution, splitIndex, true, possibilities);
        splitMultipleLand(splitLands[1], solution, splitIndex, false, possibilities);

        this.splits++;

        // Divide by original land
        return splitMultipleLand(land, solution, splitIndex + 1, side, possibilities);
    }

    /**
     * Using Memoization to get current value
     * by solving the previous solution
     * and storing that solution when it is used again next time
     */
    private int splitLandGreedy(Land land, int solution, int splitIndex, boolean side, HashMap<ArrayList<Integer>, Integer> memo) {
        if ((splitIndex >= land.getWidth() && side) || (splitIndex >= land.getHeight() && !side)) {
            return solution;
        }

        ArrayList<Integer> landArea = new ArrayList<>();
        landArea.add(land.getWidth());
        landArea.add(land.getHeight());

        // if dimension of current land exists return from memo
        if (memo.containsKey(landArea)) {
            return memo.get(landArea);
        }

        int splitSolution = 0;

        Land[] splitLands = new Land[2];

        // When split vertically
        if (side == true) {
            Land leftLand = land.divideVertically(0, splitIndex);
            Land rightLand = land.divideVertically(splitIndex, land.getWidth());
            splitLands[0] = leftLand;
            splitLands[1] = rightLand;

            int leftLandCost = leftLand.getLand()[leftLand.getWidth() - 1][leftLand.getHeight() - 1];
            splitSolution += leftLandCost;
            int rightLandCost = rightLand.getLand()[rightLand.getWidth() - 1][rightLand.getHeight() - 1];
            splitSolution += rightLandCost;
            splitSolution += this.splitCost * land.getWidth();
            ArrayList<Integer> leftLandDimensions = new ArrayList<>();
            leftLandDimensions.add(leftLand.getWidth());
            leftLandDimensions.add(leftLand.getHeight());
            ArrayList<Integer> rightLandDimensions = new ArrayList<>();
            rightLandDimensions.add(rightLand.getWidth());
            rightLandDimensions.add(rightLand.getHeight());
            memo.put(leftLandDimensions, leftLandCost);
            memo.put(rightLandDimensions, rightLandCost);
        }
        // When split horizontally
        else {
            Land topLand = land.divideHorizontally(0, splitIndex);
            Land bottomLand = land.divideHorizontally(splitIndex, land.getHeight());
            splitLands[0] = topLand;
            splitLands[1] = bottomLand;

            int topLandCost = topLand.getLand()[topLand.getWidth() - 1][topLand.getHeight() - 1];
            splitSolution += topLandCost;
            int bottomLandCost = bottomLand.getLand()[bottomLand.getWidth() - 1][bottomLand.getHeight() - 1];
            splitSolution += bottomLandCost;
            splitSolution += this.splitCost * land.getHeight();
            ArrayList<Integer> topLandDimensions = new ArrayList<>();
            topLandDimensions.add(topLand.getWidth());
            topLandDimensions.add(topLand.getHeight());
            ArrayList<Integer> bottomLandDimensions = new ArrayList<>();
            bottomLandDimensions.add(bottomLand.getWidth());
            bottomLandDimensions.add(bottomLand.getHeight());
            memo.put(topLandDimensions, topLandCost);
            memo.put(bottomLandDimensions, bottomLandCost);
        }

        this.splits++;

        // evaluate if new solution is better than prev
        if (solution > splitSolution) {
            solution = splitSolution;
        }

        // Divide inner lands
        splitLandGreedy(splitLands[0], solution, splitIndex, true, memo);
        splitLandGreedy(splitLands[0], solution, splitIndex, false, memo);
        splitLandGreedy(splitLands[1], solution, splitIndex, true, memo);
        splitLandGreedy(splitLands[1], solution, splitIndex, false, memo);

        System.out.println(memo);

        // Divide by original land
        return splitLandGreedy(land, solution, splitIndex + 1, side, memo);
    }

    private void runSplitLand(Method method) {
        switch (method) {
            case BRUTE_FORCE:
                bruteForceSplit(this.land);
                break;
            case GREEDY_TECHNIQUE:
                greedyApproachSplit(this.land);
                break;
            case EXACT_APPROACH:
                exactApproachSplit(this.land);
        }
    }

    public Land getLand() {
        return this.land;
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        LandDivider landDivider = new LandDivider(4, 3);
        landDivider.setSplitCost(50);
        landDivider.setMethod(Method.GREEDY_TECHNIQUE);
        System.out.println(Arrays.deepToString(landDivider.land.getLand()));
        System.out.println("-----------------");
//        System.out.println(landDivider.bruteForceSplit(landDivider.getLand()));
        System.out.println(landDivider.greedyApproachSplit(landDivider.getLand()));
        System.out.println("-----------------");
        System.out.println(landDivider.getSplits());
        System.out.println(landDivider.getSplitCost());
        System.out.println("-----------------");
    }
}
