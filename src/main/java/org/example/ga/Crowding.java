package org.example.ga;

import org.example.fillingHeuristic.HeuristicCrossover;
import org.example.fillingHeuristic.HeuristicMutation;
import org.example.fillingHeuristic.InversionMutation;
import org.example.fillingHeuristic.OrderCrossover;

import java.util.*;

import static org.example.Main.POPULATION_SIZE;
import static org.example.Main.towers;
import static org.example.ga.ArrayPermutations.generatePermutations;

public class Crowding {

    public static List<Chromosome> crowdingStep(List<Chromosome> oldPop, int S, double Pm, double Pc) {
        Double max = oldPop.stream().mapToDouble(c -> (int) c.fitness()).max().getAsDouble();
        Double min = oldPop.stream().mapToDouble(c -> (int) c.fitness()).min().getAsDouble();
        List<Chromosome> result = new ArrayList<>();
        List<Integer> indexPool = new ArrayList<>();
        List<Chromosome> parent = new ArrayList<>();
        List<Chromosome> child = new ArrayList<>();
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
            List<Chromosome> m = match(parent, child);
            //STEP 5
            for (int i = 0; i < S * 2; i += 2) {
                Chromosome p = m.get(i);
                Chromosome c = m.get(i + 1);
                if (p.fitness() >= c.fitness()) {
                    result.add(p);
                } else {
                    result.add(c);
                }
            }
        }

        return removeDuplicates(result);
    }

    public static List<Chromosome> removeDuplicates(List<Chromosome> chromosomes){
        Set<Chromosome> set = new HashSet<>(chromosomes);
        ArrayList<Chromosome> result = new ArrayList<>(set);
        while (result.size() != POPULATION_SIZE){
            result.add(new Chromosome().generateRandomGenes(towers.size()));
        }
        return result;
    }

    public static int distanceFunction(Chromosome c1, Chromosome c2) {
        int result = 0;
        for (int i = 0; i < c1.getGenes().size(); i++) {
            if (!(Objects.equals(c1.getGenes().get(i).getTowersIndex(), c2.getGenes().get(i).getTowersIndex()) &&
                    Objects.equals(c1.getGenes().get(i).getRotation(), c2.getGenes().get(i).getRotation()))) {
                result += 1;
            }
        }
        return result;
    }

    public static List<Chromosome> match(List<Chromosome> parent, List<Chromosome> child) {
        List<Chromosome> result = null;
        int resultTotalDistance = Integer.MAX_VALUE;
        List<List<Chromosome>> parentPermutations = new ArrayList<>();
        generatePermutations(parent, 0, parentPermutations);
        for (List<Chromosome> parentMutation : parentPermutations) {
            ArrayList<Chromosome> candidate = new ArrayList<>();
            Iterator<Chromosome> childI = child.iterator();
            Iterator<Chromosome> parentI = parent.iterator();
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

    public static double getMutationProbability(double min, double max, Chromosome chromosome){
        return 1 - ((chromosome.fitness() - min)/(max - min));
    }

    private static int evaluateDistance(List<Chromosome> chromosomes) {
        int totalDistance = 0;
        for (int i = 0; i < chromosomes.size(); i += 2) {
            totalDistance += distanceFunction(chromosomes.get(i), chromosomes.get(i + 1));
        }
        return totalDistance;
    }
}
