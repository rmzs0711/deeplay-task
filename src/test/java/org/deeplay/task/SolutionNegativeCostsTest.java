package org.deeplay.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolutionNegativeCostsTest extends Solution {
    private static final String raceInfoInput = "src/test/resources/race_negative_info_input.json";
    private static final String lvl1 = "STWSWTPPTPTTPWPP";
    private static final String lvl2 = "STWSWTPSTPTTPWPP";

    @BeforeAll
    static void setUp() {
        Assertions.assertDoesNotThrow(() -> Solution.setRaceInfo(raceInfoInput));
        setNegative(true);
    }

    @Test
    void fordBellmanSolutionTest2() {
        try {
            Assertions.assertEquals(10, Solution.getResult(lvl1, "Human"));
        } catch (NegativeCycleException e) {
            fail();
        }

        Assertions.assertThrows(NegativeCycleException.class, () -> Solution.getResult(lvl2, "Human"));
    }
}