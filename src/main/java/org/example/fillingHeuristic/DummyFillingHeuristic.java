package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.List;

public class DummyFillingHeuristic implements FillingHeuristic{

    public List<TowerPlacement> generateSolution(Chromosome chromosome, List<Tower> towers, Container container){
        List<TowerPlacement> towerPlacements = new ArrayList<>();
        double xcord = 0;
        double ycord = 0;
        double biggestDepth = 0;
        for (Gene gene: chromosome.getGenes()){
            TowerBase towerBase = getTowerBase(gene, towers.get(gene.getTowersIndex()));
            if (biggestDepth < towerBase.getDepth()){
                biggestDepth = towerBase.getDepth();
            }
            if (ycord + towerBase.getLength() > container.getLength()){
                xcord += biggestDepth;
                biggestDepth = 0;
                ycord = 0;
            }
            if (xcord + towerBase.getDepth() > container.getWidth()){
                continue;
            }
            TowerPlacement towerPlacement = new TowerPlacement(gene.getTowersIndex(), gene.getRotation(), xcord, ycord);
            ycord += towerBase.getLength();
            towerPlacements.add(towerPlacement);
        }
        return towerPlacements;
    }

    //depth - x
    //length - y
    public static TowerBase getTowerBase(Gene gene, Tower tower){
        TowerBase towerBase = new TowerBase();
        if (tower.getLength() >= tower.getDepth()){
            if (gene.getRotation() == 0){
                towerBase.setLength(tower.getLength());
                towerBase.setDepth(tower.getDepth());
            } else {
                towerBase.setLength(tower.getDepth());
                towerBase.setDepth(tower.getLength());
            }
        } else {
            if (gene.getRotation() == 0){
                towerBase.setLength(tower.getDepth());
                towerBase.setDepth(tower.getLength());
            } else {
                towerBase.setLength(tower.getLength());
                towerBase.setDepth(tower.getDepth());
            }
        }
        return towerBase;
    }
}
