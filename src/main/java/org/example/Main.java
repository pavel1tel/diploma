package org.example;

import org.example.heuristic.*;

import java.util.*;

public class Main {
    private static final int POPULATION_SIZE = 100;
    public static List<Box> boxes = new ArrayList<>();
    public static Container container = new Container(10, 10, 10);
    private static final double CROSSOVER_RATE = 1.0;
    private static final double MUTATION_RATE = 0.1;

    private static final int MAX_GENERATIONS = 1000;

    static {
        int boxV = 5 * 5 * 5;
        int containerV = (int) (container.getHeight() * container.getWidth() * container.getLength());
//        for (int i = 0; i < containerV / boxV; i++) {
//            boxes.add(new Box(7, 6, 8));
//        }
        for (int i = 0; i < 1000; i++) {
            boxes.add(new Box(1, 1, 1));
        }
        for (int i = 0; i < 1000; i++) {
            boxes.add(new Box(2, 2, 2));
        }
        for (int i = 0; i < 1000; i++) {
            boxes.add(new Box(3, 3, 3));
        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(3, 3, 3));
//        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(2, 2, 2));
//        }
    }

    private static List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Individual individual = new Individual();
            individual.generateRandomGenes(boxes.size());
            population.add(individual);
        }
        return population;
    }

    private static Individual crossover(Individual parent1, Individual parent2) {
        Individual child = new Individual();
        int crossoverPoint = new Random().nextInt(boxes.size());
        child.genes.addAll(parent1.genes.subList(0, crossoverPoint));
        child.genes.addAll(parent2.genes.subList(crossoverPoint, boxes.size()));
        return child;
    }

    private static void mutate(Individual individual) {
        int mutationPoint = new Random().nextInt(boxes.size());
        individual.genes.set(mutationPoint, new Random().nextBoolean() ? 1 : 0);
    }

    private static List<Individual> generateOffspring(List<Individual> parents) {
        List<Individual> offspring = new ArrayList<>();
        while (!parents.isEmpty()) {
            Individual parent1 = getRandomIndividual(parents);
            Individual parent2 = getRandomIndividual(parents);
            if (Math.random() < CROSSOVER_RATE) {
                Individual child = crossover(parent1, parent2);
                if (Math.random() < MUTATION_RATE) {
                    mutate(child);
                }
                offspring.add(child);
            }
        }
        return offspring;
    }

    private static Individual getRandomIndividual(List<Individual> parents) {
        Random random = new Random();
        int randomIndex = random.nextInt(parents.size());
        Individual individual = parents.get(randomIndex);
        parents.remove(randomIndex);
        return individual;
    }

    public static void main(String[] args) {
        List<Individual> population = initializePopulation();
        List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        GenerateTowers generateTowers = new GenerateTowers(boxGroups);
        ArrayList<Tower> towers = new ArrayList<>();
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
            System.out.println(tower.getWastedSpace());
        }
        System.out.println(towers.size());
//        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
//            SelectionStrategy selection = new TournamentSelection(0.5);
//            List<Individual> selected = selection.select(population, true, (int) (population.size() * 0.3), new Random());
//            List<Individual> offspring = generateOffspring(selected);
//            population.addAll(offspring);
//            population.sort((a, b) -> (int) (b.fitness() - a.fitness()));
//            population = population.subList(0, POPULATION_SIZE);
//
//            Individual bestIndividual = Collections.max(population, Comparator.comparingDouble(Individual::fitness));
//            System.out.println("generation: " + generation +" Best Fitness: " + bestIndividual.fitness());        }

    }
}