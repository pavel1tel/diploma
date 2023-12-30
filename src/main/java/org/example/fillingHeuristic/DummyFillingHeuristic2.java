package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Chromosome2;
import org.example.ga.Gene;
import org.example.ga.Gene2;
import org.example.towerHeuristic.Box;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.List;

import static org.example.Main2.boxes;
//import static org.example.Main2.towers;

public class DummyFillingHeuristic2 {

    public List<TowerPlacement> generateSolution(Chromosome2 chromosome, List<Tower> towers, Container container) {
        List<Gene2> genesLeft = chromosome.getGenes();
        List<TowerPlacement> result = new ArrayList<>();
        boolean haveAmountChanged = true;
        int x = 0;
        int y = 0;
        while (!genesLeft.isEmpty() && haveAmountChanged) {
            ArrayList<ArrayList<TowerPlacement>> rpt = new ArrayList<>();
            int before = genesLeft.size();
            genesLeft = fill(x, y, 0, container, chromosome.getGenes(), rpt);
            int after = genesLeft.size();
            haveAmountChanged = before != after;
            result.addAll(packUp(rpt).stream().flatMap(List::stream)
                    .toList());
            x = getNewX(result);
        }
        return result;
    }

    public static List<Gene2> fill(double xcord, double ycord, double totalWeight, Container container, List<Gene2> genes, ArrayList<ArrayList<TowerPlacement>> rpt) {
        ArrayList<TowerPlacement> rowPlacements = new ArrayList<>();
        double biggestDepth = 0;
        List<Gene2> geneCopy = new ArrayList<>(genes);
        List<Gene2> workingSet = new ArrayList<>(genes);
        boolean hasAmountChanged = true;
        while (hasAmountChanged) {
            for (Gene2 gene : workingSet) {
                TowerBase towerBase = getTowerBase(gene, boxes.get(gene.getBoxIndex()));
                if (totalWeight + towerBase.getWeight() > 7200) {
                    continue;
                }
                if (biggestDepth < towerBase.getDepth()) {
                    biggestDepth = towerBase.getDepth();
                }
                if (ycord + towerBase.getLength() > container.getLength()) {
                    continue;

                }
                if (xcord + towerBase.getDepth() > container.getWidth()) {
                    continue;
                }
                TowerPlacement towerPlacement = new TowerPlacement(gene.getBoxIndex(), gene.getRotation(), xcord, ycord);
                geneCopy.remove(gene);
                ycord += towerBase.getLength();
                totalWeight += towerBase.getWeight();
                rowPlacements.add(towerPlacement);
            }
            int before = workingSet.size();
            workingSet = new ArrayList<>(geneCopy);
            int after = workingSet.size();
            hasAmountChanged = before != after;
            xcord += biggestDepth;
            biggestDepth = -1;
            ycord = 0;
            if (!rowPlacements.isEmpty()) {
                rpt.add(new ArrayList<>(rowPlacements));
                rowPlacements.clear();
            }
        }
        return geneCopy;
    }

    //depth - x
    //length - y
    public static TowerBase getTowerBase(Gene gene, Tower tower) {
        return getTowerBase(gene.getRotation(), tower);
    }

    public static TowerBase getTowerBase(Gene2 gene, Box tower) {
        return getTowerBase(gene.getRotation(), tower);
    }

    public static TowerBase getTowerBase(int rotation, Tower tower) {
        TowerBase towerBase = new TowerBase();
        if (tower.getLength() >= tower.getDepth()) {
            if (rotation == 0) {
                towerBase.setLength(tower.getLength());
                towerBase.setDepth(tower.getDepth());
            } else {
                towerBase.setLength(tower.getDepth());
                towerBase.setDepth(tower.getLength());
            }
        } else {
            if (rotation == 0) {
                towerBase.setLength(tower.getDepth());
                towerBase.setDepth(tower.getLength());
            } else {
                towerBase.setLength(tower.getLength());
                towerBase.setDepth(tower.getDepth());
            }
        }
        towerBase.setWeight(tower.getTotalWeight(tower));
        return towerBase;
    }

    public static TowerBase getTowerBase(int rotation, Box box) {
        TowerBase towerBase = new TowerBase();
        if (box.getLength() >= box.getDepth()) {
            if (rotation == 0) {
                towerBase.setLength(box.getLength());
                towerBase.setDepth(box.getDepth());
            } else {
                towerBase.setLength(box.getDepth());
                towerBase.setDepth(box.getLength());
            }
        } else {
            if (rotation == 0) {
                towerBase.setLength(box.getDepth());
                towerBase.setDepth(box.getLength());
            } else {
                towerBase.setLength(box.getLength());
                towerBase.setDepth(box.getDepth());
            }
        }
        towerBase.setWeight(1);
        return towerBase;
    }

    public static Row createRow(ArrayList<TowerPlacement> rowPlacements, int initialRowX) {
        Integer maxDepth = -1;
        ArrayList<Integer> surface = new ArrayList<>();
        for (TowerPlacement towerPlacement :
                rowPlacements) {
            Box tower = boxes.get(towerPlacement.getTowerNumber());
            TowerBase towerbase = getTowerBase(towerPlacement.getRotation(), tower);
            int shiftedDepth = (int) (initialRowX - towerPlacement.getXcord());
            if (maxDepth < towerbase.getDepth()) {
                maxDepth = (int) towerbase.getDepth();
            }
            for (int i = 0; i < towerbase.getLength(); i++) {
                surface.add((int) towerbase.getDepth() - shiftedDepth);
            }
        }
        return new Row(maxDepth, surface);
    }

    public static List<Integer> getInitialRowXs(ArrayList<ArrayList<TowerPlacement>> rpt) {
        ArrayList<Integer> result = new ArrayList<>();
        for (ArrayList<TowerPlacement> towerPlacements : rpt) {
            result.add((int) towerPlacements.get(0).getXcord());
        }
        return result;
    }

    public static Integer getNewX(List<TowerPlacement> towerPlacements) {
        int max = 0;
        for (TowerPlacement tp : towerPlacements) {
            Box tower = boxes.get(tp.getTowerNumber());
            TowerBase towerbase = getTowerBase(tp.getRotation(), tower);
            if (max < towerbase.getDepth() + tp.getXcord()) {
                max = (int) (towerbase.getDepth() + tp.getXcord());
            }
        }
        return max;
    }

    public static ArrayList<ArrayList<TowerPlacement>> packUp(ArrayList<ArrayList<TowerPlacement>> towerPlacements) {
        List<Integer> initialX = getInitialRowXs(towerPlacements);
        for (int i = 1; i < towerPlacements.size(); i++) {
            Row prevRowSurf = createRow(towerPlacements.get(i - 1), initialX.get(i - 1));
            List<TowerPlacement> currentRow = towerPlacements.get(i);
            for (TowerPlacement currRowTowerPlacement : currentRow) {
                Box tower = boxes.get(currRowTowerPlacement.getTowerNumber());
                TowerBase towerbase = getTowerBase(currRowTowerPlacement.getRotation(), tower);
                int start = (int) currRowTowerPlacement.getYcord();
                int end = (int) (currRowTowerPlacement.getYcord() + towerbase.getLength());
                int prevRowMaxDepth = -1;
                for (int j = start; j < end; j++) {
                    if (prevRowSurf.surface.size() <= j) {
                        break;
                    }
                    if (prevRowMaxDepth < prevRowSurf.surface.get(j)) {
                        prevRowMaxDepth = prevRowSurf.surface.get(j);
                    }
                }
                if (prevRowMaxDepth < prevRowSurf.getMaxDepth()) {
                    currRowTowerPlacement.setXcord(currRowTowerPlacement.getXcord() - (prevRowSurf.getMaxDepth() - prevRowMaxDepth));
                }
            }
        }
        return towerPlacements;
    }
}
