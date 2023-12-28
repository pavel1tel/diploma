package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InversionMutation {
    public Chromosome mutate(Chromosome chromosome){
        Random rnd = new Random();
        List<Gene> genes = new ArrayList<>();
        int maxWindowSize = chromosome.getGenes().size() / 2;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start = new Random().nextInt(0, chromosome.getGenes().size() - windowSize);
        int end = start + windowSize;
        genes.addAll(chromosome.getGenes().subList(0, start));
        for (int i = end; i >= start; i--) {
            Gene gene = new Gene();
            gene.setRotation(chromosome.getGenes().get(i).getRotation() ^ 1);
            gene.setTowersIndex(chromosome.getGenes().get(i).getTowersIndex());
            genes.add(gene);
        }
        genes.addAll(chromosome.getGenes().subList(end + 1, chromosome.getGenes().size()));
        return new Chromosome(genes);
    }
}
