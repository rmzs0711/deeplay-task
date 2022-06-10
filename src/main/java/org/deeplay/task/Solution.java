package org.deeplay.task;

import com.google.gson.Gson;
import org.w3c.dom.Node;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class Solution {
    private Gson gson = new Gson();
    private HashMap<String, HashMap<String, Integer>> raceInfo;
    private static final char[][] level = new char[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];

    public Solution(String pathfile) throws IOException {
        try (var jsonReader = gson.newJsonReader(new FileReader(pathfile))) {
            this.raceInfo = gson.fromJson(jsonReader, this.raceInfo.getClass());
        }
    }

    static public int getResult(String levelDescription, String race) {
        parseLevelDescription(levelDescription);
        return findShortestPathDijkstra();
    }

    private static class GraphNode {
        private int d = Integer.MAX_VALUE;
        private final int x;
        private final int y;

        private GraphNode(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static class NodeComparator implements Comparator<GraphNode> {
        @Override
        public int compare(GraphNode first, GraphNode second) {
            if (first.d == second.d) {
                if (first.y == second.y) {
                    return first.x - second.x;
                }
                return first.y - second.y;
            }
            return first.d - second.d;
        }
    }



    private static void parseLevelDescription(String levelDescription) {
        int index = 0;
        for (var c : levelDescription.toCharArray()) {
            level[index / TaskConstants.LEVEL_HEIGHT][index % TaskConstants.LEVEL_WIDTH] = c;
        }
    }
}
