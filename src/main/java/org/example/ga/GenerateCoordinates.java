package org.example.ga;

import org.example.fillingHeuristic.FillingHeuristic;
import org.example.fillingHeuristic.TowerBase;
import org.example.fillingHeuristic.TowerPlacement;
import org.example.towerHeuristic.Box;
import org.example.towerHeuristic.BoxGroup;
import org.example.towerHeuristic.BoxUtil;
import org.example.towerHeuristic.Tower;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.example.Main.*;
import static org.example.fillingHeuristic.DummyFillingHeuristic.getTowerBase;

public class GenerateCoordinates {

    static List<BoxGroup> boxGroups = BoxUtil.groupBoxesByTypes(boxes);

    public static void writeCoordinates(Chromosome chromosome) {
        boxGroups = BoxUtil.groupBoxesByTypes(boxes);
        clear("result.txt");
        FillingHeuristic fillingHeuristic = chromosome.fillingHeuristic;
        List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(chromosome, towers, container);
        for (TowerPlacement towerPlacement : towerPlacements) {
            System.out.println(towerPlacement.getTowerNumber());
            Tower tower = towers.get(towerPlacement.getTowerNumber());
            TowerBase towerbase = getTowerBase(towerPlacement.getRotation(), tower);
            boolean wasRotated = tower.getDepth() != towerbase.getDepth();
            int z = 0;
            int x = 0;
            int y = 0;
            for (Box box : tower.getBoxes()) {
                BoxGroup boxGroup = new BoxGroup(tower.getBoxes().get(0).getDepth(), tower.getBoxes().get(0).getLength(), tower.getBoxes().get(0).getHeight(), 0, 6);
                int group = boxGroups.indexOf(boxGroup) + 1;
                int length = (int) towerbase.getLength();
                int height = tower.getBoxes().get(0).getHeight();
                int depth = (int) towerbase.getDepth();
                x = (int) towerPlacement.getXcord();
                y = (int) towerPlacement.getYcord();
                writeToFIle("result.txt", length + " " + height + " " + depth + " " + x + " " + y + " " + z + " " + group);
                z += height;
            }
            if (tower.getTowerOnTop() != null) {
                writeBoxesFromTower(tower.getTowerOnTop(), x, y, z, wasRotated);

            }
        }
    }

    public static void writeBoxesFromTower(Tower tower, int x, int y, int z, boolean wasRotated) {
        if (tower == null) {
            return;
        }
        int zz = z;
        for (Box box : tower.getBoxes()) {
            BoxGroup boxGroup = new BoxGroup(box.getDepth(), box.getLength(), box.getHeight(), 0, 6);
            int group = boxGroups.indexOf(boxGroup) + 1;
            int length = box.getLength();
            int height = box.getHeight();
            int depth =  box.getDepth();
            if (wasRotated){
                length = box.getDepth();
                depth =  box.getLength();
            }
            writeToFIle("result.txt", length + " " + height + " " + depth + " " + x + " " + y + " " + zz + " " + group);
            zz += height;
        }
        if (!wasRotated) {
            if (tower.getOnFront() != null) {
                writeBoxesFromTower(tower.getOnFront(), x + tower.getBoxes().get(0).getDepth(), y, z, wasRotated);
            }
            if (tower.getOnSide() != null) {
                writeBoxesFromTower(tower.getOnSide(), x, y + tower.getBoxes().get(0).getLength(), z, wasRotated);
            }
        } else {
            if (tower.getOnFront() != null) {
                writeBoxesFromTower(tower.getOnFront(), x , y + tower.getBoxes().get(0).getDepth(), z, wasRotated);
            }
            if (tower.getOnSide() != null) {
                writeBoxesFromTower(tower.getOnSide(), x + tower.getBoxes().get(0).getLength(), y , z, wasRotated);
            }
        }
        if (tower.getTowerOnTop() != null) {
            writeBoxesFromTower(tower.getTowerOnTop(), x, y, zz, wasRotated);
        }
    }

    public static void writeToFIle(String filename, String str) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(str);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear(String filename) {
        try {
            FileWriter fw = new FileWriter(filename, false);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
