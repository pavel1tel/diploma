package org.example;

import org.example.fillingHeuristic.FillingHeuristic;
import org.example.fillingHeuristic.TowerPlacement;
import org.example.ga.Chromosome;
import org.example.towerHeuristic.*;

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
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(1, 1, 1));
//        }
        for (int i = 0; i < 1000; i++) {
            boxes.add(new Box(2, 2, 2));
        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(3, 3, 3));
//        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(3, 3, 3));
//        }
//        for (int i = 0; i < 1000; i++) {
//            boxes.add(new Box(2, 2, 2));
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

    private static Chromosome getRandomIndividual(List<Chromosome> parents) {
        Random random = new Random();
        int randomIndex = random.nextInt(parents.size());
        Chromosome individual = parents.get(randomIndex);
        parents.remove(randomIndex);
        return individual;
    }

    public static void main(String[] args) {
        List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        GenerateTowers generateTowers = new GenerateTowers(boxGroups);
        ArrayList<Tower> towers = new ArrayList<>();
        while (!boxGroups.isEmpty()) {
            Tower tower = generateTowers.fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            towers.add(tower);
        }
        FillingHeuristic fillingHeuristic = new FillingHeuristic();
        List<Chromosome> population = initializePopulation(towers);
        for (Chromosome chromo : population) {
            List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(chromo, towers, container);
            double chromoTotalVolume = 0.0;
            for (TowerPlacement towerPlacement : towerPlacements){
                Tower tower = towers.get(towerPlacement.getTowerNumber());
                chromoTotalVolume += tower.getTotalVolume(tower);
            }
            System.out.println(chromoTotalVolume);
        }
    }
}