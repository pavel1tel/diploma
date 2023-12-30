package org.example;

import org.example.fillingHeuristic.DummyFillingHeuristic;
import org.example.fillingHeuristic.RecursiveFillingHeuristic;
import org.example.ga.Chromosome;
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
    public static Container container = new Container(587, 220, 233);
    public static ArrayList<Tower> towers = new ArrayList<>();


    static {
//        for (int i = 0; i < 325; i++) {
//            boxes.add(new Box(36, 40, 28, 20));
//        }
//        for (int i = 0; i < 25; i++) {
//            boxes.add(new Box(28, 54, 30, 22));
//        }
//        for (int i = 0; i < 75; i++) {
//            boxes.add(new Box(28, 54, 30, 2.35));
//        }
//        for (int i = 0; i < 75; i++) {
//            boxes.add(new Box(29, 39, 32, 20));
//        }
//        for (int i = 0; i < 10; i++) {
//            boxes.add(new Box(10, 15, 20, 25));
//        }
//        for (int i = 0; i < 150; i++) {
//            boxes.add(new Box(37, 42, 25, 18));
//        }
//        for (int i = 0; i < 3; i++) {
//            boxes.add(new Box(18, 36, 18, 22));
//        }
//        for (int i = 0; i < 25; i++) {
//            boxes.add(new Box(17, 18, 22, 22));
//        }
//        for (int i = 0; i < 50; i++) {
//            boxes.add(new Box(17, 22, 22, 22));
//        }
//        for (int i = 0; i < 5; i++) {
//            boxes.add(new Box(11, 12, 16, 16));
//        }
//        for (int i = 0; i < 20; i++) {
//            boxes.add(new Box(27, 30, 40, 40));
//        }
//        for (int i = 0; i < 3; i++) {
//            boxes.add(new Box(7, 19, 21, 21));
//        }
    }

    private static List<Chromosome> initializePopulation(ArrayList<Tower> towers) {
        int size = 5000;
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Chromosome chromosome = new Chromosome();
            chromosome.setFillingHeuristic(new RecursiveFillingHeuristic());
            chromosome.generateRandomGenes(towers.size());
            population.add(chromosome);
        }
        System.out.println("a");
        population.sort((a, b) -> (int) (b.fitness() - a.fitness()));
        System.out.println("b");
        ArrayList<Chromosome> result = new ArrayList<>(population.subList(0, POPULATION_SIZE));

        return result;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Double> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("testData/soco.txt"))) {
            for (int i = 0; i < 1; i++) {
                br.readLine();
                String[] containerParam = br.readLine().strip().split(" ");
                container = new Container(Integer.parseInt(containerParam[0]), Integer.parseInt(containerParam[2]), Integer.parseInt(containerParam[1]));
                int numOfBoxTypes = Integer.parseInt(br.readLine().strip());
                for (int j = 0; j < numOfBoxTypes; j++) {
                    String box = br.readLine().strip();
                    String[] params = box.split(" ");
                    int quantity = Integer.parseInt(params[7]);
                    for (int k = 0; k < quantity; k++) {
                        boxes.add(new Box(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]), Integer.parseInt(params[4]), Integer.parseInt(params[5]), Integer.parseInt(params[6]), 20));
                    }
                }
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
        Chromosome result = getFittest(population);

        for (int i = 0; i < 1500; i++) {
            population = crowdingStep(population, 4, 0.4, 0.9);
            if (getFittest(population).fitness() > result.fitness()) {
                result = getFittest(population);
            }
            System.out.println("Volume ut: " + population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
            System.out.println(i);
        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        System.out.println("Seconds: " + duration);
        System.out.println("Volume: " + result.fitness());
        System.out.println("Weight: " + result.weightFitness());
        System.out.println("Volume ut: " + result.fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
        writeCoordinates(result);
        return result.fitness() / (container.getHeight() * container.getLength() * container.getWidth());
    }

    public static Chromosome getFittest(List<Chromosome> chromosomes){
        double maxFix = 0;
        Chromosome result = null;
        for (Chromosome chromosome: chromosomes) {
            if (chromosome.fitness() > maxFix){
                result = chromosome;
                maxFix = chromosome.fitness();
            }
        }
        return result;
    }
}