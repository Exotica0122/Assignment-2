package LandDivider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
/**
 *
 * @author toetu
 */
public final class Land {

    private final int length;
    private final int width;
    private final int area;
    private double[][] land;
    
    final double price = 100;//Price per 1unit^2 land
    private double value;
    private final int label;

    /**
     * 
     * @param length length of the land
     * @param width width of the land 
     * @param label for labeling purposes that aid in the drawing of the land.
     */
    public Land(int length, int width, int label) {
        this.length = length;
        this.width = width;
        this.land = new double[this.length][this.width];
        this.area = length * width;
        this.label = label;
        evaluateLand();
        addLabels();
    }

    private void evaluateLand() {
        this.value = area * price;
//        if (area%2 == 0) {
//            this.value = area * price;
//        }else{
//            this.value = area * 2*price;
//        }
    }

    public double getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }

    public int getArea() {
        return area;
    }

    public double[][] getLand() {
        return land;
    }

    public void setLand(double[][] land) {
        this.land = land;
    }

    public int getWidth() {
        return width;
    }

    public double getPrice() {
        return price;
    }

    public void addLabels() {
        for (double[] i : land) {
            for (int j = 0; j < i.length; j++) {
                i[j] = label;
            }
        }
    }
//Drawing purposes--------------------------------------------------------------

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int[] xIntervals = horizontalDivider();
        int[] yIntervals = verticalDivider();
        g2.setStroke(new BasicStroke(5));
        g2.drawRect(50, 50, 400, 400);
        g2.setColor(Color.GREEN);
        g2.fillRect(50, 50, 400, 400);
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < xIntervals.length; i++) {
            for (double[] j : land) {
                for (int k = 0; k < land[0].length - 1; k++) {
                    if (j[k] != j[k + 1]) {
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(50 + xIntervals[k], 50, 50 + xIntervals[k], 450);
                        g2.setStroke(new BasicStroke(1));
                        g2.setColor(Color.RED);
                    }
                }
            }
            g2.setColor(Color.GRAY);
            g2.drawLine(50 + xIntervals[i], 50, 50 + xIntervals[i], 450);
        }
        for (int i = 0; i < yIntervals.length; i++) {
            g2.setColor(Color.RED);
            for (int j = 0; j < land[0].length; j++) {
                for (int k = 0; k < land.length - 1; k++) {
                    if (land[k][j] != land[k + 1][j]) {
                        g2.setStroke(new BasicStroke(10));
                        g2.drawLine(50, 50 + yIntervals[k - 1], 450, 50 + yIntervals[k - 1]);
              //          g2.fillRect(50,yIntervals[k], 400, yIntervals[0]);
                        g2.setStroke(new BasicStroke(1));
                    }
                }
            }
            g2.setColor(Color.GRAY);
            g2.drawLine(50, 50 + yIntervals[i], 450, 50 + yIntervals[i]);
        }
    }

    public int[] horizontalDivider() {
        int interval = 400 / width;
        int[] intervals = new int[width + 1];
        intervals[0] = 0;
        for (int i = 1; i < intervals.length; i++) {
            intervals[i] = intervals[i - 1] + interval;
        }

        return intervals;
    }

    public int[] verticalDivider() {
        int interval = 400 / length;
        int[] intervals = new int[length + 1];
        intervals[0] = interval;
        for (int i = 1; i < intervals.length; i++) {
            intervals[i] = intervals[i - 1] + interval;
        }

        return intervals;
    }

    public static void main(String args[]) {
        Land land = new Land(5, 5, 1);
        for (int i = 0; i < land.getWidth(); i++) {
            System.out.println(land.verticalDivider()[i]);
        }

        for (int j = 0; j < land.getLength(); j++) {
            System.out.println(land.verticalDivider()[j]);
        }
    }
}
