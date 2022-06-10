package org.deeplay.task;

import java.util.HashMap;

public class RaceInfo {
    // Раса -> Местность -> Стоимость
    private HashMap<String, HashMap<String, Integer>> costMap = new HashMap<>();
    // Эта мапа на случай если хочется оставить названия местностей,
    // но кодировки сделать более хитрыми, например числами
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
