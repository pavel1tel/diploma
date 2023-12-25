package org.example.ga;

import java.util.ArrayList;
import java.util.List;

public class NonDominatedSort {

    public static List<List<Chromosome>> performNonDominatedSorting(List<Chromosome> population) {
        List<List<Chromosome>> fronts = new ArrayList<>();
        List<Chromosome> currentFront = new ArrayList<>();

        for (Chromosome p : population) {
            p.dominationCount = 0;
            p.dominatedSolutions.clear();

            for (Chromosome q : population) {
                if (p.dominates(q)) {
                    p.dominatedSolutions.add(population.indexOf(q));
                } else if (q.dominates(p)) {
                    p.dominationCount++;
                }
            }

            if (p.dominationCount == 0) {
                p.rank = 1;
                currentFront.add(p);
            }
        }

        fronts.add(new ArrayList<>(currentFront));

        int frontIndex = 0;

        while (!currentFront.isEmpty()) {
            List<Chromosome> nextFront = new ArrayList<>();
            for (Chromosome p : currentFront) {
                for (int qIndex : p.dominatedSolutions) {
                    Chromosome q = population.get(qIndex);
                    q.dominationCount--;

                    if (q.dominationCount == 0) {
                        nextFront.add(q);
                    }
                }
            }

            currentFront = new ArrayList<>(nextFront);
            fronts.add(currentFront);
            frontIndex++;
        }

        return fronts;
    }
}
