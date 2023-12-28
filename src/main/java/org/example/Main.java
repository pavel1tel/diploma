package org.example;

import org.example.ga.Chromosome;
import org.example.towerHeuristic.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.example.Util.calculateMean;
import static org.example.Util.calculateVariance;
import static org.example.ga.Crowding.crowdingStep;
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

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Double> results = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            results.add(crowding());
            System.out.println(i);
        }
        System.out.println();
        System.out.println(calculateMean(results));
        System.out.println(calculateVariance(results));

    }

    public static double crowding() {
        long startTime = System.nanoTime();
        List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        GenerateTowers generateTowers = new GenerateTowers(boxGroups);
        double result = 0;
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
        }
        List<Chromosome> population = initializePopulation(towers);

        for (int i = 0; i < 3000; i++) {
            population = crowdingStep(population, 4, 0.3, 0.9);
            System.out.println("Volume ut: " + population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
            System.out.println("Weight: " + population.get(0).weightFitness());
            if (result < population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth())) {
                result = population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth());
            }
        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        System.out.println("Seconds: " + duration);
        System.out.println("Volume: " + population.get(0).fitness());
        System.out.println("Weight: " + population.get(0).weightFitness());
        System.out.println("Volume ut: " + result);
        writeCoordinates(population);
        towers.clear();
        return result;
    }
}