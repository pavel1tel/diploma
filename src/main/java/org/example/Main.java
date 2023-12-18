package org.example;

import org.example.fillingHeuristic.InversionMutation;
import org.example.fillingHeuristic.OrderCrossover;
import org.example.ga.Chromosome;
import org.example.selection.EliteStrategy;
import org.example.selection.SelectionStrategy;
import org.example.towerHeuristic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int POPULATION_SIZE = 100;
    public static List<Box> boxes = new ArrayList<>();
    public static Container container = new Container(40, 8, 8);
    public static ArrayList<Tower> towers = new ArrayList<>();


    static {
        Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            boxes.add(new Box(random.nextInt(1, 5), random.nextInt(1, 5), random.nextInt(1, 5)));
        }
    }

    private static List<Chromosome> initializePopulation(ArrayList<Tower> towers) {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Chromosome chromosome = new Chromosome();
            chromosome.generateRandomGenes(towers.size());
            population.add(chromosome);
        }
        return population;
    }

    private static Chromosome getRandomIndividual(List<Chromosome> parents) {
        Random random = new Random();
        int randomIndex = random.nextInt(parents.size());
        Chromosome individual = parents.get(randomIndex);
        parents.remove(randomIndex);
        return individual;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        GenerateTowers generateTowers = new GenerateTowers(boxGroups);
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
        }
        List<Chromosome> population = initializePopulation(towers);
        for (int i = 0; i < 500; i++) {
            SelectionStrategy selectionStrategy = new EliteStrategy();
            List<Chromosome> selected = selectionStrategy.select(population, true, 30, new Random());
            OrderCrossover orderCrossover = new OrderCrossover();
            InversionMutation inversionMutation = new InversionMutation();
            for (int j = 0; j < 30; j += 1) {
                if (new Random().nextBoolean() && j + 1 < selected.size()) {
                    Chromosome newChromo = orderCrossover.crossover(selected.get(j), selected.get(j + 1));
                    population.add(newChromo);
                    j++;
                } else {
                    population.add(inversionMutation.mutate(selected.get(j)));
                }
            }
            population.sort((c1, c2) -> (int) (c2.fitness() - c1.fitness()));
            population = new ArrayList<>(population.subList(0, 100));
        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        System.out.println("Millis: " + duration);
        System.out.println("Volume: " + population.get(0).fitness());

    }
}