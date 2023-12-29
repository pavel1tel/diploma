package org.example.fillingHeuristic;

import java.util.ArrayList;
import java.util.List;

public class Row {
    int maxDepth = 0;
    List<Integer> surface = new ArrayList<>();

    public Row(int maxLength, List<Integer> surface) {
        this.maxDepth = maxLength;
        this.surface = surface;
    }

    public Row() {

    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxLength) {
        this.maxDepth = maxLength;
    }
}
