package org.example;

import org.example.fillingHeuristic.InversionMutation;
import org.example.fillingHeuristic.OrderCrossover;
import org.example.ga.Chromosome;
import org.example.ga.NonDominatedSort;
import org.example.selection.SelectionStrategy;
import org.example.selection.TournamentSelection;
import org.example.towerHeuristic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.ga.GenerateCoordinates.writeCoordinates;

public class Main {
    private static final int POPULATION_SIZE = 100;
    public static List<Box> boxes = new ArrayList<>();
    public static Container container = new Container(530, 210, 220);
    public static ArrayList<Tower> towers = new ArrayList<>();


    static {
        for (int i = 0; i < 325; i++) {
            boxes.add(new Box(36, 40, 28, 20));
        }
        for (int i = 0; i < 25; i++) {
            boxes.add(new Box(28, 54, 30, 22));
        }
        for (int i = 0; i < 75; i++) {
            boxes.add(new Box(28, 54, 30, 2.35));
        }
        for (int i = 0; i < 75; i++) {
            boxes.add(new Box(29, 39, 32, 20));
        }
        for (int i = 0; i < 10; i++) {
            boxes.add(new Box(10, 15, 20, 25));
        }
        for (int i = 0; i < 150; i++) {
            boxes.add(new Box(37, 42, 25, 18));
        }
        for (int i = 0; i < 3; i++) {
            boxes.add(new Box(18, 36, 18, 22));
        }
        for (int i = 0; i < 25; i++) {
            boxes.add(new Box(17, 18, 22, 22));
        }
        for (int i = 0; i < 50; i++) {
            boxes.add(new Box(17, 22, 22, 22));
        }
        for (int i = 0; i < 5; i++) {
            boxes.add(new Box(11, 12, 16, 16));
        }
        for (int i = 0; i < 20; i++) {
            boxes.add(new Box(27, 30, 40, 40));
        }
        for (int i = 0; i < 3; i++) {
            boxes.add(new Box(7, 19, 21, 21));
        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(2, 2, 2, 1));
//        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(3, 3, 3, 1));
//        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(1, 1, 1, 1));
//        }
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
//            NonDominatedSort.performNonDominatedSorting(population);
            SelectionStrategy selectionStrategy = new TournamentSelection(0.7, 2);
            List<Chromosome> selected = selectionStrategy.select(population, true, (int) (POPULATION_SIZE * 0.3), new Random());
            OrderCrossover orderCrossover = new OrderCrossover();
            InversionMutation inversionMutation = new InversionMutation();
            for (int j = 0; j < POPULATION_SIZE * 0.3; j += 1) {
                double chance = new Random().nextDouble();
                if (chance <= 0.9 && j + 1 < selected.size()) {
                    Chromosome newChromo = orderCrossover.crossover(selected.get(j), selected.get(j + 1));
                    population.add(newChromo);
                    j++;
                } else if (chance <= 0.3) {
                    population.add(inversionMutation.mutate(selected.get(j)));
                }
            }
            population.sort((c1, c2) -> (int) (c2.fitness() - c1.fitness()));
            population = new ArrayList<>(population.subList(0, POPULATION_SIZE));
            System.out.println("Volume ut: " + population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
            System.out.println("Weight: " + population.get(0).weightFitness());

        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        System.out.println("Seconds: " + duration);
        System.out.println("Volume: " + population.get(0).fitness());
        System.out.println("Weight: " + population.get(0).weightFitness());
        System.out.println("Volume ut: " + population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
        writeCoordinates(population);
    }
}