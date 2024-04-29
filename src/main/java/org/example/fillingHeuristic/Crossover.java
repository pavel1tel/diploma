package org.example.fillingHeuristic;

import org.example.ga.Chromosome;

import java.util.ArrayList;

public interface Crossover {
    ArrayList<Chromosome> crossover(Chromosome p1, Chromosome p2);
}
