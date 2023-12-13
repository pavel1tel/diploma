package org.example.selection;

import org.example.ga.Chromosome;

import java.util.List;
import java.util.Random;


public interface SelectionStrategy
{
    List<Chromosome> select(List<Chromosome> population,
                            boolean naturalFitnessScores,
                            int selectionSize,
                            Random rng);
}