package org.example.towerHeuristic;

import java.util.ArrayList;
import java.util.List;

import static org.example.Main.container;

public class Tower {
    private double depth;
    private double height;
    private double length;
    private Tower towerOnTop;

    private Tower onFront;
    private Tower onSide;

    public Tower getOnFront() {
        return onFront;
    }

    public void setOnFront(Tower onFront) {
        this.onFront = onFront;
    }

    public Tower getOnSide() {
        return onSide;
    }

    public void setOnSide(Tower onSide) {
        this.onSide = onSide;
    }

    public Tower getTowerOnTop() {
        return towerOnTop;
    }

    public void setTowerOnTop(Tower towerOnTop) {
        this.towerOnTop = towerOnTop;
    }

    private List<Box> boxes = new ArrayList<>();


    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public Tower(double depth, double height, double length) {
        this.depth = depth;
        this.height = height;
        this.length = length;
    }

    public double getWastedSpace() {
        double maximumVolume = this.getDepth() * container.getHeight() * this.getLength();
        double towerTotalVolume = getTotalVolume(this);
        return maximumVolume - towerTotalVolume;
    }

    public double getTotalVolume(Tower tower) {
        if (tower.getBoxes().isEmpty()) {
            return 0;
        }
        double boxesVolume = tower.getBoxes().get(0).getDepth() * tower.getBoxes().get(0).getHeight()
                * tower.getBoxes().get(0).getLength() * tower.getBoxes().size();
        if (tower.getTowerOnTop() != null) {
            boxesVolume += getTotalVolume(tower.getTowerOnTop());
        }
        if (tower.getOnFront() != null) {
            boxesVolume += getTotalVolume(tower.getOnFront());
        }
        if (tower.getOnSide() != null) {
            boxesVolume += getTotalVolume(tower.getOnSide());
        }
        return boxesVolume;
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
