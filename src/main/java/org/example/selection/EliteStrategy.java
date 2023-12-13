package org.example.selection;

import org.example.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EliteStrategy  implements SelectionStrategy{
    @Override
    public List<Individual> select(List<Individual> population, boolean naturalFitnessScores, int selectionSize, Random rng) {
        population.sort((a, b) -> (int) (b.fitness() - a.fitness()));
        return  new ArrayList<>(population.subList(0, selectionSize));
    }
}
