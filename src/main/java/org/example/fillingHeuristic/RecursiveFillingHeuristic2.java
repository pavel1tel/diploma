package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Chromosome2;
import org.example.ga.Gene;
import org.example.ga.Gene2;
import org.example.towerHeuristic.Box;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.example.Main2.boxes;
import static org.example.fillingHeuristic.DummyFillingHeuristic2.getTowerBase;

public class RecursiveFillingHeuristic2 {

    public static double totalWeight = 0.0;

    public static List<TowerPlacement> generateSolution(Chromosome2 chromosome, List<Tower> towers, Container container) {
        totalWeight = 0.0;
        EmptySpace emptySpace = new EmptySpace(0, 0, container.getLength(), container.getWidth());
        ArrayList<TowerPlacement> towerPlacements = new ArrayList<>();
        HashSet<Integer> usedTowers = new HashSet<>();
        fillRecursively(emptySpace, towerPlacements, chromosome, usedTowers, towers, true);
        return towerPlacements;
    }

    public static void fillRecursively(EmptySpace emptySpace, List<TowerPlacement> towerPlacements, Chromosome2 chromosome, HashSet<Integer> usedTowers, List<Tower> towers, boolean flag) {
        AbstractMap.SimpleEntry<Gene2, TowerBase> towerBase = pickTowerBaseFromChromo(chromosome, usedTowers, towers, emptySpace);
        if (towerBase == null) {
            return;
        }
        if (totalWeight + towerBase.getValue().getWeight() > 7200) {
            return;
        }
        TowerPlacement towerPlacement = new TowerPlacement(towerBase.getKey().getBoxIndex(), towerBase.getKey().getRotation(), emptySpace.getXcord(), emptySpace.getYcord());
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

    private static AbstractMap.SimpleEntry<Gene2, TowerBase> pickTowerBaseFromChromo(Chromosome2 chromosome, HashSet<Integer> usedTowers, List<Tower> towers, EmptySpace emptySpace) {
        for (int i = 0; i < boxes.size(); i++) {
            TowerBase towerBase = getTowerBase(chromosome.getGenes().get(i), boxes.get(chromosome.getGenes().get(i).getBoxIndex()));
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