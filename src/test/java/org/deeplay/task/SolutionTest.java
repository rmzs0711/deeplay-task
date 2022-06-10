package org.deeplay.task;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class SolutionTest extends Solution {
    private static final String raceInfoInput = "src/test/resources/race_info_input.json";

    @BeforeAll
    static void setUp() {
        Assertions.assertDoesNotThrow(() -> Solution.setRaceInfo(raceInfoInput));
    }

    @Test
    void raceInfoInitTest() {

    }

//    @Test
    void rat() {
        Gson gson = new Gson();
        RaceInfo raceInfo = new RaceInfo();
        final String SWAMP = "SWAMP";
        final String WATER = "WATER";
        final String FOREST = "FOREST";
        final String PLAIN = "PLAIN";

        raceInfo.getDecodeMap().put('S', SWAMP);
        raceInfo.getDecodeMap().put('W', WATER);
        raceInfo.getDecodeMap().put('T', FOREST);
        raceInfo.getDecodeMap().put('P', PLAIN);

        raceInfo.getCostMap().put("HUMAN", new HashMap<>());
        raceInfo.getCostMap().put("SWAMPER", new HashMap<>());
        raceInfo.getCostMap().put("FORESTER", new HashMap<>());

        var human = raceInfo.getCostMap().get("HUMAN");
        human.put(SWAMP, 5);
        human.put(WATER, 2);
        human.put(FOREST, 3);
        human.put(PLAIN, 1);

        var swamper = raceInfo.getCostMap().get("SWAMPER");
        swamper.put(SWAMP, 2);
        swamper.put(WATER, 2);
        swamper.put(FOREST, 5);
        swamper.put(PLAIN, 2);

        var forester = raceInfo.getCostMap().get("FORESTER");
        forester.put(SWAMP, 3);
        forester.put(WATER, 3);
        forester.put(FOREST, 2);
        forester.put(PLAIN, 2);
        System.out.println(gson.toJson(raceInfo));
    }
}