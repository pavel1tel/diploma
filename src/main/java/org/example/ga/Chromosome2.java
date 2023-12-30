package org.example.ga;

import org.example.fillingHeuristic.*;
import org.example.towerHeuristic.Box;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Main2.*;

public class Chromosome2 {
    List<Gene2> genes;

    RecursiveFillingHeuristic2 fillingHeuristic = new RecursiveFillingHeuristic2();

    public Chromosome2() {
        genes = new ArrayList<>();
    }

    public Chromosome2(List<Gene2> genes) {
        this.genes = genes;
    }

    public void generateRandomGenes(int nubmerOfBoxes) {
        Random random = new Random();
        List<Integer> integerList = IntStream.range(0, nubmerOfBoxes)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(integerList);
        for (Integer i : integerList) {
            Gene2 gene = new Gene2();
            gene.setRotation(random.nextInt(2));
            gene.setBoxIndex(i);
            genes.add(gene);
        }
    }

    public double fitness() {
        List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(this, towers, container);
        double chromoTotalVolume = 0.0;
        for (TowerPlacement towerPlacement : towerPlacements) {
            Box box = boxes.get(towerPlacement.getTowerNumber());
            chromoTotalVolume += box.getDepth() * box.getLength();
        }
        return chromoTotalVolume;
    }

    public double weightFitness() {
        return 500;
    }

    public boolean dominates(Chromosome p){
        if(this.fitness() > p.fitness() && this.weightFitness() > p.weightFitness()){
            return true;
        }
        return false;
    }

    public List<Gene2> getGenes() {
        return genes;
    }
}
