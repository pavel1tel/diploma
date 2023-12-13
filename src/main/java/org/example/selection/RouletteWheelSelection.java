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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements SelectionStrategy
{
    public List<Chromosome> select(List<Chromosome> population,
                                   boolean naturalFitnessScores,
                                   int selectionSize,
                                   Random rng)
    {
        double[] cumulativeFitnesses = new double[population.size()];
        cumulativeFitnesses[0] = getAdjustedFitness(population.get(0).fitness(),
                naturalFitnessScores);
        for (int i = 1; i < population.size(); i++)
        {
            double fitness = getAdjustedFitness(population.get(i).fitness(),
                    naturalFitnessScores);
            cumulativeFitnesses[i] = cumulativeFitnesses[i - 1] + fitness;
        }

        List<Chromosome> selection = new ArrayList<>(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            double randomFitness = rng.nextDouble() * cumulativeFitnesses[cumulativeFitnesses.length - 1];
            int index = Arrays.binarySearch(cumulativeFitnesses, randomFitness);
            if (index < 0)
            {
                index = Math.abs(index + 1);
            }
            selection.add(population.get(index));
        }
        return selection;
    }


    private double getAdjustedFitness(double rawFitness,
                                      boolean naturalFitness)
    {
        if (naturalFitness)
        {
            return rawFitness;
        }
        else
        {
            // If standardised fitness is zero we have found the best possible
            // solution.  The evolutionary algorithm should not be continuing
            // after finding it.
            return rawFitness == 0 ? Double.POSITIVE_INFINITY : 1 / rawFitness;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Roulette Wheel Selection";
    }
}