package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.towerHeuristic.Container;
import org.example.towerHeuristic.Tower;

import java.util.List;

public interface FillingHeuristic {
    List<TowerPlacement> generateSolution(Chromosome chromosome, List<Tower> towers, Container container);
}
