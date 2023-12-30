package org.example.towerHeuristic;

public class Box {
    private int depth;

    private int orD;
    private int height;

    private int orH;
    private int length;

    private int orL;

    private double weight;

    public Box(int length, int orL, int width, int orD, int height, int orH, double weight) {
        this.depth = width;
        this.orD = orD;
        this.orL = orL;
        this.orH = orH;
        this.height = height;
        this.length = length;
        this.weight = 1;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getRotations() {
        return 2 * this.orD + 2 * this.orH + 2 * this.orL;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
