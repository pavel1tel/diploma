package org.example.ga;

import java.util.List;
import java.util.Objects;

public class TowerGene {
    Integer boxGroupIndex;

    public TowerGene(Integer boxGroupIndex) {
        this.boxGroupIndex = boxGroupIndex;
    }

    public Integer getBoxGroupIndex() {
        return boxGroupIndex;
    }

    public void setBoxGroupIndex(Integer boxGroupIndex) {
        this.boxGroupIndex = boxGroupIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowerGene towerGene = (TowerGene) o;
        return Objects.equals(boxGroupIndex, towerGene.boxGroupIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxGroupIndex);
    }
}
