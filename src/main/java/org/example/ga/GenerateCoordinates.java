package org.example.ga;

import org.example.fillingHeuristic.FillingHeuristic;
import org.example.fillingHeuristic.TowerBase;
import org.example.fillingHeuristic.TowerPlacement;
import org.example.towerHeuristic.Box;
import org.example.towerHeuristic.Tower;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.example.Main.*;
import static org.example.fillingHeuristic.DummyFillingHeuristic.getTowerBase;

public class GenerateCoordinates {

    public static void writeCoordinates(List<Chromosome> chromosomes) {
        clear("result.txt");
        FillingHeuristic fillingHeuristic = chromosomes.get(0).fillingHeuristic;
        List<TowerPlacement> towerPlacements = fillingHeuristic.generateSolution(chromosomes.get(0), towers, container);
        for (TowerPlacement towerPlacement : towerPlacements) {
            Tower tower = towers.get(towerPlacement.getTowerNumber());
            TowerBase towerbase = getTowerBase(towerPlacement.getRotation(), tower);
            int z = 0;
            int x = 0;
            int y = 0;
            for (Box box : tower.getBoxes()) {
                int length = (int) towerbase.getLength();
                int height = tower.getBoxes().get(0).getHeight();
                int depth = (int) towerbase.getDepth();
                x = (int) towerPlacement.getXcord();
                y = (int) towerPlacement.getYcord();
                writeToFIle("result.txt", length + " " + height + " " + depth + " " + x + " " + y + " " + z);
                z += height;
            }
            if (tower.getTowerOnTop() != null){
                writeBoxesFromTower(tower.getTowerOnTop(), x, y, z);

            }
        }
    }

    public static void writeBoxesFromTower(Tower tower, int x, int y, int z){
        if(tower == null){
            return;
        }
        int zz = z;
        for (Box box : tower.getBoxes()){
            int length =  box.getLength();
            int height =  box.getHeight();
            int depth =  box.getDepth();
            writeToFIle("result.txt" ,length + " " + height + " " + depth + " " + x + " " + y + " " + zz);
            zz += height;
        }
        if (tower.getOnFront() != null){
            writeBoxesFromTower(tower.getOnFront(), x +  tower.getBoxes().get(0).getDepth(), y, z);
        }
        if (tower.getOnSide() != null){
            writeBoxesFromTower(tower.getOnSide(), x, y +  tower.getBoxes().get(0).getLength(), z);
        }
        if (tower.getTowerOnTop() != null){
            writeBoxesFromTower(tower.getTowerOnTop(), x, y, zz);
        }
    }

    public static void writeToFIle(String filename, String str) {
        try {
            FileWriter fw = new FileWriter(filename,true);
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
            FileWriter fw = new FileWriter(filename,false);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
