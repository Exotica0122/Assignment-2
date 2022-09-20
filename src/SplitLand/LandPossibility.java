package SplitLand;

public class LandPossibility {
    private int index;
    private boolean side;
    private int solution;

    public LandPossibility(int index, boolean side, int solution) {
        this.index = index;
        this.side = side;
        this.solution = solution;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean getSide() {
        return this.side;
    }

    public int getSolution() {
        return this.solution;
    }

    public String toString() {

        return this.index + " " + this.side + " " + this.solution;
    }
}
