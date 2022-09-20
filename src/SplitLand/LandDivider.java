package SplitLand;

import java.util.Arrays;
import java.util.HashMap;

public class LandDivider implements Runnable {
    public enum Method {
        BRUTE_FORCE, GREEDY_TECHNIQUE, EXACT_APPROACH;
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

    public void setSplitCost(int splitCost) {
        this.splitCost = splitCost;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public float bruteForceSplit(Land land) {
        int solution = 0;
        HashMap<int[][], Integer> possibilities = new HashMap<int[][], Integer>();
        // split horizontally
        splitLand(land, solution, 1, false, possibilities);

        // calculate split cost

        //split vertically
        splitLand(land, solution, 1, true, possibilities);


        // calculate

        return solution;
    }

    public float greedyApproachSplit(Land land) {
        return 0.0f;
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
    private int splitLand(Land land, int solution, int splitIndex, boolean side, HashMap<int[][], Integer> possibilities) {
        System.out.println(splitIndex);
        System.out.println(land.getHeight());
        if ((splitIndex >= land.getWidth() && side) || (splitIndex >= land.getHeight() && !side)) {
            return solution;
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


//        return splitLand(land, solution, splitIndex + 1, true, possibilities)
        return splitLand(land, solution, splitIndex + 1, side, possibilities);
//        return solution;
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
        LandDivider landDivider = new LandDivider(6, 6);
        landDivider.setSplitCost(50);
        landDivider.setMethod(Method.BRUTE_FORCE);
        System.out.println(Arrays.deepToString(landDivider.land.getLand()));
        System.out.println("-----------------");
        landDivider.bruteForceSplit(landDivider.getLand());
//        landDivider.splitLand(landDivider.land, 0, 1, true);
    }
}
