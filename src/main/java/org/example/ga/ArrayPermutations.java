package org.example.ga;

import org.example.towerHeuristic.TowerChromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayPermutations {
    public static void generatePermutations(List<Chromosome> chromosomes, int index, List<List<Chromosome>> result) {
        if (index == chromosomes.size() - 1) {
            result.add(chromosomes);
            return;
        }

        for (int i = index; i < chromosomes.size(); i++) {
            // Swap elements at index and i
            swap(chromosomes, index, i);

            // Recursively generate permutations for the remaining elements
            generatePermutations(chromosomes, index + 1, result);

            // Backtrack by swapping back to the original order
            swap(chromosomes, index, i);
        }
    }

    private static void swap(List<Chromosome> chromosomes, int i, int j) {
        Chromosome temp = chromosomes.get(i);
        chromosomes.set(i, chromosomes.get(j));
        chromosomes.set(j, temp);
    }

    public static void generatePermutations2(List<TowerChromosome> chromosomes, int index, List<List<TowerChromosome>> result) {
        if (index == chromosomes.size() - 1) {
            result.add(chromosomes);
            return;
        }

        for (int i = index; i < chromosomes.size(); i++) {
            // Swap elements at index and i
            swap2(chromosomes, index, i);

            // Recursively generate permutations for the remaining elements
            generatePermutations2(chromosomes, index + 1, result);

            // Backtrack by swapping back to the original order
            swap2(chromosomes, index, i);
        }
    }

    private static void swap2(List<TowerChromosome> chromosomes, int i, int j) {
        TowerChromosome temp = chromosomes.get(i);
        chromosomes.set(i, chromosomes.get(j));
        chromosomes.set(j, temp);
    }
}
