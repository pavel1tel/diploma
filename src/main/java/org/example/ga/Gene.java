package org.example.ga;

import java.util.List;
import java.util.Objects;

public class Gene {
    Integer towersIndex;
    Integer rotation;

    public Integer getTowersIndex() {
        return towersIndex;
    }

    public void setTowersIndex(Integer towersIndex) {
        this.towersIndex = towersIndex;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene gene = (Gene) o;
        return Objects.equals(towersIndex, gene.towersIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(towersIndex);
    }

    public Gene(Integer towersIndex, Integer rotation) {
        this.towersIndex = towersIndex;
        this.rotation = rotation;
    }

    public Gene() {
    }
}
