package org.example.ga;

import org.example.fillingHeuristic.InversionMutation;
import org.example.fillingHeuristic.OrderCrossover;

import java.util.*;

import static org.example.ga.ArrayPermutations.generatePermutations;
import static org.example.ga.ArrayPermutations.generatePermutations2;

public class Crowding2 {

    public static List<Chromosome2> crowdingStep(List<Chromosome2> oldPop, int S, double Pm, double Pc) {
        List<Chromosome2> result = new ArrayList<>();
        List<Integer> indexPool = new ArrayList<>();
        List<Chromosome2> parent = new ArrayList<>();
        List<Chromosome2> child = new ArrayList<>();
        OrderCrossover orderCrossover = new OrderCrossover();
        InversionMutation inversionMutation = new InversionMutation();
        int[][] distance = new int[S][S];
        Random random = new Random();
        for (int i = 0; i < oldPop.size(); i++) {
            indexPool.add(i);
        }
        while (indexPool.size() > 1) {
            parent.clear();
            child.clear();
            distance = new int[S][S];
            //STEP 1
            for (int i = 0; i < S; i++) {
                if (indexPool.size() == 2) {
                    Integer j = indexPool.get(1);
                    parent.add(oldPop.get(j));
                    indexPool.remove(1);
                } else if (indexPool.size() == 1) {
                    Integer j = indexPool.get(0);
                    parent.add(oldPop.get(j));
                    indexPool.remove(0);
                } else {
                    int randInt = random.nextInt(1, indexPool.size());
                    Integer j = indexPool.get(randInt);
                    parent.add(oldPop.get(j));
                    indexPool.remove(randInt);
                }
            }
            //STEP 2
            for (int i = 0; i < S; i += 2) {
                if (Pc > random.nextDouble()) {
                    child.addAll(orderCrossover.crossover2(parent.get(i), parent.get(i + 1)));
                } else {
                    child.add(parent.get(i));
                    child.add(parent.get(i + 1));
                }
                if (Pm > random.nextDouble()) {
                    child.set(child.size() -1, inversionMutation.mutate2(child.get(i)));
                }
                if (Pm > random.nextDouble()) {
                    child.set(child.size() -1, inversionMutation.mutate2(child.get(i + 1)));
                }
            }
            //STEP 3
            for (int i = 0; i < S; i++) {
                for (int j = 0; j < S; j++) {
                    distance[i][j] = distanceFunction(parent.get(i), child.get(j));
                }
            }
            //STEP 4
            List<Chromosome2> m = match(parent, child);
            //STEP 5
            for (int i = 0; i < S * 2; i += 2) {
                Chromosome2 p = m.get(i);
                Chromosome2 c = m.get(i + 1);
                if (p.fitness() >= c.fitness()) {
                    result.add(p);
                } else {
                    result.add(c);
                }
            }
        }

        return result;
    }

    public static int distanceFunction(Chromosome2 c1, Chromosome2 c2) {
        int result = 0;
        for (int i = 0; i < c1.getGenes().size(); i++) {
            if (!(Objects.equals(c1.getGenes().get(i).getBoxIndex(), c2.getGenes().get(i).getBoxIndex()) &&
                    Objects.equals(c1.getGenes().get(i).getRotation(), c2.getGenes().get(i).getRotation()))) {
                result += 1;
            }
        }
        return result;
    }

    public static List<Chromosome2> match(List<Chromosome2> parent, List<Chromosome2> child) {
        List<Chromosome2> result = null;
        int resultTotalDistance = Integer.MAX_VALUE;
        List<List<Chromosome2>> parentPermutations = new ArrayList<>();
        generatePermutations2(parent, 0, parentPermutations);
        for (List<Chromosome2> parentMutation : parentPermutations) {
            ArrayList<Chromosome2> candidate = new ArrayList<>();
            Iterator<Chromosome2> childI = child.iterator();
            Iterator<Chromosome2> parentI = parent.iterator();
            for (int i = 0; i < parent.size() * 2; i++) {
                if (i % 2 == 0) {
                    candidate.add(parentI.next());
                } else {
                    candidate.add(childI.next());
                }
            }
            if (evaluateDistance(candidate) < resultTotalDistance) {
                result = candidate;
                resultTotalDistance = evaluateDistance(candidate);
            }
        }

        return result;
    }

    private static int evaluateDistance(List<Chromosome2> chromosomes) {
        int totalDistance = 0;
        for (int i = 0; i < chromosomes.size(); i += 2) {
            totalDistance += distanceFunction(chromosomes.get(i), chromosomes.get(i + 1));
        }
        return totalDistance;
    }
}
