package org.example;

import org.example.ga.Chromosome;
import org.example.ga.GenerateCoordinates;
import org.example.towerHeuristic.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.Util.calculateMean;
import static org.example.Util.calculateVariance;
import static org.example.ga.Crowding.crowdingStep;
import static org.example.ga.GenerateCoordinates.writeCoordinates;

public class Main {
    public static final int POPULATION_SIZE = 60;
    public static final int THREAD_SIZE = 12;
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
        for (int w = 0; w < 10; w++) {
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
                    towers = TowerGa.GAloop(200);
                    CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_SIZE);
                    ReentrantLock reentrantLock = new ReentrantLock();
                    List<Thread> threads = new ArrayList<>();
                    List<List<Chromosome>> populations = new ArrayList<>();
                    List<Double> intermidiate = new ArrayList<>();
                    AtomicReference<Double> Pm = new AtomicReference<>(0.1);
                    AtomicReference<Double> PmI = new AtomicReference<>(0.1);
                    AtomicReference<Double> Pc = new AtomicReference<>(1.0);
                    AtomicReference<Double> PcI = new AtomicReference<>(-0.1);
                    for (int j = 0; j < THREAD_SIZE; j++) {
                        populations.add(new ArrayList<>());
                    }
                    for (int j = 0; j < THREAD_SIZE; j++) {
                        int finalJ = j;
                        Thread t = new Thread(() -> {
                            try {
                                List<Chromosome> thisPop = populations.get(finalJ);
                                List<Chromosome> concPop;
                                boolean isMain = finalJ == 0;
                                if (finalJ + 1 < THREAD_SIZE) {
                                    concPop = populations.get(finalJ + 1);
                                } else {
                                    concPop = populations.get(0);
                                }
                                intermidiate.add(crowding(thisPop, cyclicBarrier, concPop, reentrantLock, isMain, Pm.get(), Pc.get()));
                            } catch (InterruptedException | BrokenBarrierException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        if (Pm.get() == 0.1){
                            PmI.updateAndGet(v -> 0.1);
                        }
                        if (Pm.get() == 0.4){
                            PmI.updateAndGet(v -> -0.1);
                        }
                        if (Pc.get() == 0.7 ){
                            PcI.updateAndGet(v -> 0.1);
                        }
                        if (Pc.get() == 1.0 ){
                            PcI.updateAndGet(v -> -0.1);
                        }
                        Pm.updateAndGet(v -> BigDecimal.valueOf(v).add(BigDecimal.valueOf(PmI.get())).doubleValue());
                        Pc.updateAndGet(v -> BigDecimal.valueOf(v).add(BigDecimal.valueOf(PcI.get())).doubleValue());
                        threads.add(t);
                    }
                    for (Thread t : threads) {
                        t.start();
                    }
                    for (Thread t : threads) {
                        t.join();
                    }
                    results.add(Collections.max(intermidiate));
                    populations.clear();
                    threads.clear();
                    intermidiate.clear();
                    GenerateCoordinates.writeToFIle("thpack7par5", String.valueOf(results.get(results.size() - 1)));
                    towers.clear();
                    boxes.clear();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
        System.out.println(calculateMean(results));
        System.out.println(calculateVariance(results));

    }

    public static double crowding(List<Chromosome> population, CyclicBarrier cyclicBarrier, List<Chromosome> concurrentPopulation, ReentrantLock reentrantLock, boolean isMain, double Pm, double Pc) throws InterruptedException, BrokenBarrierException {
        long startTime = System.nanoTime();
        population.addAll(initializePopulation(towers));
        cyclicBarrier.await();
        if (isMain) {
            cyclicBarrier.reset();
        }
        Thread.sleep(1000);
        Chromosome result = getFittest(population);

        for (int i = 0; i < 3000; i++) {
            List<Chromosome> r = crowdingStep(population, 4, Pm, Pc);
            population.clear();
            population.addAll(r);
            if (getFittest(population).fitness() > result.fitness()) {
                result = getFittest(population);
            }
            System.out.printf(Thread.currentThread().getName() + ": Volume ut: %.2f", (result.fitness() / (container.getHeight() * container.getLength() * container.getWidth()) * 100));
            System.out.println();
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i % 100 == 0) {
                int d = new Random().nextInt(3);
                cyclicBarrier.await();
                reentrantLock.lock();
                int startingSize = population.size();
                for (int j = POPULATION_SIZE / 10; j < startingSize; j++) {
                    if (j % 5 == d) {
                        Chromosome current = population.get(j);
                        Chromosome concurrent = concurrentPopulation.get(j);
                        population.add(concurrent);
                        concurrentPopulation.add(current);
                    }
                }
                for (int j = POPULATION_SIZE / 10; j < startingSize; j++) {
                    if (j % 5 == d) {
                        population.remove(j);
                        concurrentPopulation.remove(j);
                    }
                }
                reentrantLock.unlock();
                if (isMain) {
                    cyclicBarrier.reset();
                }
                Thread.sleep(100);
            }
        }
        long endTime = System.nanoTime();
        double duration = (double) (endTime - startTime) / 1000000000;
        System.out.println("Seconds: " + duration);
        System.out.println("Volume: " + result.fitness());
        System.out.println("Weight: " + result.weightFitness());
        System.out.println("Volume ut: " + result.fitness() / (container.getHeight() * container.getLength() * container.getWidth()));
        if (isMain) {
            writeCoordinates(result);
        }
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