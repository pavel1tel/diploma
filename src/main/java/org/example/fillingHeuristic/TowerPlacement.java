package org.example.fillingHeuristic;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowerPlacement that = (TowerPlacement) o;
        return Objects.equals(towerNumber, that.towerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(towerNumber);
    }
}
