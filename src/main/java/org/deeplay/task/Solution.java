package org.deeplay.task;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

public class Solution {
    private static boolean negativeCostsExist;
    private static final Gson gson = new Gson();

    protected static RaceInfo raceInfo = null; // Тут и дальше я заменил private на protected для удобства доступа в тестировании

    protected static String currentRace = null;
    protected static final NodeComparator comparator = new NodeComparator();

    protected static final int[][] level = new int[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];

    static public void setRaceInfo(String pathfile) throws IOException {
        try (var jsonReader = gson.newJsonReader(new FileReader(pathfile))) {
            raceInfo = gson.fromJson(jsonReader, RaceInfo.class);
        }
    }

    static public int getResult(String levelDescription, String race) throws NegativeCycleException {
        currentRace = race;
        parseLevelDescription(levelDescription);

        int[][] dist = new int[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];
        Arrays.stream(dist).forEach(array -> Arrays.fill(array, Integer.MAX_VALUE));
        dist[0][0] = 0;
        var start = new GraphNode(0, 0);
        start.d = 0;
        if (negativeCostsExist) {
            return findShortestPathFordBellman(start, dist);
        } else {
            return findShortestPathDijkstra(start, dist);
        }
    }

    public static boolean isNegative() {
        return negativeCostsExist;
    }

    /*
       Если нужны отрицательные ребра, выполните setNegative(true);
     */
    public static void setNegative(boolean areNegativeCostsExist) {
        Solution.negativeCostsExist = areNegativeCostsExist;
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

    private static int findShortestPathDijkstra(GraphNode start, int[][] dist) {
        TreeSet<GraphNode> sortedSet = new TreeSet<>(comparator);
        sortedSet.add(start);
        while (!sortedSet.isEmpty()) {
            var nearest = sortedSet.pollFirst();
            relaxDijkstra(nearest, dist, sortedSet);
        }
        return dist[TaskConstants.LEVEL_HEIGHT - 1][TaskConstants.LEVEL_WIDTH - 1];
    }

    private static int findShortestPathFordBellman(GraphNode start, int[][]dist) throws NegativeCycleException {
        LinkedList<GraphNode> queue = new LinkedList<>();
        queue.add(start);
        boolean[][] inQueue = new boolean[TaskConstants.LEVEL_HEIGHT][TaskConstants.LEVEL_WIDTH];
        inQueue[start.y][start.x] = true;
        int stepsCounter = 0;
        while (!queue.isEmpty()) {
            var node = queue.pollFirst();
            inQueue[node.y][node.x] = false;
            var neighbors = calcNeighbors(node);
            for (var neighbor : neighbors) {
                if (neighbor == null) {
                    continue;
                }
                var oldNode = new GraphNode(neighbor[1], neighbor[0]);
                if (dist[node.y][node.x] + level[oldNode.y][oldNode.x] < dist[oldNode.y][oldNode.x]) {
                    if (stepsCounter > TaskConstants.LEVEL_WIDTH * TaskConstants.LEVEL_HEIGHT) {
                        throw new NegativeCycleException();
                    }
                    dist[oldNode.y][oldNode.x] = dist[node.y][node.x] + level[oldNode.y][oldNode.x];
                    if (!inQueue[oldNode.y][oldNode.x]) {
                        inQueue[oldNode.y][oldNode.x] = true;
                        queue.add(oldNode);
                    }
                }
            }
            stepsCounter++;
        }
        return dist[TaskConstants.LEVEL_HEIGHT - 1][TaskConstants.LEVEL_WIDTH - 1];
    }

    protected static int[][] calcNeighbors(GraphNode start) {
        int[][] neighbors = new int[2 * TaskConstants.LEVEL_DIMENSION][];
        for (int i = 0; i < TaskConstants.edges.length; i++) {
            var edge = TaskConstants.edges[i];
            var newY = edge[0] + start.y;
            var newX = edge[1] + start.x;
            if (notInBounds(newX, TaskConstants.LEVEL_WIDTH) || notInBounds(newY, TaskConstants.LEVEL_HEIGHT)) {
                continue;
            }
            neighbors[i] = new int[]{newY, newX};
        }
        return neighbors;
    }

    private static void relaxDijkstra(GraphNode nearest, int[][] dist, TreeSet<GraphNode> sortedSet) {
        int[][] neighbors = calcNeighbors(nearest);

        for (var neighbor : neighbors) {
            if (neighbor == null) {
                continue;
            }
            var destNode = new GraphNode(neighbor[1], neighbor[0]);
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

    // так как статическая функция и хочется иметь способ все почистить
    protected void clear() {
        currentRace = null;
        raceInfo = null;
        negativeCostsExist = false;
        Arrays.stream(level).forEach(array -> Arrays.fill(array, 0));
    }
}
