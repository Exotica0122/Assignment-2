package LandDivider;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author toetu
 */
public final class LandManager implements Runnable {
    public enum Method{
        BRUTE_FORCE, DYNAMIC_PROGRAMMING, GREEDY_TECHNIQUUE;
    }
    private final Land land;
    private final Method method;
    final double splitCost = 50;//Cost for every subdivision... Split is directly proportional to the length of the subdivision
    private double solution;
    private boolean running;
    private int splits = 0;

    public LandManager(int length, int width, Method method) {
        land = new Land(length, width, 0);
        this.method = method;
        System.out.println("Original land value: $" + land.getValue());
 //       getExactBestSubdivision(land);
    }

    /**
     * BRUTE FORCE APPROACH
     *
     * @param land Land to be subdivided
     * @return maximum value of the best subdivision of input variable land
     */
    public double getBestSubdivision(Land land) {
        HashMap<Boolean, List<Integer>> possibilities = checkPossibleSplits(land.getLand(), false);
        List<Land> landMasses = new ArrayList<>();
        double landValue = land.getValue();
        double horizontalVal = 0, verticalVal = 0;

        //Horizontal splits
        if (possibilities.containsKey(false)) {
            //Do horizontal splits
            for (Integer i : possibilities.get(false)) {
                landMasses = subdivide(land, i, false);
                horizontalVal = landMasses.get(0).getValue() + landMasses.get(1).getValue() - splitCost * land.getWidth();

                //Check if one of the land masses is splittable
                //Land Mass 1
                if (landMasses.get(0).getLength() > 1 || landMasses.get(0).getWidth() > 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getBestSubdivision(landMasses.get(0));

                    if (newVal > horizontalVal) {
                        horizontalVal = newVal;
                    }
                }
                //Land Mass 2
                if (landMasses.get(1).getLength() > 1 || landMasses.get(1).getLength() > 1) {
                    double newVal = getBestSubdivision(landMasses.get(1));
                    if (newVal > horizontalVal) {
                        horizontalVal = newVal;
                    }
                }

            }
        }

        //Vertical splits
        if (possibilities.containsKey(true)) {
            //Do vertical splits
            for (Integer i : possibilities.get(true)) {
                landMasses = subdivide(land, i, true);
                verticalVal = landMasses.get(0).getValue() + landMasses.get(1).getValue() - splitCost * land.getLength();
                //Check if one of the land masses is splittable 
                //Land mass 1
                if (landMasses.get(0).getArea() != 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getBestSubdivision(landMasses.get(0));

                    if (newVal > verticalVal) {
                        verticalVal = newVal;
                    }

                }
                //Land mass 2
                if (landMasses.get(1).getArea() != 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getBestSubdivision(landMasses.get(1));

                    if (newVal > verticalVal) {
                        verticalVal = newVal;
                    }
                }
            }
        }

        //Compare the two splits and get the highest one
        if (horizontalVal > verticalVal) {
            landValue = horizontalVal;

            if (landMasses.size() == 2) {//For Drawing purposes
                this.land.setLand(combineArrays(landMasses.get(0).getLand(), landMasses.get(1).getLand(), true));
            }
        } else {
            landValue = verticalVal;

            if (landMasses.size() == 2) {//For Drawing purposses
                this.land.setLand(combineArrays(landMasses.get(0).getLand(), landMasses.get(1).getLand(), false));
            }
        }

        System.out.println(possibilities);

        splits++;
        return landValue;
    }

    /**
     * DYNAMIC PROGRAMMING APPROACH
     *
     * @param land Land to be subdivided
     * @return value of the best subdivision.
     */
    public double getExactBestSubdivision(Land land) {
        //Check possibilities and consider the symmetric property
        HashMap<Boolean, List<Integer>> possibilities = checkPossibleSplits(land.getLand(), true);
        List<Land> landMasses = new ArrayList<>();
        double landValue = land.getValue();
        double horizontalVal = 0, verticalVal = 0;

        //Horizontal splits
        if (possibilities.containsKey(false)) {
            //Do horizontal splits
            for (Integer i : possibilities.get(false)) {
                landMasses = subdivide(land, i, false);
                horizontalVal = landMasses.get(0).getValue() + landMasses.get(1).getValue() - splitCost * land.getWidth();

                //Check if one of the land masses is splittable
                //Land Mass 1
                if (landMasses.get(0).getLength() > 1 || landMasses.get(0).getWidth() > 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getExactBestSubdivision(landMasses.get(0));

                    if (newVal > horizontalVal) {
                        horizontalVal = newVal;
                    }
                }
                //Land Mass 2
                if (landMasses.get(1).getLength() > 1 || landMasses.get(1).getLength() > 1) {
                    double newVal = getExactBestSubdivision(landMasses.get(1));
                    if (newVal > horizontalVal) {
                        horizontalVal = newVal;
                    }
                }

            }
        }

        //Vertical splits
        if (possibilities.containsKey(true)) {
            //Do vertical splits
            for (Integer i : possibilities.get(true)) {
                landMasses = subdivide(land, i, true);
                verticalVal = landMasses.get(0).getValue() + landMasses.get(1).getValue() - splitCost * land.getLength();
                //Check if one of the land masses is splittable 
                //Land mass 1
                if (landMasses.get(0).getArea() != 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getExactBestSubdivision(landMasses.get(0));

                    if (newVal > verticalVal) {
                        verticalVal = newVal;
                    }

                }
                //Land mass 2
                if (landMasses.get(1).getArea() != 1) {
                    //If so then recursively subdivide the land mass
                    double newVal = getExactBestSubdivision(landMasses.get(1));

                    if (newVal > verticalVal) {
                        verticalVal = newVal;
                    }
                }
            }
        }

        //Compare the two splits and get the highest one
        if (horizontalVal > verticalVal) {
            landValue = horizontalVal;

            if (landMasses.size() == 2) {//For Drawing purposes
                this.land.setLand(combineArrays(landMasses.get(0).getLand(), landMasses.get(1).getLand(), true));
            }
        } else {
            landValue = verticalVal;

            if (landMasses.size() == 2) {//For Drawing purposses
                this.land.setLand(combineArrays(landMasses.get(0).getLand(), landMasses.get(1).getLand(), false));
            }
        }

        splits++;
        return landValue;
    }

    /**
     * GREEDY APPROACH
     *
     * @param land Land to be subdivided
     * @return value of the best subdivision that is chosen using the greedy
     * approach.
     */
    public double getGreedySubdivision(Land land) {
        return 0.0;
    }

    public int getSplits() {
        return splits;
    }
    
    public Land getLand() {
        return land;
    }

    /**
     * @param land array to be split
     * @param splitIndex Index of the array where the splitting occurs
     * @param vertical a Boolean variable that determines whether to split
     * vertically or horizontally. false, cut horizontally and true is to cut
     * vertically
     * @return returns a list containing the two land masses extracted from
     * land.
     */
    public List<Land> subdivide(Land land, int splitIndex, boolean vertical) {
        Land land1;
        Land land2;
        List<Land> landMasses = new ArrayList<>();

        //size of array 1 + size of array 2 equals the size of land
        if (vertical) {
            if (splitIndex == 0) {
                land1 = new Land(land.getLength(), splitIndex + 1, 1);
                land2 = new Land(land.getLength(), land.getWidth() - 1, 2);
            } else {
                land1 = new Land(land.getLength(), splitIndex + 1, 1);
                land2 = new Land(land.getLength(), land.getWidth() - splitIndex - 1, 2);
            }

        } else {
            if (splitIndex == 0) {
                land1 = new Land(splitIndex + 1, land.getWidth(), 1);
                land2 = new Land(land.getLength() - 1, land.getWidth(), 2);
            } else {
                land1 = new Land(splitIndex + 1, land.getWidth(), 1);
                land2 = new Land(land.getLength() - splitIndex - 1, land.getWidth(), 2);
            }
        }
        landMasses.add(land1);
        landMasses.add(land2);

        return landMasses;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void drawLand(Graphics g) {
        land.draw(g);
    }

    public double[][] combineArrays(double[][] array1, double[][] array2, boolean vertical) {
        double[][] combined;
        if (vertical) {
            //Array1 and Array 2 should have equal columns
            combined = new double[array1.length + array2.length][array1[0].length];
            for (int i = 0; i < array1.length; i++) {
                System.arraycopy(array1[0], 0, combined[i], 0, array1[0].length);
            }
            for (int i = array1.length; i < combined.length; i++) {
                System.arraycopy(array2[0], 0, combined[i], 0, array2[0].length);
            }
        } else {
            //Array1 and Array 2 should have equal rows
            combined = new double[array1.length][array1[0].length + array2[0].length];
            for (int i = 0; i < array1.length; i++) {
                System.arraycopy(array1[0], 0, combined[i], 0, array1[0].length);
            }
            for (int i = array1.length; i < combined.length; i++) {
                System.arraycopy(array2[0], 0, combined[i], array1[0].length, combined[0].length);
            }
        }
        return combined;

    }

    /**
     * Check if land can be be subdivided.This takes into consideration the
     * symmetries of the land. Given a rectangular land, there will be (M - 1) +
     * (N - 1) possible subdivisions. Where M is the length and N is the width.
     * Considering the symmetries, the number of splits will be reduced by (M/2
     * - 1) + (N/2 - 1). For a square land, the number of splits is M/2 - 1
     * given that M = N.
     *
     * @param land
     * @param considerSymmetry consider the symmetries of the land array to
     * reduce redundant splits
     * @return HashMap containing a Boolean key that determines whether to cut
     * vertically (TRUE) or horizontally(FALSE). The values of the HashMap is a
     * List of possible cuts.S
     */
    public HashMap<Boolean, List<Integer>> checkPossibleSplits(double[][] land, boolean considerSymmetry) {
        HashMap<Boolean, List<Integer>> possibilities = new HashMap<>();
        List<Integer> indices = null;
        if (considerSymmetry) {
            if (land[0].length > 1) {
                indices = new ArrayList<>();
                for (int i = 0; i < (double) land[0].length / 2; i++) {
                    indices.add(i);
                }
                possibilities.put(Boolean.TRUE, indices);
            }
            if (land.length > 1 && land.length != land[0].length) {
                //Check horizontal splits
                indices = new ArrayList<>();
                for (int i = 0; i < (double) land.length / 2; i++) {
                    indices.add(i);
                }
                possibilities.put(Boolean.FALSE, indices);
            }
            return possibilities;
        }else{
            if (land[0].length > 1) {
                indices = new ArrayList<>();
                for (int i = 0; i < land[0].length - 1; i++) {
                    indices.add(i);
                }
                possibilities.put(Boolean.TRUE, indices);
            }
            if (land.length > 1) {
                indices = new ArrayList<>();
                for (int i = 0; i < land.length - 1; i++) {
                    indices.add(i);
                }
                possibilities.put(Boolean.FALSE, indices);
            }
            return possibilities;
        }
    }

    public static void main(String[] args) {
        LandManager manager = new LandManager(3,3,Method.BRUTE_FORCE);
        System.out.println(manager.getBestSubdivision(manager.getLand()));
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if(null != this.method)
                switch (this.method) {
                case BRUTE_FORCE -> solution = getBestSubdivision(land);
                case DYNAMIC_PROGRAMMING -> solution = getExactBestSubdivision(land);
                case GREEDY_TECHNIQUUE -> solution = getGreedySubdivision(land);
                default -> {
                }
            }
            running = false;
        }
    }

    public double getSolution() {
        return solution;
    }

}
