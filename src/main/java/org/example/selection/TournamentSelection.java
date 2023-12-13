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

import org.example.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements SelectionStrategy
{
    private final double selectionProbability;


    public TournamentSelection(double selectionProbability)
    {
        this.selectionProbability = selectionProbability;
    }


    public List<Individual> select(List<Individual> population,
                              boolean naturalFitnessScores,
                              int selectionSize,
                              Random rng)
    {
        List<Individual> selection = new ArrayList<Individual>(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            // Pick two candidates at random.
            Individual candidate1 = population.get(rng.nextInt(population.size()));
            Individual candidate2 = population.get(rng.nextInt(population.size()));

            // Use a random value to decide wether to select the fitter individual or the weaker one.
            boolean selectFitter = rng.nextDouble() < selectionProbability;
            if (selectFitter == naturalFitnessScores)
            {
                // Select the fitter candidate.
                selection.add(candidate2.fitness() > candidate1.fitness()
                        ? candidate2
                        : candidate1);
            }
            else
            {
                // Select the less fit candidate.
                selection.add(candidate2.fitness() > candidate1.fitness()
                        ? candidate1
                        : candidate2);
            }
        }
        return selection;
    }
}