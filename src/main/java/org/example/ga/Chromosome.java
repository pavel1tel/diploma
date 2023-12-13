package org.example.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Main.boxes;
import static org.example.Main.container;

public class Chromosome {
    List<Gene> genes;

    public Chromosome() {
        genes = new ArrayList<>();
    }

    public void generateRandomGenes(int numberOfTowers) {
        Random random = new Random();
        List<Integer> integerList = IntStream.range(0, numberOfTowers)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(integerList);
        for (Integer i : integerList) {
            Gene gene = new Gene();
            gene.setRotation(random.nextInt(2));
            gene.setTowersIndex(i);
            genes.add(gene);
        }
    }

    public double fitness() {
        return 0;
    }

    public List<Gene> getGenes() {
        return genes;
    }
}
