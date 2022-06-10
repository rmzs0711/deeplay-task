package org.deeplay.task;

import java.util.HashMap;

public class RaceInfo {
    private HashMap<String, HashMap<String, Integer>> costMap = new HashMap<>();
    private HashMap<Character, String> decodeMap = new HashMap<>();

    public HashMap<String, HashMap<String, Integer>> getCostMap() {
        return costMap;
    }

    public void setCostMap(HashMap<String, HashMap<String, Integer>> costMap) {
        this.costMap = costMap;
    }

    public HashMap<Character, String> getDecodeMap() {
        return decodeMap;
    }

    public void setDecodeMap(HashMap<Character, String> decodeMap) {
        this.decodeMap = decodeMap;
    }
}
