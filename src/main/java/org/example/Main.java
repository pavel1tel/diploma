package org.example;

import org.example.fillingHeuristic.DummyFillingHeuristic;
import org.example.fillingHeuristic.RecursiveFillingHeuristic;
import org.example.ga.Chromosome;
import org.example.ga.GenerateCoordinates;
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
    public static final int POPULATION_SIZE = 100;
    public static List<Box> boxes = new ArrayList<>();
    public static Container container = new Container(587, 220, 233);
    public static ArrayList<Tower> towers = new ArrayList<>();


    private static List<Chromosome> initializePopulation(ArrayList<Tower> towers) {
        int size = 2000;
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Chromosome chromosome = new Chromosome();
            chromosome.generateRandomGenes(towers.size());
            population.add(chromosome);
        }
        population.sort((a, b) -> (int) (b.fitness() - a.fitness()));
        ArrayList<Chromosome> result = new ArrayList<>(population.subList(0, POPULATION_SIZE));

        return result;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Double> results = new ArrayList<>();
        for (int w = 0; w < 1; w++) {
            try (BufferedReader br = new BufferedReader(new FileReader("testData/soco.txt"))) {
                for (int i = 0; i < 10; i++) {
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
//                    GenerateCoordinates.writeToFIle("thpack1big", String.valueOf(results.get(results.size() - 1)));
                    towers.clear();
                    boxes.clear();
                }
            }
        }
        System.out.println();
        System.out.println(calculateMean(results));
        System.out.println(calculateVariance(results));

    }

    public static double crowding() {
        long startTime = System.nanoTime();
        towers = TowerGa.GAloop(200);
        List<Chromosome> population = initializePopulation(towers);
        Chromosome result = getFittest(population);

        for (int i = 0; i < 10000; i++) {
            population = crowdingStep(population, 4, 0.3, 0.9);
            if (getFittest(population).fitness() > result.fitness()) {
                result = getFittest(population);
            }
            GenerateCoordinates.writeToFIle("test", String.valueOf((result.fitness() / (container.getHeight() * container.getLength() * container.getWidth()) * 100)));
            System.out.printf("Volume ut: %.2f", (result.fitness() / (container.getHeight() * container.getLength() * container.getWidth()) * 100));
            System.out.println();
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

    public static Chromosome getFittest(List<Chromosome> chromosomes) {
        double maxFix = 0;
        Chromosome result = null;
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.fitness() > maxFix) {
                result = chromosome;
                maxFix = chromosome.fitness();
            }
        }
        return result;
    }
}