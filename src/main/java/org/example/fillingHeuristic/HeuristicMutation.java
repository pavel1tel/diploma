package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;

import java.util.*;

import static org.example.Main.container;
import static org.example.Main.towers;
import static org.example.fillingHeuristic.DummyFillingHeuristic.getTowerBase;

public class HeuristicMutation {
    public Chromosome mutate(Chromosome chromosome) {
        List<List<TowerPlacement>> rows = DummyFillingHeuristic.generateRows(chromosome, towers, container);
        SortedSet<Map.Entry<Integer, Double>> sortedRows = getRowsSpaceUtilization(rows);
        Iterator<Map.Entry<Integer, Double>> iterator = sortedRows.iterator();
        LinkedHashSet<Gene> newGenes = new LinkedHashSet<>();

        for (int i = 0; i < new Random().nextInt(1, (rows.size() + 1) / 2); i++) {
            List<TowerPlacement> towerPlacements = rows.get(iterator.next().getKey());
            for (TowerPlacement towerPlacement : towerPlacements) {
                Gene gene = new Gene(towerPlacement.getTowerNumber(), towerPlacement.getRotation());
                newGenes.add(gene);
            }
        }
        List<Gene> toShuffle = new ArrayList<>();
        toShuffle.addAll(chromosome.getGenes());
        Collections.shuffle(toShuffle);
        for (Gene g:
             toShuffle) {
            int r = g.getRotation();
            newGenes.add(new Gene(g.getTowersIndex(),r ^ 1));
        }
        return new Chromosome(newGenes.stream().toList());
    }

    private SortedSet<Map.Entry<Integer, Double>> getRowsSpaceUtilization(List<List<TowerPlacement>> rows) {
        SortedSet<Map.Entry<Integer, Double>> result = new TreeSet<>(
                (e1, e2) -> (int) (e1.getValue() - e2.getValue()));
        SortedMap<Integer, Double> myMap = new TreeMap<>();
        int i = 0;
        for (List<TowerPlacement> row : rows) {
            TowerBase biggest = new TowerBase();
            for (TowerPlacement towerPlacement : row) {
                TowerBase towerBase = getTowerBase(towerPlacement.getRotation(), towers.get(towerPlacement.getTowerNumber()));
                if (biggest.getDepth() < towerBase.getDepth()) {
                    biggest = towerBase;
                }
            }
            double waste = biggest.getDepth() * container.getLength();
            for (TowerPlacement towerPlacement : row) {
                TowerBase towerBase = getTowerBase(towerPlacement.getRotation(), towers.get(towerPlacement.getTowerNumber()));
                waste -= towerBase.getDepth() * towerBase.getLength();
            }
            myMap.put(i, waste);
            i++;
        }
        result.addAll(myMap.entrySet());
        return result;
    }
}
