package org.example.ga;

import java.util.Objects;

public class Gene2 {
    Integer boxIndex;
    Integer rotation;

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getBoxIndex() {
        return boxIndex;
    }

    public void setBoxIndex(Integer boxIndex) {
        this.boxIndex = boxIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene2 gene = (Gene2) o;
        return Objects.equals(boxIndex, gene.boxIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxIndex);
    }
}
