package org.example.heuristic;

import java.util.*;

public class BoxUtil {

    public static ArrayList<String> rotations = new ArrayList<>();

    static {
        rotations.add("xx");
        rotations.add("x");
        rotations.add("y");
        rotations.add("z");
        rotations.add("xy");
        rotations.add("xz");
    }

    public static AbstractMap.SimpleEntry<String, Double> getBoxGroupLongestDimension(BoxGroup boxGroup) {
        if (boxGroup.getHeight() > boxGroup.getDepth() && boxGroup.getHeight() > boxGroup.getLength()) {
            return new AbstractMap.SimpleEntry<>("height", boxGroup.getHeight());
        }
        if (boxGroup.getDepth() > boxGroup.getLength() && boxGroup.getDepth() > boxGroup.getHeight()) {
            return new AbstractMap.SimpleEntry<>("depth", boxGroup.getDepth());
        } else {
            return new AbstractMap.SimpleEntry<>("length", boxGroup.getLength());
        }
    }

    public static void rotateBoxSoLargestDimensionGoDeep(BoxGroup boxGroup, Tower tower){
        ArrayList<AbstractMap.SimpleEntry<String, Double>> dimensions = new ArrayList<>();
        dimensions.add(new AbstractMap.SimpleEntry<>("height", boxGroup.getHeight()));
        dimensions.add(new AbstractMap.SimpleEntry<>("depth", boxGroup.getDepth()));
        dimensions.add(new AbstractMap.SimpleEntry<>("length", boxGroup.getLength()));
        dimensions.sort(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue));
        Collections.reverse(dimensions);
        for (AbstractMap.SimpleEntry<String, Double> dimension : dimensions) {
            if (dimension.getValue() > tower.getDepth()){
                continue;
            }
            if (dimension.getKey().equals("height")) {
                BoxUtil.rotateBox(boxGroup, "x");
            }
            if (dimension.getKey().equals("length")) {
                BoxUtil.rotateBox(boxGroup, "z");
            }
            return;
        }
        throw new RuntimeException();
    }

    public static void rotateBox(BoxGroup boxGroup, String rotationType) {
        Box box = new Box(boxGroup.getDepth(), boxGroup.getHeight(), boxGroup.getLength());
        // length - x
        // depth  - y
        // height - z
        switch (rotationType) {
            case ("x"):
                box.setDepth(boxGroup.getHeight());
                box.setHeight(boxGroup.getDepth());
                break;
            case ("y"):
                box.setLength(boxGroup.getHeight());
                box.setHeight(boxGroup.getLength());
                break;
            case ("z"):
                box.setDepth(boxGroup.getLength());
                box.setLength(boxGroup.getDepth());
                break;
            case ("xy"):
                box.setHeight(boxGroup.getLength());
                box.setLength(boxGroup.getDepth());
                box.setDepth(boxGroup.getHeight());
                break;
            case ("xz"):
                box.setDepth(boxGroup.getLength());
                box.setLength(boxGroup.getHeight());
                box.setHeight(boxGroup.getDepth());
                break;
            case ("xx"):
                break;
        }
        boxGroup.setLength(box.getLength());
        boxGroup.setHeight(box.getHeight());
        boxGroup.setDepth(boxGroup.getDepth());
    }

    public static ArrayList<String> getBoxRotationThatFitsTower(BoxGroup boxGroup, Tower tower) {
        ArrayList<String> possibleRotations = new ArrayList<>();
        for (String rotation : rotations) {
            BoxGroup copy = new BoxGroup(boxGroup);
            rotateBox(boxGroup, rotation);
            if (checkFit(boxGroup, tower)) {
                possibleRotations.add(rotation);
            }
            boxGroup.setDepth(copy.getDepth());
            boxGroup.setHeight(copy.getHeight());
            boxGroup.setLength(copy.getLength());
        }
        return possibleRotations;
    }

    public static boolean checkFit(BoxGroup boxGroup, Tower tower) {
        return tower.getDepth() >= boxGroup.getDepth() &&
                tower.getHeight() >= boxGroup.getHeight() &&
                tower.getLength() >= boxGroup.getLength();
    }

    public static List<BoxGroup> groupBoxesByTypes(List<Box> boxes) {
        Map<String, BoxGroup> groupMap = new HashMap<>();
        for (Box box : boxes) {
            String key = box.getHeight() + "|" + box.getDepth() + "|" + box.getLength();
            if (groupMap.containsKey(key)) {
                groupMap.get(key).incrementQuantity();
            } else {
                BoxGroup boxGroup = new BoxGroup(box.getDepth(), box.getHeight(), box.getLength());
                groupMap.put(key, boxGroup);
            }
        }
        return new ArrayList<>(groupMap.values().stream().toList());
    }
}
