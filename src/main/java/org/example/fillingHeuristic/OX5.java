package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;

import java.util.*;

public class OX5 implements Crossover{

    public ArrayList<Chromosome> crossover(Chromosome p1, Chromosome p2) {
        Random rnd = new Random();
        int maxWindowSize = p1.getGenes().size() / 4;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start1 = new Random().nextInt(0, p1.getGenes().size() / 2 - windowSize);
        int end1 = start1 + windowSize;
        int start2 = new Random().nextInt(p1.getGenes().size() / 2, p1.getGenes().size() - windowSize);
        int end2 = start2 + windowSize;
        Gene[] child1 = new Gene[p1.getGenes().size()];
        Gene[] child2 = new Gene[p1.getGenes().size()];
        for (int i = start1; i < end1; i++) {
            child1[i] = p1.getGenes().get(i);
            child2[i] = p2.getGenes().get(i);
        }
        for (int i = start2; i < end2; i++) {
            child1[i] = p1.getGenes().get(i);
            child2[i] = p2.getGenes().get(i);
        }
        Deque<Gene> p2_unused = new ArrayDeque<>();
        for (Gene gene: p2.getGenes()) {
            if (Arrays.stream(child1).filter(Objects::nonNull).noneMatch(g -> g.equals(gene))){
                p2_unused.add(gene);
            }
        }
        Deque<Gene> p1_unused = new ArrayDeque<>();
        for (Gene gene: p1.getGenes()) {
            if (Arrays.stream(child2).filter(Objects::nonNull).noneMatch(g -> g.equals(gene))){
                p1_unused.add(gene);
            }
        }
        for (int i = 0; i < p1.getGenes().size(); i++) {
            if (child1[i] == null){
                child1[i] = p2_unused.removeFirst();
                child2[i] = p1_unused.removeFirst();
            }
        }
        ArrayList<Chromosome> result = new ArrayList<>();
        result.add(new Chromosome(Arrays.stream(child1).toList()));
        result.add(new Chromosome(Arrays.stream(child2).toList()));
        return result;
    }
}
