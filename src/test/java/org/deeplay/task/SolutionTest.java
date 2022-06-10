package org.deeplay.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

class SolutionTest extends Solution {
    private static final String raceInfoInput = "src/test/resources/race_info_input.json";
    private static final String lvl1 = "STWSWTPPTPTTPWPP";

    @BeforeAll
    static void setUp() {
        Assertions.assertDoesNotThrow(() -> Solution.setRaceInfo(raceInfoInput));
    }

    @Test
    void raceInfoInitTest() {
        RaceInfo correctRaceInfo = new RaceInfo();
        final String SWAMP = "SWAMP";
        final String WATER = "WATER";
        final String FOREST = "FOREST";
        final String PLAIN = "PLAIN";

        correctRaceInfo.getDecodeMap().put('S', SWAMP);
        correctRaceInfo.getDecodeMap().put('W', WATER);
        correctRaceInfo.getDecodeMap().put('T', FOREST);
        correctRaceInfo.getDecodeMap().put('P', PLAIN);

        correctRaceInfo.getCostMap().put("Human", new HashMap<>());
        correctRaceInfo.getCostMap().put("Swamper", new HashMap<>());
        correctRaceInfo.getCostMap().put("Woodman", new HashMap<>());

        var human = correctRaceInfo.getCostMap().get("Human");
        human.put(SWAMP, 5);
        human.put(WATER, 2);
        human.put(FOREST, 3);
        human.put(PLAIN, 1);

        var swamper = correctRaceInfo.getCostMap().get("Swamper");
        swamper.put(SWAMP, 2);
        swamper.put(WATER, 2);
        swamper.put(FOREST, 5);
        swamper.put(PLAIN, 2);

        var woodman = correctRaceInfo.getCostMap().get("Woodman");
        woodman.put(SWAMP, 3);
        woodman.put(WATER, 3);
        woodman.put(FOREST, 2);
        woodman.put(PLAIN, 2);

        Assertions.assertEquals(correctRaceInfo.getCostMap(), raceInfo.getCostMap());
        Assertions.assertEquals(correctRaceInfo.getDecodeMap(), raceInfo.getDecodeMap());
    }

    @Test
    void lvlParseTest() {
        int[][] correctLvl = new int[][]{{5, 3, 2, 5}, {2, 3, 1, 1}, {3, 1, 3, 3}, {1, 2, 1, 1}};
        currentRace = "Human";
        parseLevelDescription(lvl1);

        for (int y = 0; y < TaskConstants.LEVEL_HEIGHT; y++) {
            for (int x = 0; x < TaskConstants.LEVEL_WIDTH; x++) {
                Assertions.assertEquals(correctLvl[y][x], level[y][x]);
            }
        }
    }

    @Test
    void comparatorValueTest() {
        var f = new GraphNode(0, 0);
        var s = new GraphNode(0, 0);
        f.setD(1);
        Assertions.assertTrue(comparator.compare(f, s) < 0);
        s.setD(0);
        Assertions.assertTrue(comparator.compare(f, s) > 0);
        s.setD(1);
        Assertions.assertEquals(0, comparator.compare(f, s));
    }

    @Test
    void comparatorPosTest() {
        var biggerX = new GraphNode(1, 0);
        var smallerX = new GraphNode(0, 0);
        Assertions.assertTrue(comparator.compare(biggerX, smallerX) > 0);

        var biggerY = new GraphNode(0, 1);
        var smallerY = new GraphNode(1, 0);
        Assertions.assertTrue(comparator.compare(biggerY, smallerY) > 0);

        var equal1 = new GraphNode(0, 0);
        var equal2 = new GraphNode(0, 0);
        Assertions.assertEquals(0, comparator.compare(equal1, equal2));
    }

    @Test
    void dijkstraSolutionTest1() {
        setNegative(false);
        try {
            Assertions.assertEquals(10, Solution.getResult(lvl1, "Human"));
            Assertions.assertEquals(12, Solution.getResult(lvl1, "Woodman"));
            Assertions.assertEquals(15, Solution.getResult(lvl1, "Swamper"));

            Assertions.assertEquals(6, Solution.getResult("PPPPPPPPPPPPPPPP", "Human"));
            Assertions.assertEquals(30, Solution.getResult("SSSSSSSSSSSSSSSS", "Human"));

            Assertions.assertEquals(10, Solution.getResult("SSPPSPPPPPPPPPPP", "Human"));
        } catch (NegativeCycleException exception) {
            fail();
        }
    }

    @Test
    void fordBellmanSolutionTest1() {
        setNegative(true);
        dijkstraSolutionTest1();

    }

    @Test
    void solutionStressTest() {
        /*
            Можно написать алгоритм флойда, и затем по нему проверять
            ответы из разных точек на карте друг в друга,
            но к нему затем бы тоже написать тесты на корректность...
        */
    }
}