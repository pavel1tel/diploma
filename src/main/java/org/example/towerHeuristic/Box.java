package org.example.towerHeuristic;

public class Box {
    private double depth;
    private double height;
    private double length;

    public Box(double width, double height, double length) {
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
