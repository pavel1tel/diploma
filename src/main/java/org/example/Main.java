package org.example;

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
//        for (int i = 0; i < 13; i++) {
//            boxes.add(new Box(91,0,  54, 1, 45, 1, 20));
//        }
//        for (int i = 0; i < 15; i++) {
//            boxes.add(new Box(105,1,  77, 1, 72, 1, 20));
//        }
//        for (int i = 0; i < 10; i++) {
//            boxes.add(new Box(79,1,  78, 1, 48, 1, 20));
//        }
//        for (int i = 0; i < 12; i++) {
//            boxes.add(new Box(109,1,  76, 1, 56, 1, 20));
//        }
//        for (int i = 0; i < 13; i++) {
//            boxes.add(new Box(48,1,  37, 1, 30, 1, 20));
//        }
//        for (int i = 0; i < 9; i++) {
//            boxes.add(new Box(44,1,  37, 1, 27, 1, 20));
//        }
//        for (int i = 0; i < 17; i++) {
//            boxes.add(new Box(79,1,  76, 1, 54, 1, 20));
//        }
//        for (int i = 0; i < 17; i++) {
//            boxes.add(new Box(116,0,  78, 0, 20, 1, 20));
//        }
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
        try (BufferedReader br = new BufferedReader(new FileReader("testData/thpack7.txt"))) {
            for (int i = 0; i < 100; i++) {
                br.readLine();
                br.readLine();
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
        double result = 0;
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
        }
        List<Chromosome> population = initializePopulation(towers);

        for (int i = 0; i < 500; i++) {
            population = crowdingStep(population, 4, 0.3, 0.9);
            //System.out.println("Volume ut: " + population.get(0).fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
            //System.out.println("Weight: " + population.get(0).weightFitness());
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
        //writeCoordinates(population);
        towers.clear();
        return result;
    }
}