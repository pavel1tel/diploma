package org.example.fillingHeuristic;

public class TowerPlacement {
    Integer towerNumber;
    Integer rotation;
    double xcord;
    double ycord;

    public TowerPlacement(Integer towerNumber, Integer rotation, double xcord, double ycord) {
        this.towerNumber = towerNumber;
        this.rotation = rotation;
        this.xcord = xcord;
        this.ycord = ycord;
    }

    public Integer getTowerNumber() {
        return towerNumber;
    }

    public void setTowerNumber(Integer towerNumber) {
        this.towerNumber = towerNumber;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
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
}
