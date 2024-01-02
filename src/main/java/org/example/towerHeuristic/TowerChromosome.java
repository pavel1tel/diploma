package org.example.towerHeuristic;

import org.example.fillingHeuristic.DummyFillingHeuristic;
import org.example.fillingHeuristic.FillingHeuristic;
import org.example.fillingHeuristic.RecursiveFillingHeuristic;
import org.example.fillingHeuristic.TowerPlacement;
import org.example.ga.TowerGene;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Main.*;

public class TowerChromosome {
    List<TowerGene> genes;


    public TowerChromosome() {
        genes = new ArrayList<>();
    }

    public TowerChromosome(List<TowerGene> genes) {
        this.genes = genes;
    }

    public void generateRandomGenes(int numberOfBoxGroups) {
        Random random = new Random();
        List<Integer> integerList = IntStream.range(0, numberOfBoxGroups)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(integerList);
        for (Integer i : integerList) {
            TowerGene gene = new TowerGene(i);
            genes.add(gene);
        }
    }

    public double fitness() {
        List<Tower> towerList = generateTowers();
        double totalWaste = 0;
        for (Tower tower : towerList) {
            totalWaste += tower.getTotalWaste();
        }
        return totalWaste;
    }

    public ArrayList<Tower> generateTowers(){
        List<BoxGroup> inOrder = new ArrayList<>();
        List<BoxGroup> boxGroup = BoxUtil.groupBoxesByTypes(boxes);
        for (TowerGene towerGene : genes) {
            inOrder.add(boxGroup.get(towerGene.getBoxGroupIndex()));
        }
        GenerateTowers generateTowers = new GenerateTowers(inOrder);
        ArrayList<Tower> towerList = generateTowers.generateTowers();
        return towerList;
    }

    public List<TowerGene> getGenes() {
        return genes;
    }

    public void setGenes(List<TowerGene> genes) {
        this.genes = genes;
    }
}
