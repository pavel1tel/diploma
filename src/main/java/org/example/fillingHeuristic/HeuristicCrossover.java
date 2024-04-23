package org.example.fillingHeuristic;

import org.example.ga.Chromosome;
import org.example.ga.Gene;

import java.util.*;

import static org.example.Main.container;
import static org.example.Main.towers;
import static org.example.fillingHeuristic.DummyFillingHeuristic.getTowerBase;

public class HeuristicCrossover {

    public ArrayList<Chromosome> crossover(Chromosome p1, Chromosome p2) {
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(createNewChild(p1, p2));
        chromosomes.add(createNewChild(p2, p1));
        return chromosomes;
    }

    public Chromosome createNewChild(Chromosome p1, Chromosome p2){
        List<List<TowerPlacement>> rows1 = DummyFillingHeuristic.generateRows(p1, towers, container);
        List<List<TowerPlacement>> rows2 = DummyFillingHeuristic.generateRows(p2, towers, container);
        SortedSet<Map.Entry<Integer, Double>> sortedRows1 = getRowsSpaceUtilization(rows1);
        SortedSet<Map.Entry<Integer, Double>> sortedRows2 = getRowsSpaceUtilization(rows2);
        LinkedHashSet<Gene> newGenes = new LinkedHashSet<>();
        for (int i = 0; i < sortedRows1.size() / 4; i++) {
            Integer r1 = getNextDistinctRow(newGenes, sortedRows1, rows1);
            if (r1!= null) {
                List<TowerPlacement> towerPlacements = rows1.get(r1);
                for (TowerPlacement towerPlacement : towerPlacements) {
                    Gene gene = new Gene(towerPlacement.getTowerNumber(), towerPlacement.getRotation());
                    newGenes.add(gene);
                }
            }
            Integer r2 = getNextDistinctRow(newGenes, sortedRows2, rows2);
            if (r2!= null){
                List<TowerPlacement> towerPlacements = rows2.get(r2);
                for (TowerPlacement towerPlacement : towerPlacements) {
                    Gene gene = new Gene(towerPlacement.getTowerNumber(), towerPlacement.getRotation());
                    newGenes.add(gene);
                }
            }
        }
        List<Gene> toShuffle = new ArrayList<>();
        toShuffle.addAll(p1.getGenes());
        Collections.shuffle(toShuffle);
        for (Gene g:
                toShuffle) {
            int r = g.getRotation();
            newGenes.add(new Gene(g.getTowersIndex(),r ^ 1));
        }
        return new Chromosome(newGenes.stream().toList());
    }

    private Integer getNextDistinctRow(LinkedHashSet<Gene> genes, SortedSet<Map.Entry<Integer, Double>> sortedRows, List<List<TowerPlacement>> rows2){
        Iterator<Map.Entry<Integer, Double>> iterator2 = sortedRows.iterator();
        List<Integer> towers = genes.stream().map(Gene::getTowersIndex).toList();
        Map.Entry<Integer, Double> result = null;
        while (iterator2.hasNext()){
            result = iterator2.next();
            List<TowerPlacement> towerPlacements = rows2.get(result.getKey());
            boolean res = true;
            for (TowerPlacement towerPlacement:
                 towerPlacements) {
                if (towers.contains(towerPlacement.getTowerNumber())){
                    res = false;
                    break;
                }
            }
            if (res){
                return result.getKey();
            }
        }
        return null;
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
