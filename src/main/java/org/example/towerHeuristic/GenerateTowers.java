package org.example.towerHeuristic;

import org.example.fillingHeuristic.RecursiveFillingHeuristic;
import org.example.ga.Chromosome;

import java.util.*;

import static org.example.Main.boxes;
import static org.example.Main.container;

public class GenerateTowers {

    List<BoxGroup> boxGroups;

    public ArrayList<Tower> generateTowers() {
        ArrayList<Tower> currentTow = new ArrayList<>();
        while (!boxGroups.isEmpty()) {
            Tower tower = fillTower(new Tower(container.getWidth(), container.getHeight(), container.getLength()), true);
            currentTow.add(tower);
        }
        return currentTow;
    }

    public GenerateTowers(List<BoxGroup> boxGroups) {
        this.boxGroups = boxGroups;
    }

    public BoxGroup pickNewBoxGroup(Tower tower) {
        List<BoxGroup> copyOfBoxGroups = new ArrayList<>(boxGroups);
        int i = 0;
        while (i < boxGroups.size()) {
            BoxGroup boxGroup = copyOfBoxGroups.get(i);
            if (BoxUtil.getBoxRotationThatFitsTower(boxGroup, tower).isEmpty()) {
                i++;
                continue;
            }
            return boxGroup;
        }
        return null;
    }


    public Tower fillTower(Tower tower, boolean isBase) {
        BoxGroup boxGroup = pickNewBoxGroup(tower);
        if (boxGroup == null) {
            return null;
        }
        BoxGroup copy = new BoxGroup(boxGroup);

        //rotate box to its largest dimension be depth
        BoxUtil.rotateBoxSoLargestDimensionGoDeep(copy, tower);

        Tower upTower = new Tower(copy.getDepth(), tower.getHeight(), tower.getLength());
        tower.setTowerOnTop(upTower);
        double remainingHeight = fillTowerWithBoxGroup(copy, upTower);
        if (remainingHeight != 0) {
            Tower newTower = new Tower(upTower.getDepth(), remainingHeight, upTower.getLength());
            upTower.setTowerOnTop(fillTower(newTower, false));
        }
        if (!isBase) {
            Tower inFront = new Tower(tower.getDepth() - copy.getDepth(), tower.getHeight(), tower.getLength());
            upTower.setOnFront(fillTower(inFront, false));
            Tower onSide = new Tower(copy.getDepth(), tower.getHeight(), tower.getLength() - copy.getLength());
            upTower.setOnSide(fillTower(onSide, false));
        }
        return upTower;
    }

    public double fillTowerWithBoxGroup(BoxGroup boxGroup, Tower tower) {
        int possibleDimensions = isHeightAndLengthFeasible(boxGroup, tower);
        if (possibleDimensions == 8) {
            if (!isQuantitySufficientForColumn(boxGroup, tower)) {
                if (boxGroup.getHeight() > boxGroup.getLength()) {
                    BoxUtil.rotateBox(boxGroup, "y");
                }
                double remainingHeight = buildTowerUp(boxGroup, tower);
                tower.setHeight(tower.getHeight() - remainingHeight);
                tower.setLength(boxGroup.getLength());
                return remainingHeight;
            } else {
                if (getOptimalHeightDimension(boxGroup, tower)) {
                    BoxUtil.rotateBox(boxGroup, "y");
                }
                double remainingHeight = buildTowerUp(boxGroup, tower);
                tower.setHeight(tower.getHeight() - remainingHeight);
                tower.setLength(boxGroup.getLength());
                return remainingHeight;
            }
        } else if (possibleDimensions == 3) {
            double remainingHeight = buildTowerUp(boxGroup, tower);
            tower.setHeight(tower.getHeight() - remainingHeight);
            tower.setLength(boxGroup.getLength());
            return remainingHeight;
        } else if (possibleDimensions == 5) {
            BoxUtil.rotateBox(boxGroup, "y");
            double remainingHeight = buildTowerUp(boxGroup, tower);
            tower.setHeight(tower.getHeight() - remainingHeight);
            tower.setLength(boxGroup.getLength());
            return remainingHeight;
        }
        return 0;
    }

    public double buildTowerUp(BoxGroup group, Tower tower) {
        double towerHeight = tower.getHeight();
        while (towerHeight - group.getHeight() >= 0 && group.getRemainingQuantity() != 0) {
            towerHeight = towerHeight - group.getHeight();
            tower.getBoxes().add(new Box(group.getLength(), 0, group.getDepth(), 0, group.getHeight(), 0, group.getWeight()));
            group.setOpen(true);
            group.decrementQuantity(boxGroups);
        }
        if (group.getRemainingQuantity() == 0) {
            boxGroups.remove(group);
        }
        return towerHeight;
    }

    public int isHeightAndLengthFeasible(BoxGroup boxGroup, Tower tower) {
        int result = 0;
        ArrayList<String> rotations = BoxUtil.getAllowedRotations(boxGroup);
        if (tower.getLength() >= boxGroup.getLength() && tower.getHeight() >= boxGroup.getHeight()) {
            result += 3;
        }
        if (rotations.contains("y") && tower.getLength() >= boxGroup.getHeight() && tower.getHeight() >= boxGroup.getLength()) {
            result += 5;
        }
        return result;
    }

    public boolean isQuantitySufficientForColumn(BoxGroup boxGroup, Tower tower) {
        return tower.getHeight() < boxGroup.getHeight() * boxGroup.getRemainingQuantity() &&
                tower.getHeight() < boxGroup.getLength() * boxGroup.getRemainingQuantity();
    }

    // 0 if height of boxgroup is optimal 1 if length
    public boolean getOptimalHeightDimension(BoxGroup boxGroup, Tower tower) {
        return tower.getHeight() % boxGroup.getHeight() > tower.getHeight() % boxGroup.getLength();
    }

    public BoxGroup firstRankingCriteria(List<BoxGroup> boxGroups) {
        BoxGroup winner = boxGroups.get(0);
        double winnerSmallestDimension = Math.min(Math.min(winner.getHeight(), winner.getLength()), winner.getDepth());
        for (BoxGroup boxGroup : boxGroups) {
            double smallestDimension = Math.min(Math.min(boxGroup.getHeight(), boxGroup.getLength()), boxGroup.getDepth());
            if (smallestDimension > winnerSmallestDimension) {
                winnerSmallestDimension = smallestDimension;
                winner = boxGroup;
            }
        }
        return winner;
    }

    public BoxGroup selectOpenBoxWithBiggestQuantity(List<BoxGroup> boxGroups) {
        BoxGroup result = null;
        for (BoxGroup boxGroup : boxGroups) {
            if (boxGroup.isOpen()) {
                if (result != null) {
                    if (result.getRemainingQuantity() < boxGroup.getRemainingQuantity()) {
                        result = boxGroup;
                    }
                } else {
                    result = boxGroup;
                }
            }
        }
        return result;
    }
}
