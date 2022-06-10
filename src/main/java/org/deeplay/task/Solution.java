package org.deeplay.task;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class Solution {
    private static final Gson gson = new Gson();

    protected static RaceInfo raceInfo; // Тут и дальше я заменил private на protected для удобства доступа в тестировании

    protected static String currentRace = null;
    protected static final NodeComparator comparator = new NodeComparator();

    protected static final int[][] level = new int[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];

    static public void setRaceInfo(String pathfile) throws IOException {
        try (var jsonReader = gson.newJsonReader(new FileReader(pathfile))) {
            raceInfo = gson.fromJson(jsonReader, RaceInfo.class);
        }
    }

    static public int getResult(String levelDescription, String race) {
        currentRace = race;
        parseLevelDescription(levelDescription);
        return findShortestPathDijkstra();
    }

    protected static class GraphNode {
        private int d = Integer.MAX_VALUE;
        private final int x;
        private final int y;

        protected GraphNode(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setD(int d) {
            this.d = d;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    protected static class NodeComparator implements Comparator<GraphNode> {
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

    private static int findShortestPathDijkstra() {
        int[][] dist = new int[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];
        Arrays.stream(dist).forEach(array -> Arrays.fill(array, Integer.MAX_VALUE));
        dist[0][0] = 0;
        TreeSet<GraphNode> sortedSet = new TreeSet<>(comparator);
        var start = new GraphNode(0, 0);
        start.d = 0;
        sortedSet.add(start);
        while (!sortedSet.isEmpty()) {
            var nearest = sortedSet.pollFirst();
            relax(nearest, dist, sortedSet);
        }
        return dist[TaskConstants.LEVEL_HEIGHT - 1][TaskConstants.LEVEL_WIDTH - 1];
    }

    private static void relax(GraphNode nearest, int[][] dist, TreeSet<GraphNode> sortedSet) {
        int[][] destinations = new int[2 * TaskConstants.LEVEL_DIMENSION][];

        for (int i = 0; i < TaskConstants.edges.length; i++) {
            var edge = TaskConstants.edges[i];
            var newY = edge[0] + nearest.y;
            var newX = edge[1] + nearest.x;
            if (notInBounds(newX, TaskConstants.LEVEL_WIDTH) || notInBounds(newY, TaskConstants.LEVEL_HEIGHT)) {
                continue;
            }
            destinations[i] = new int[]{newY, newX};
        }


        for (var dest : destinations) {
            if (dest == null) {
                continue;
            }
            var destNode = new GraphNode(dest[1], dest[0]);
            destNode.d = Integer.min(
                    nearest.d + level[destNode.y][destNode.x], dist[destNode.y][destNode.x]);

            var oldNode = new GraphNode(destNode.x, destNode.y);
            oldNode.d = dist[oldNode.y][oldNode.x];

            if (comparator.compare(destNode, oldNode) < 0) {
                if (oldNode.d == Integer.MAX_VALUE || sortedSet.remove(oldNode)) {
                    sortedSet.add(destNode);
                }
                dist[oldNode.y][oldNode.x] = destNode.d;
            }
        }
    }

    private static boolean notInBounds(int pos, int max) {
        return pos < 0 || pos >= max;
    }

    protected static void parseLevelDescription(String levelDescription) {
        int index = 0;
        for (var c : levelDescription.toCharArray()) {
            String place = raceInfo.getDecodeMap().get(c);
            level[index / TaskConstants.LEVEL_HEIGHT][index % TaskConstants.LEVEL_WIDTH] =
                    raceInfo.getCostMap().get(currentRace).get(place);
            index++;
        }
    }
}
