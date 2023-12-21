package org.example.towerHeuristic;

public class Box {
    private double depth;

    private int orD = 0;
    private double height;

    private int orH = 1;
    private double length;

    private int orL = 0;

    private int orientation;

    public Box( double length, double width, double height) {
        this.depth = width;
        this.height = height;
        this.length = length;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
