package org.example;

import org.example.ga.Chromosome;
import org.example.ga.Chromosome2;
import org.example.towerHeuristic.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            boxes.add(new Box(36, 0, 40, 0, 28, 1, 20));
        }
        for (int i = 0; i < 25; i++) {
            boxes.add(new Box(28, 0, 54, 0, 30, 1, 22));
        }
        for (int i = 0; i < 75; i++) {
            boxes.add(new Box(28, 0, 54, 0, 30, 1, 2.35));
        }
        for (int i = 0; i < 75; i++) {
            boxes.add(new Box(29, 0, 39, 0, 32, 1, 20));
        }
        for (int i = 0; i < 10; i++) {
            boxes.add(new Box(10, 0, 15, 0, 20, 1, 25));
        }
        for (int i = 0; i < 150; i++) {
            boxes.add(new Box(37, 0, 42, 0, 25, 1, 18));
        }
        for (int i = 0; i < 3; i++) {
            boxes.add(new Box(18, 0, 36, 0, 18, 1, 22));
        }
        for (int i = 0; i < 25; i++) {
            boxes.add(new Box(17, 0, 18, 0, 22, 1, 22));
        }
        for (int i = 0; i < 50; i++) {
            boxes.add(new Box(17, 0, 22, 0, 22, 1, 22));
        }
        for (int i = 0; i < 5; i++) {
            boxes.add(new Box(11, 0, 12, 0, 16, 1, 16));
        }
        for (int i = 0; i < 20; i++) {
            boxes.add(new Box(27, 0, 30, 0, 40, 1, 40));
        }
        for (int i = 0; i < 3; i++) {
            boxes.add(new Box(7, 0, 19, 0, 21, 1, 21));
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

    public static void main(String[] args) throws IOException {
        ArrayList<Double> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("testData/thpack1.txt"))) {
            for (int i = 0; i < 1; i++) {
//                br.readLine();
//                br.readLine();
//                int numOfBoxTypes = Integer.parseInt(br.readLine().strip());
//                for (int j = 0; j < numOfBoxTypes; j++) {
//                    String box = br.readLine().strip();
//                    String[] params = box.split(" ");
//                    int quantity = Integer.parseInt(params[7]);
//                    for (int k = 0; k < quantity; k++) {
//                        boxes.add(new Box(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]), Integer.parseInt(params[6]), 20));
//                    }
//                }
                results.add(crowding());
                towers.clear();
                boxes.clear();
                System.out.println(i);
            }
        }
        System.out.println();
        System.out.println(calculateMean(results));
        System.out.println(calculateVariance(results));

    }

    public static double crowding() {
        long startTime = System.nanoTime();
        List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        GenerateTowers generateTowers = new GenerateTowers(boxGroups);
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
        }
        List<Chromosome> population = initializePopulation(towers);
        List<Chromosome> result = new ArrayList<>(population);

        for (int i = 0; i < 500; i++) {
            population = crowdingStep(population, 4, 0.3, 0.9);
            System.out.println("Floor ut: " + population.get(0).fitness() / (container.getLength() * container.getWidth() * container.getHeight()));
            System.out.println(i);
            if (result.get(0).fitness() < population.get(0).fitness()) {
                result = new ArrayList<>(population);
            }
        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        population.sort((a, b) -> (int) (b.fitness() - a.fitness()));
        System.out.println("Seconds: " + duration);
        System.out.println("Volume: " + result.get(0).fitness());
        System.out.println("Weight: " + result.get(0).weightFitness());
        System.out.println("Volume ut: " + result.get(0).fitness() /  (container.getLength() * container.getWidth() * container.getHeight()));
        writeCoordinates(population);
        towers.clear();
        return result.get(0).fitness() /  (container.getLength() * container.getWidth() * container.getHeight());
    }
}