package org.example.towerHeuristic;

import org.example.fillingHeuristic.InversionMutation;
import org.example.fillingHeuristic.OrderCrossover;
import org.example.ga.Chromosome;

import java.util.*;

import static org.example.Main.*;
import static org.example.ga.ArrayPermutations.generatePermutations;
import static org.example.ga.ArrayPermutations.generatePermutations2;

public class TowerCrowding {

    public static List<TowerChromosome> crowdingStep(List<TowerChromosome> oldPop, int S, double Pm, double Pc) {
        List<TowerChromosome> result = new ArrayList<>();
        List<Integer> indexPool = new ArrayList<>();
        List<TowerChromosome> parent = new ArrayList<>();
        List<TowerChromosome> child = new ArrayList<>();
        TowerOrderCrossover orderCrossover = new TowerOrderCrossover();
        TowerInversionMutation inversionMutation = new TowerInversionMutation();
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
                    child.addAll(orderCrossover.crossover(parent.get(i), parent.get(i + 1)));
                } else {
                    child.add(parent.get(i));
                    child.add(parent.get(i + 1));
                }
                if (Pm > random.nextDouble()) {
                    child.set(child.size() -1, inversionMutation.mutate(child.get(i)));
                }
                if (Pm > random.nextDouble()) {
                    child.set(child.size() -1, inversionMutation.mutate(child.get(i + 1)));
                }
            }
            //STEP 3
            for (int i = 0; i < S; i++) {
                for (int j = 0; j < S; j++) {
                    distance[i][j] = distanceFunction(parent.get(i), child.get(j));
                }
            }
            //STEP 4
            List<TowerChromosome> m = match(parent, child);
            //STEP 5
            for (int i = 0; i < S * 2; i += 2) {
                TowerChromosome p = m.get(i);
                TowerChromosome c = m.get(i + 1);
                if (p.fitness() >= c.fitness()) {
                    result.add(p);
                } else {
                    result.add(c);
                }
            }
        }

        return removeDuplicates(result);
    }

    public static List<TowerChromosome> removeDuplicates(List<TowerChromosome> chromosomes){
        Set<TowerChromosome> set = new HashSet<>(chromosomes);
        ArrayList<TowerChromosome> result = new ArrayList<>(set);
        while (result.size() != POPULATION_SIZE){
            List<BoxGroup> boxGroup = BoxUtil.groupBoxesByTypes(boxes);
            result.add(new TowerChromosome().generateRandomGenes(boxGroup.size()));
        }
        return result;
    }

    public static int distanceFunction(TowerChromosome c1, TowerChromosome c2) {
        int result = 0;
        for (int i = 0; i < c1.getGenes().size(); i++) {
            if (c1.getGenes().size() != c2.getGenes().size()){
                System.out.println(c1.getGenes().size());
                System.out.println(c2.getGenes().size());
            }
            if (!(Objects.equals(c1.getGenes().get(i).getBoxGroupIndex(), c2.getGenes().get(i).getBoxGroupIndex()))) {
                result += 1;
            }
        }
        return result;
    }

    public static List<TowerChromosome> match(List<TowerChromosome> parent, List<TowerChromosome> child) {
        List<TowerChromosome> result = null;
        int resultTotalDistance = Integer.MAX_VALUE;
        List<List<TowerChromosome>> parentPermutations = new ArrayList<>();
        generatePermutations2(parent, 0, parentPermutations);
        for (List<TowerChromosome> parentMutation : parentPermutations) {
            ArrayList<TowerChromosome> candidate = new ArrayList<>();
            Iterator<TowerChromosome> childI = child.iterator();
            Iterator<TowerChromosome> parentI = parent.iterator();
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

    private static int evaluateDistance(List<TowerChromosome> chromosomes) {
        int totalDistance = 0;
        for (int i = 0; i < chromosomes.size(); i += 2) {
            totalDistance += distanceFunction(chromosomes.get(i), chromosomes.get(i + 1));
        }
        return totalDistance;
    }
}
