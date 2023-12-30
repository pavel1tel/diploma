package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Chromosome2;
import org.example.ga.Gene;
import org.example.ga.Gene2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderCrossover {

    public ArrayList<Chromosome> crossover(Chromosome p1, Chromosome p2) {
        Random rnd = new Random();
        int maxWindowSize = p1.getGenes().size() / 2;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start = new Random().nextInt(0, p1.getGenes().size() - windowSize);
        int end = start + windowSize;
        List<Gene> child1 = new ArrayList<>(p1.getGenes().subList(start, end));
        List<Gene> child2 = new ArrayList<>(p2.getGenes().subList(start, end));
        int currentGenIndex = 0;
        Gene currentGenInChild1 = null;
        Gene currentGenInChild2 = null;
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
        ArrayList<Chromosome> result = new ArrayList<>();
        result.add(new Chromosome(child1));
        result.add(new Chromosome(child2));
        return result;
    }

    public ArrayList<Chromosome2> crossover2(Chromosome2 p1, Chromosome2 p2) {
        Random rnd = new Random();
        int maxWindowSize = p1.getGenes().size() / 2;
        int windowSize = rnd.nextInt(1, maxWindowSize);
        int start = new Random().nextInt(0, p1.getGenes().size() - windowSize);
        int end = start + windowSize;
        List<Gene2> child1 = new ArrayList<>(p1.getGenes().subList(start, end));
        List<Gene2> child2 = new ArrayList<>(p2.getGenes().subList(start, end));
        int currentGenIndex = 0;
        Gene2 currentGenInChild1 = null;
        Gene2 currentGenInChild2 = null;
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
        ArrayList<Chromosome2> result = new ArrayList<>();
        result.add(new Chromosome2(child1));
        result.add(new Chromosome2(child2));
        return result;
    }
}
