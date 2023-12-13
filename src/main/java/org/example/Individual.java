package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.Main.boxes;
import static org.example.Main.container;

public class Individual {
    List<Integer> genes;

    Individual() {
        genes = new ArrayList<>();
    }

    void generateRandomGenes(int numberOfBoxes) {
        for (int i = 0; i < numberOfBoxes; i++) {
            genes.add(new Random().nextBoolean() ? 1 : 0);
        }
    }

    public double fitness() {
        double totalVolume = 0;
        for(int i = 0; i < genes.size(); i++){
            if(genes.get(i) == 1) {
                double volume = boxes.get(i).getHeight() * boxes.get(i).getLength() * boxes.get(i).getDepth();
                totalVolume += volume;
            }
        }
        double containerVolume = container.getHeight() * container.getLength() * container.getWidth();
        return totalVolume <= containerVolume ? totalVolume : 0;
    }
}
