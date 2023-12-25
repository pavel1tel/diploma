//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package org.example.selection;

import org.example.ga.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements SelectionStrategy {
    private final double selectionProbability;
    private final int tournamentSize;


    public TournamentSelection(double selectionProbability, int tournamentSize) {
        this.selectionProbability = selectionProbability;
        this.tournamentSize = tournamentSize;
    }


    public List<Chromosome> select(List<Chromosome> population,
                                   boolean naturalFitnessScores,
                                   int selectionSize,
                                   Random rng) {
        List<Chromosome> selection = new ArrayList<Chromosome>(selectionSize);
        for (int i = 0; i < selectionSize; i++) {
            List<Chromosome> candidates = new ArrayList<>();

            for (int j = 0; j < tournamentSize; j++) {
                candidates.add(population.get(rng.nextInt(population.size())));
            }

            boolean selectFitter = rng.nextDouble() < selectionProbability;
            if (selectFitter == naturalFitnessScores) {
                selection.add(getMaxFitnessChromosome(candidates));
            } else {
                selection.add(getLessFintessChromosome(candidates));
            }
        }
        return selection;
    }

    private static Chromosome getMaxFitnessChromosome(List<Chromosome> chromosomeList) {
        if (chromosomeList.isEmpty()) {
            return null; // Return null if the list is empty
        }

        Chromosome maxFitnessChromosome = chromosomeList.get(0);

        for (Chromosome chromosome : chromosomeList) {
            if (chromosome.dominates(maxFitnessChromosome)) {
                maxFitnessChromosome = chromosome;
            }
        }

        return maxFitnessChromosome;
    }

    private static Chromosome getLessFintessChromosome(List<Chromosome> chromosomeList) {
        if (chromosomeList.isEmpty()) {
            return null; // Return null if the list is empty
        }

        Chromosome lessFitnessChromosome = chromosomeList.get(0);

        for (Chromosome chromosome : chromosomeList) {
            if (!chromosome.dominates(lessFitnessChromosome)) {
                lessFitnessChromosome = chromosome;
            }
        }

        return lessFitnessChromosome;
    }
}