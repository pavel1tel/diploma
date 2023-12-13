package org.example.selection;

import org.example.Individual;

import java.util.List;
import java.util.Random;


public interface SelectionStrategy
{
    List<Individual> select(List<Individual> population,
                                 boolean naturalFitnessScores,
                                 int selectionSize,
                                 Random rng);
}