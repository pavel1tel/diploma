package org.example.towerHeuristic;

public class Box {
    private int depth;

    private int orD = 0;
    private int height;

    private int orH = 1;
    private int length;

    private int orL = 0;

    private int orientation;

    public Box(int length, int width, int height) {
        this.depth = width;
        this.height = height;
        this.length = length;
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
}
