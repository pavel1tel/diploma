package org.example.ga;

import org.example.fillingHeuristic.DummyFillingHeuristic;
import org.example.fillingHeuristic.FillingHeuristic;
import org.example.fillingHeuristic.RecursiveFillingHeuristic;
import org.example.fillingHeuristic.TowerPlacement;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Main.*;

public class Chromosome {
    public int dominationCount = 0;
    public ArrayList<Integer> dominatedSolutions = new ArrayList<>();
    public int rank;
    List<Gene> genes;

    FillingHeuristic fillingHeuristic = new DummyFillingHeuristic();

    public Chromosome() {
        genes = new ArrayList<>();
    }

    public Chromosome(List<Gene> genes) {
        this.genes = genes;
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
        List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(this, towers, container);
        double chromoTotalVolume = 0.0;
        for (TowerPlacement towerPlacement : towerPlacements) {
            Tower tower = towers.get(towerPlacement.getTowerNumber());
            chromoTotalVolume += tower.getTotalVolume(tower);
        }
        return chromoTotalVolume;
    }

    public double weightFitness() {
        List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(this, towers, container);
        double chromoTotalWeight = 0.0;
        for (TowerPlacement towerPlacement : towerPlacements) {
            Tower tower = towers.get(towerPlacement.getTowerNumber());
            chromoTotalWeight += tower.getTotalWeight(tower);
        }
        return chromoTotalWeight >= 7200 ? 0 : chromoTotalWeight;
    }

    public boolean dominates(Chromosome p){
        if(this.fitness() > p.fitness() && this.weightFitness() > p.weightFitness()){
            return true;
        }
        return false;
    }

    public List<Gene> getGenes() {
        return genes;
    }
}
