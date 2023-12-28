package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.*;

import static org.example.fillingHeuristic.DummyFillingHeuristic.getTowerBase;

public class RecursiveFillingHeuristic implements FillingHeuristic {

    public static double totalWeight = 0.0;

    public List<TowerPlacement> generateSolution(Chromosome chromosome, List<Tower> towers, Container container) {
        totalWeight = 0.0;
        EmptySpace emptySpace = new EmptySpace(0, 0, container.getLength(), container.getWidth());
        ArrayList<TowerPlacement> towerPlacements = new ArrayList<>();
        HashSet<Integer> usedTowers = new HashSet<>();
        fillRecursively(emptySpace, towerPlacements, chromosome, usedTowers, towers, true);
        return towerPlacements;
    }

    public static void fillRecursively(EmptySpace emptySpace, List<TowerPlacement> towerPlacements, Chromosome chromosome, HashSet<Integer> usedTowers, List<Tower> towers, boolean flag) {
        AbstractMap.SimpleEntry<Gene, TowerBase> towerBase = pickTowerBaseFromChromo(chromosome, usedTowers, towers, emptySpace);
        if (towerBase == null) {
            return;
        }
        if (totalWeight + towerBase.getValue().getWeight() > 7200) {
            return;
        }
        TowerPlacement towerPlacement = new TowerPlacement(towerBase.getKey().getTowersIndex(), towerBase.getKey().getRotation(), emptySpace.getXcord(), emptySpace.getYcord());
        towerPlacements.add(towerPlacement);
        totalWeight += towerBase.getValue().getWeight();
        if (flag) {
            EmptySpace onSide = new EmptySpace(emptySpace.getXcord(), emptySpace.getYcord() + towerBase.getValue().getLength(), emptySpace.getLength() - towerBase.getValue().getLength(), towerBase.getValue().getDepth());
            fillRecursively(onSide, towerPlacements, chromosome, usedTowers, towers, false);
            EmptySpace inFront = new EmptySpace(emptySpace.getXcord() + towerBase.getValue().getDepth(), emptySpace.getYcord(), emptySpace.getLength(), emptySpace.getDepth() - towerBase.getValue().getDepth());
            fillRecursively(inFront, towerPlacements, chromosome, usedTowers, towers, false);
        } else {
            EmptySpace inFront = new EmptySpace(emptySpace.getXcord() + towerBase.getValue().getDepth(), emptySpace.getYcord(), emptySpace.getLength(), emptySpace.getDepth() - towerBase.getValue().getDepth());
            fillRecursively(inFront, towerPlacements, chromosome, usedTowers, towers, true);
            EmptySpace onSide = new EmptySpace(emptySpace.getXcord(), emptySpace.getYcord() + towerBase.getValue().getLength(), emptySpace.getLength() - towerBase.getValue().getLength(), towerBase.getValue().getDepth());
            fillRecursively(onSide, towerPlacements, chromosome, usedTowers, towers, true);
        }

    }

    private static AbstractMap.SimpleEntry<Gene, TowerBase> pickTowerBaseFromChromo(Chromosome chromosome, HashSet<Integer> usedTowers, List<Tower> towers, EmptySpace emptySpace) {
        for (int i = 0; i < towers.size(); i++) {
            TowerBase towerBase = getTowerBase(chromosome.getGenes().get(i), towers.get(chromosome.getGenes().get(i).getTowersIndex()));
            if (emptySpace.getLength() >= towerBase.getLength() && emptySpace.getDepth() >= towerBase.getDepth()) {
                if (!usedTowers.contains(i)) {
                    usedTowers.add(i);
                    return new AbstractMap.SimpleEntry<>(chromosome.getGenes().get(i), towerBase);
                }
            }
        }
        return null;
    }
}