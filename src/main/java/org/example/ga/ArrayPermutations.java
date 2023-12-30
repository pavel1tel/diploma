package org.example.ga;

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
}
