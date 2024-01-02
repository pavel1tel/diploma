package org.example.towerHeuristic;

import org.example.ga.TowerGene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TowerOrderCrossover {

    public ArrayList<TowerChromosome> crossover(TowerChromosome p1, TowerChromosome p2) {
        Random rnd = new Random();
        int maxWindowSize = p1.getGenes().size() / 2;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start = new Random().nextInt(0, p1.getGenes().size() - windowSize);
        int end = start + windowSize;
        List<TowerGene> child1 = new ArrayList<>(p1.getGenes().subList(start, end));
        List<TowerGene> child2 = new ArrayList<>(p2.getGenes().subList(start, end));
        int currentGenIndex = 0;
        TowerGene currentGenInChild1 = null;
        TowerGene currentGenInChild2 = null;
        for (int i = 0; i < p1.getGenes().size(); i++) {
            currentGenIndex = (end + i) % p1.getGenes().size();
            currentGenInChild1 = p1.getGenes().get(currentGenIndex);
            currentGenInChild2 = p2.getGenes().get(currentGenIndex);
            if (!child1.contains(currentGenInChild2)) {
                child1.add(currentGenInChild2);
            }
            if (!child2.contains(currentGenInChild1)) {
                child2.add(currentGenInChild1);
            }
        }
        Collections.rotate(child1, start);
        Collections.rotate(child2, start);
        ArrayList<TowerChromosome> result = new ArrayList<>();
        result.add(new TowerChromosome(child1));
        result.add(new TowerChromosome(child2));
        return result;
    }
}
