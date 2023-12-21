package org.example.towerHeuristic;

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

    public static AbstractMap.SimpleEntry<String, Integer> getBoxGroupLongestDimension(BoxGroup boxGroup) {
        if (boxGroup.getHeight() > boxGroup.getDepth() && boxGroup.getHeight() > boxGroup.getLength()) {
            return new AbstractMap.SimpleEntry<>("height", boxGroup.getHeight());
        }
        if (boxGroup.getDepth() > boxGroup.getLength() && boxGroup.getDepth() > boxGroup.getHeight()) {
            return new AbstractMap.SimpleEntry<>("depth", boxGroup.getDepth());
        } else {
            return new AbstractMap.SimpleEntry<>("length", boxGroup.getLength());
        }
    }

    public static void rotateBoxSoLargestDimensionGoDeep(BoxGroup boxGroup, Tower tower) {
        ArrayList<String> allowedRotations = getAllowedRotations(boxGroup);
        ArrayList<AbstractMap.SimpleEntry<String, Integer>> dimensions = new ArrayList<>();
        dimensions.add(new AbstractMap.SimpleEntry<>("height", boxGroup.getHeight()));
        dimensions.add(new AbstractMap.SimpleEntry<>("depth", boxGroup.getDepth()));
        dimensions.add(new AbstractMap.SimpleEntry<>("length", boxGroup.getLength()));
        dimensions.sort(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue));
        Collections.reverse(dimensions);
        for (AbstractMap.SimpleEntry<String, Integer> dimension : dimensions) {
            if (dimension.getValue() > tower.getDepth()) {
                continue;
            }
            if (dimension.getKey().equals("height")) {
                if (allowedRotations.contains("x")) {
                    BoxUtil.rotateBox(boxGroup, "x");
                }
            }
            if (dimension.getKey().equals("length")) {
                if (allowedRotations.add("z")) {
                    BoxUtil.rotateBox(boxGroup, "z");
                }
            }
            return;
        }
        throw new RuntimeException();
    }

    public static void rotateBox(BoxGroup boxGroup, String rotationType) {
        Box box = new Box(boxGroup.getLength(), boxGroup.getDepth(), boxGroup.getHeight());
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
        for (String rotation : getAllowedRotations(boxGroup)) {
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

    public static ArrayList<String> getAllowedRotations(BoxGroup boxGroup) {
        ArrayList<String> rotations = new ArrayList<>();
        if (boxGroup.getRotations() == 2) {
            rotations.add("xx");
            rotations.add("z");
        } else if (boxGroup.getRotations() == 4) {
            rotations.add("xx");
            rotations.add("z");
            rotations.add("y");
            rotations.add("xy");
        } else if (boxGroup.getRotations() == 6) {
            rotations.add("xx");
            rotations.add("z");
            rotations.add("y");
            rotations.add("xy");
            rotations.add("z");
            rotations.add("xz");
        }
        return rotations;
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
                BoxGroup boxGroup = new BoxGroup(box.getDepth(), box.getHeight(), box.getLength(), box.getRotations());
                groupMap.put(key, boxGroup);
            }
        }
        return new ArrayList<>(groupMap.values().stream().toList());
    }

    public static boolean areBoxesEqual(BoxGroup boxGroup1, BoxGroup boxGroup2){
        for (String rotation : rotations) {
            BoxGroup copy = new BoxGroup(boxGroup2);
            rotateBox(boxGroup2, rotation);
            if (boxGroup1.getHeight() == boxGroup2.getHeight() &&
                boxGroup1.getDepth() == boxGroup2.getDepth() &&
                boxGroup1.getLength() == boxGroup2.getLength()) {
               return true;
            }
            boxGroup2.setDepth(copy.getDepth());
            boxGroup2.setHeight(copy.getHeight());
            boxGroup2.setLength(copy.getLength());
        }
        return false;
    }


    public static BoxGroup getEqualBox(BoxGroup boxGroup1, List<BoxGroup> boxGroups){
        for(BoxGroup boxGroup2 : boxGroups) {
            for (String rotation : rotations) {
                BoxGroup copy = new BoxGroup(boxGroup2);
                rotateBox(boxGroup2, rotation);
                if (boxGroup1.getHeight() == boxGroup2.getHeight() &&
                        boxGroup1.getDepth() == boxGroup2.getDepth() &&
                        boxGroup1.getLength() == boxGroup2.getLength()) {
                    return boxGroup2;
                }
                boxGroup2.setDepth(copy.getDepth());
                boxGroup2.setHeight(copy.getHeight());
                boxGroup2.setLength(copy.getLength());
            }
        }
        return null;
    }


}
