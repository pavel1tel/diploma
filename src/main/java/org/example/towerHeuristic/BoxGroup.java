package org.example.towerHeuristic;

import java.util.List;
import java.util.Objects;

import static org.example.towerHeuristic.BoxUtil.areBoxesEqual;

public class BoxGroup {
    private int depth;
    private int height;
    private int length;

    private double weight;

    private int rotations;

    private boolean isOpen = false;

    private int remainingQuantity = 1;

    public BoxGroup(BoxGroup boxGroup) {
        this.weight = boxGroup.weight;
        this.rotations = boxGroup.rotations;
        this.depth = boxGroup.getDepth();
        this.height = boxGroup.getHeight();
        this.length = boxGroup.getLength();
        this.remainingQuantity = boxGroup.getRemainingQuantity();
    }

    public BoxGroup(int depth, int height, int length, double weight, int rotations) {
        this.weight = weight;
        this.rotations = rotations;
        this.depth = depth;
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

    public void decrementQuantity(List<BoxGroup> boxGroupList) {
        BoxGroup original = BoxUtil.getEqualBox(this, boxGroupList);
        original.setRemainingQuantity(original.getRemainingQuantity() - 1);
        remainingQuantity -= 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoxGroup boxGroup = (BoxGroup) o;
        return areBoxesEqual(this, boxGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depth, height, length);
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

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
