package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.example.Main.towers;

public class DummyFillingHeuristic implements FillingHeuristic {

    public List<TowerPlacement> generateSolution(Chromosome chromosome, List<Tower> towers, Container container) {
        List<Gene> genesLeft = chromosome.getGenes();
        List<TowerPlacement> result = new ArrayList<>();
        boolean haveAmountChanged = true;
        int x = 0;
        int y = 0;
        while (!genesLeft.isEmpty() && haveAmountChanged) {
            ArrayList<ArrayList<TowerPlacement>> rpt = new ArrayList<>();
            int before = genesLeft.size();
            genesLeft = fill(x, y, 0, container, genesLeft, rpt);
            int after = genesLeft.size();
            haveAmountChanged = before != after;
            result.addAll(packUp(rpt).stream().flatMap(List::stream)
                    .toList());
            x = getNewX(result);
        }
        return result;
    }

    public static List<Gene> fill(double xcord, double ycord, double totalWeight, Container container, List<Gene> genes, ArrayList<ArrayList<TowerPlacement>> rpt) {
        ArrayList<TowerPlacement> rowPlacements = new ArrayList<>();
        double biggestDepth = 0;
        List<Gene> geneCopy = new ArrayList<>(genes);
        List<Gene> workingSet = new ArrayList<>(genes);
        boolean hasAmountChanged = true;
        while (hasAmountChanged) {
            for (Gene gene : workingSet) {
                TowerBase towerBase = getTowerBase(gene, towers.get(gene.getTowersIndex()));
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
                TowerPlacement towerPlacement = new TowerPlacement(gene.getTowersIndex(), gene.getRotation(), xcord, ycord);
                geneCopy.remove(getIndexToRemove(geneCopy, gene));
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

    public static int getIndexToRemove(List<Gene> genes, Gene gene){
        for  (int i = 0; i < genes.size(); i++) {
            if (Objects.equals(genes.get(i).getTowersIndex(), gene.getTowersIndex())) {
                return i;
            }
        }
        return -1;
    }

    //depth - x
    //length - y
    public static TowerBase getTowerBase(Gene gene, Tower tower) {
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

    public static Row createRow(ArrayList<TowerPlacement> rowPlacements, int initialRowX) {
        Integer maxDepth = -1;
        ArrayList<Integer> surface = new ArrayList<>();
        for (TowerPlacement towerPlacement :
                rowPlacements) {
            Tower tower = towers.get(towerPlacement.getTowerNumber());
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
            Tower tower = towers.get(tp.getTowerNumber());
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
                Tower tower = towers.get(currRowTowerPlacement.getTowerNumber());
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
