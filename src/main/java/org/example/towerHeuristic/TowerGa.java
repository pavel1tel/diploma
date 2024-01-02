package org.example.towerHeuristic;

import java.util.ArrayList;
import java.util.List;

import static org.example.Main.boxes;
import static org.example.towerHeuristic.TowerCrowding.crowdingStep;

public class TowerGa {

    private static ArrayList<TowerChromosome> initializePopulation() {
        List<BoxGroup> boxGroup = BoxUtil.groupBoxesByTypes(boxes);
        ArrayList<TowerChromosome> population = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TowerChromosome chromosome = new TowerChromosome();
            chromosome.generateRandomGenes(boxGroup.size());
            population.add(chromosome);
        }
        return population;
    }

    public static ArrayList<Tower> GAloop(){
        List<TowerChromosome> population = initializePopulation();
        TowerChromosome best = population.get(0);
        for (int i = 0; i < 200; i++) {
            population = crowdingStep(population, 4, 0.3, 0.9);
            for (TowerChromosome towerChromosome:
                    population) {
                if (towerChromosome.fitness() > best.fitness()){
                    best = towerChromosome;
                }
            }
            System.out.println(best.fitness());
        }

        return best.generateTowers();
    }
}
