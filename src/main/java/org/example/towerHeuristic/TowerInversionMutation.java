package org.example.towerHeuristic;

import org.example.ga.TowerGene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TowerInversionMutation {
    public TowerChromosome mutate(TowerChromosome chromosome){
        Random rnd = new Random();
        List<TowerGene> genes = new ArrayList<>();
        int maxWindowSize = chromosome.getGenes().size() / 2;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start = new Random().nextInt(0, chromosome.getGenes().size() - windowSize);
        int end = start + windowSize;
        genes.addAll(chromosome.getGenes().subList(0, start));
        for (int i = end; i >= start; i--) {
            TowerGene gene = new TowerGene(chromosome.getGenes().get(i).getBoxGroupIndex());
            genes.add(gene);
        }
        genes.addAll(chromosome.getGenes().subList(end + 1, chromosome.getGenes().size()));
        return new TowerChromosome(genes);
    }
}
