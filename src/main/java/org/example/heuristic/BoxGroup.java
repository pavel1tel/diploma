package org.example.heuristic;

import java.util.Objects;

public class BoxGroup {
    private double depth;
    private double height;
    private double length;

    private boolean isOpen = false;

    private int remainingQuantity = 1;

    public BoxGroup(BoxGroup boxGroup) {
        this.depth = boxGroup.getDepth();
        this.height = boxGroup.getHeight();
        this.length = boxGroup.getLength();
    }

    public BoxGroup(double depth, double height, double length) {
        this.depth = depth;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public void incrementQuantity() {
        remainingQuantity += 1;
    }

    public void decrementQuantity() {
        remainingQuantity -= 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxGroup boxGroup = (BoxGroup) o;
        return Double.compare(depth, boxGroup.depth) == 0 && Double.compare(height, boxGroup.height) == 0 && Double.compare(length, boxGroup.length) == 0 && isOpen == boxGroup.isOpen && remainingQuantity == boxGroup.remainingQuantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(depth, height, length, isOpen, remainingQuantity);
    }

    @Override
    public String toString() {
        return "BoxGroup{" +
                "width=" + depth +
                ", height=" + height +
                ", length=" + length +
                ", isOpen=" + isOpen +
                ", remainingQuantity=" + remainingQuantity +
                '}';
    }


}
