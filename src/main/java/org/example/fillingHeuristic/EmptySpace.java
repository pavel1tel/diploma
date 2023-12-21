package org.example.fillingHeuristic;

public class EmptySpace {
    double xcord;
    double ycord;
    double length;
    double depth;

    public EmptySpace(double xcord, double ycord, double length, double depth) {
        this.xcord = xcord;
        this.ycord = ycord;
        this.length = length;
        this.depth = depth;
    }

    public double getXcord() {
        return xcord;
    }

    public void setXcord(double xcord) {
        this.xcord = xcord;
    }

    public double getYcord() {
        return ycord;
    }

    public void setYcord(double ycord) {
        this.ycord = ycord;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
