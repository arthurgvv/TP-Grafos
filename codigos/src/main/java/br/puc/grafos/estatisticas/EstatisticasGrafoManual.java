package br.puc.grafos.estatisticas;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.ArestaGrafo;
import br.puc.grafos.utilitarios.SuporteCsv;
import br.puc.grafos.utilitarios.JsonSimples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Implementacao manual das metricas, sem bibliotecas prontas de grafos.
 */
public class EstatisticasGrafoManual extends EstatisticasGrafo {

    private final Set<Integer> nodes = new TreeSet<>();
    private final Map<Integer, Map<Integer, Double>> edges = new TreeMap<>();
    private final Map<Integer, Map<Integer, Double>> inEdges = new TreeMap<>();
    private final Map<Integer, String> vertexLabels = new TreeMap<>();

    public EstatisticasGrafoManual(AbstractGraph graph) {
        super(graph);
        loadFromGraph();
    }

    private void loadFromGraph() {
        for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
            nodes.add(vertex);
            vertexLabels.put(vertex, graph.getVertexLabel(vertex));
            edges.put(vertex, new TreeMap<>());
            inEdges.put(vertex, new TreeMap<>());
        }

        for (ArestaGrafo edge : graph.iterEdges()) {
            edges.get(edge.source()).put(edge.target(), edge.weight());
            inEdges.get(edge.target()).put(edge.source(), edge.weight());
        }
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeGrau() {
        Map<Integer, Double> centrality = new TreeMap<>();
        int n = nodes.size();
        for (int node : nodes) {
            double degree = successors(node).size() + predecessors(node).size();
            centrality.put(node, n <= 1 ? 0.0 : degree / (n - 1));
        }
        return centrality;
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeGrauEntrada() {
        Map<Integer, Double> centrality = new TreeMap<>();
        int n = nodes.size();
        for (int node : nodes) {
            centrality.put(node, n <= 1 ? 0.0 : predecessors(node).size() / (double) (n - 1));
        }
        return centrality;
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeGrauSaida() {
        Map<Integer, Double> centrality = new TreeMap<>();
        int n = nodes.size();
        for (int node : nodes) {
            centrality.put(node, n <= 1 ? 0.0 : successors(node).size() / (double) (n - 1));
        }
        return centrality;
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeIntermediacao() {
        Map<Integer, Double> betweenness = zeros();

        for (int source : nodes) {
            Deque<Integer> stack = new ArrayDeque<>();
            Map<Integer, List<Integer>> predecessors = new TreeMap<>();
            Map<Integer, Integer> distance = new TreeMap<>();
            Map<Integer, Double> sigma = new TreeMap<>();

            for (int node : nodes) {
                predecessors.put(node, new ArrayList<>());
                distance.put(node, -1);
                sigma.put(node, 0.0);
            }
            distance.put(source, 0);
            sigma.put(source, 1.0);

            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(source);
            while (!queue.isEmpty()) {
                int current = queue.remove();
                stack.push(current);
                for (int neighbor : successors(current)) {
                    if (distance.get(neighbor) < 0) {
                        queue.add(neighbor);
                        distance.put(neighbor, distance.get(current) + 1);
                    }
                    if (distance.get(neighbor) == distance.get(current) + 1) {
                        sigma.put(neighbor, sigma.get(neighbor) + sigma.get(current));
                        predecessors.get(neighbor).add(current);
                    }
                }
            }

            Map<Integer, Double> dependency = zeros();
            while (!stack.isEmpty()) {
                int node = stack.pop();
                for (int predecessor : predecessors.get(node)) {
                    if (sigma.get(node) > 0.0) {
                        double ratio = sigma.get(predecessor) / sigma.get(node);
                        dependency.put(predecessor, dependency.get(predecessor) + ratio * (1.0 + dependency.get(node)));
                    }
                }
                if (node != source) {
                    betweenness.put(node, betweenness.get(node) + dependency.get(node));
                }
            }
        }

        int n = nodes.size();
        if (n > 2) {
            double scale = 1.0 / ((n - 1.0) * (n - 2.0));
            for (int node : nodes) {
                betweenness.put(node, betweenness.get(node) * scale);
            }
        }
        return betweenness;
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeProximidade() {
        Map<Integer, Double> closeness = new TreeMap<>();
        for (int node : nodes) {
            Map<Integer, Integer> distances = shortestDistancesFrom(node);
            int reachable = 0;
            int totalDistance = 0;
            for (int distance : distances.values()) {
                if (distance != Integer.MAX_VALUE && distance > 0) {
                    reachable++;
                    totalDistance += distance;
                }
            }
            closeness.put(node, totalDistance == 0 ? 0.0 : reachable / (double) totalDistance);
        }
        return closeness;
    }

    @Override
    public Map<Integer, Double> calcularPageRank(double alpha, int maxIterations, double tolerance) {
        int n = nodes.size();
        if (n == 0) {
            return Map.of();
        }

        Map<Integer, Double> rank = new TreeMap<>();
        for (int node : nodes) {
            rank.put(node, 1.0 / n);
        }

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Map<Integer, Double> next = new TreeMap<>();
            double danglingRank = 0.0;
            for (int node : nodes) {
                if (successors(node).isEmpty()) {
                    danglingRank += rank.get(node);
                }
            }

            double difference = 0.0;
            for (int node : nodes) {
                double incomingRank = 0.0;
                for (int predecessor : predecessors(node)) {
                    double totalOutWeight = totalOutWeight(predecessor);
                    if (totalOutWeight > 0.0) {
                        incomingRank += rank.get(predecessor) * edgeWeight(predecessor, node) / totalOutWeight;
                    }
                }
                double value = (1.0 - alpha) / n + alpha * (incomingRank + danglingRank / n);
                next.put(node, value);
                difference += Math.abs(value - rank.get(node));
            }

            rank = next;
            if (difference < tolerance) {
                break;
            }
        }

        return rank;
    }

    @Override
    public Map<Integer, Double> calcularCentralidadeAutovetor(int maxIterations, double tolerance) {
        int n = nodes.size();
        if (n == 0) {
            return Map.of();
        }

        Map<Integer, Double> centrality = new TreeMap<>();
        for (int node : nodes) {
            centrality.put(node, 1.0 / n);
        }

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Map<Integer, Double> next = zeros();
            for (int node : nodes) {
                for (int predecessor : predecessors(node)) {
                    next.put(node, next.get(node) + edgeWeight(predecessor, node) * centrality.get(predecessor));
                }
            }

            double norm = Math.sqrt(next.values().stream().mapToDouble(value -> value * value).sum());
            if (norm > 0.0) {
                for (int node : nodes) {
                    next.put(node, next.get(node) / norm);
                }
            }

            double difference = 0.0;
            for (int node : nodes) {
                difference += Math.abs(next.get(node) - centrality.get(node));
            }
            centrality = next;
            if (difference < tolerance) {
                break;
            }
        }

        return centrality;
    }

    @Override
    public double calcularDensidade() {
        int n = nodes.size();
        if (n <= 1) {
            return 0.0;
        }
        return graph.getEdgeCount() / (double) (n * (n - 1));
    }

    @Override
    public Map<Integer, Double> calcularCoeficienteAglomeracao() {
        Map<Integer, Double> clustering = new TreeMap<>();
        for (int node : nodes) {
            List<Integer> neighbors = new ArrayList<>(undirectedNeighbors(node));
            int k = neighbors.size();
            if (k < 2) {
                clustering.put(node, 0.0);
                continue;
            }

            int edgesBetweenNeighbors = 0;
            for (int i = 0; i < neighbors.size(); i++) {
                for (int j = i + 1; j < neighbors.size(); j++) {
                    int first = neighbors.get(i);
                    int second = neighbors.get(j);
                    if (hasAnyDirection(first, second)) {
                        edgesBetweenNeighbors++;
                    }
                }
            }

            double possibleEdges = k * (k - 1) / 2.0;
            clustering.put(node, edgesBetweenNeighbors / possibleEdges);
        }
        return clustering;
    }

    @Override
    public double calcularAglomeracaoMedia() {
        Map<Integer, Double> clustering = calcularCoeficienteAglomeracao();
        return clustering.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    @Override
    public double calcularAssortatividade() {
        List<int[]> pairs = new ArrayList<>();
        Map<Integer, Integer> degrees = new TreeMap<>();
        for (int node : nodes) {
            degrees.put(node, undirectedNeighbors(node).size());
        }

        for (int source : nodes) {
            for (int target : successors(source)) {
                pairs.add(new int[]{degrees.get(source), degrees.get(target)});
                pairs.add(new int[]{degrees.get(target), degrees.get(source)});
            }
        }

        if (pairs.isEmpty()) {
            return 0.0;
        }

        double m = pairs.size();
        double sumJk = 0.0;
        double sumJ = 0.0;
        double sumK = 0.0;
        double sumJ2 = 0.0;
        double sumK2 = 0.0;
        for (int[] pair : pairs) {
            double j = pair[0];
            double k = pair[1];
            sumJk += j * k;
            sumJ += j;
            sumK += k;
            sumJ2 += j * j;
            sumK2 += k * k;
        }

        double numerator = sumJk / m - (sumJ / m) * (sumK / m);
        double denominatorJ = sumJ2 / m - Math.pow(sumJ / m, 2);
        double denominatorK = sumK2 / m - Math.pow(sumK / m, 2);
        double denominator = denominatorJ * denominatorK;
        return denominator > 0.0 ? numerator / Math.sqrt(denominator) : 0.0;
    }

    @Override
    public Map<Integer, Integer> detectarComunidades() {
        Map<Integer, Integer> community = new TreeMap<>();
        Map<Integer, Double> nodeDegree = new TreeMap<>();
        Map<Integer, Double> communityDegree = new TreeMap<>();
        double totalWeight = totalEdgeWeight();

        for (int node : nodes) {
            community.put(node, node);
            double degree = weightedDegree(node);
            nodeDegree.put(node, degree);
            communityDegree.put(node, degree);
        }

        if (totalWeight == 0.0) {
            return normalizeCommunities(community);
        }

        for (int iteration = 0; iteration < 50; iteration++) {
            boolean improved = false;
            for (int node : nodes) {
                int currentCommunity = community.get(node);
                int bestCommunity = currentCommunity;
                double bestGain = 0.0;

                for (int candidate : neighborCommunities(node, community)) {
                    if (candidate == currentCommunity) {
                        continue;
                    }
                    double gain = modularityGain(
                            node,
                            candidate,
                            community,
                            nodeDegree,
                            communityDegree,
                            totalWeight
                    );
                    if (gain > bestGain) {
                        bestGain = gain;
                        bestCommunity = candidate;
                    }
                }

                if (bestCommunity != currentCommunity && bestGain > 1e-6) {
                    communityDegree.put(currentCommunity, communityDegree.getOrDefault(currentCommunity, 0.0) - nodeDegree.get(node));
                    communityDegree.put(bestCommunity, communityDegree.getOrDefault(bestCommunity, 0.0) + nodeDegree.get(node));
                    community.put(node, bestCommunity);
                    improved = true;
                }
            }
            if (!improved) {
                break;
            }
        }

        return normalizeCommunities(community);
    }

    @Override
    public double calcularModularidade() {
        Map<Integer, Integer> communities = detectarComunidades();
        double totalWeight = totalEdgeWeight();
        if (totalWeight == 0.0) {
            return 0.0;
        }

        double modularity = 0.0;
        for (int nodeI : nodes) {
            for (int nodeJ : nodes) {
                if (!communities.get(nodeI).equals(communities.get(nodeJ))) {
                    continue;
                }
                double adjacency = edgeWeight(nodeI, nodeJ) + edgeWeight(nodeJ, nodeI);
                modularity += adjacency - (weightedDegree(nodeI) * weightedDegree(nodeJ)) / (2.0 * totalWeight);
            }
        }

        return modularity / (2.0 * totalWeight);
    }

    @Override
    public Map<Integer, Boolean> identificarVerticesPonte() {
        Map<Integer, Integer> communities = detectarComunidades();
        Map<Integer, Boolean> bridging = new TreeMap<>();
        for (int node : nodes) {
            boolean bridge = false;
            for (int neighbor : undirectedNeighbors(node)) {
                if (!communities.get(node).equals(communities.get(neighbor))) {
                    bridge = true;
                    break;
                }
            }
            bridging.put(node, bridge);
        }
        return bridging;
    }

    @Override
    public Map<String, Map<Integer, Object>> calcularTodasMetricas() {
        Map<String, Map<Integer, Object>> metrics = new LinkedHashMap<>();
        metrics.put("degree_centrality", objectMap(calcularCentralidadeGrau()));
        metrics.put("in_degree_centrality", objectMap(calcularCentralidadeGrauEntrada()));
        metrics.put("out_degree_centrality", objectMap(calcularCentralidadeGrauSaida()));
        metrics.put("betweenness_centrality", objectMap(calcularCentralidadeIntermediacao()));
        metrics.put("closeness_centrality", objectMap(calcularCentralidadeProximidade()));
        metrics.put("pagerank", objectMap(calcularPageRank(0.85, 100, 1e-6)));
        metrics.put("eigenvector_centrality", objectMap(calcularCentralidadeAutovetor(100, 1e-6)));
        metrics.put("clustering_coefficient", objectMap(calcularCoeficienteAglomeracao()));
        metrics.put("community", objectMap(detectarComunidades()));
        metrics.put("bridging_node", objectMap(identificarVerticesPonte()));
        return metrics;
    }

    @Override
    public void exportarMetricasParaCsv(Path nodesOutputFile, Path graphOutputFile) {
        try {
            Path nodesParent = nodesOutputFile.getParent();
            if (nodesParent != null) {
                Files.createDirectories(nodesParent);
            }
            Path graphParent = graphOutputFile.getParent();
            if (graphParent != null) {
                Files.createDirectories(graphParent);
            }

            Map<String, Map<Integer, Object>> metrics = calcularTodasMetricas();
            List<String> metricNames = new ArrayList<>(metrics.keySet());
            metricNames.sort(Comparator.naturalOrder());

            List<String> lines = new ArrayList<>();
            lines.add("Id,Label," + String.join(",", metricNames));
            for (int node : nodes) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(node));
                row.add(SuporteCsv.escapar(vertexLabels.getOrDefault(node, "Node" + node)));
                for (String metricName : metricNames) {
                    Object value = metrics.get(metricName).getOrDefault(node, 0.0);
                    row.add(value instanceof Boolean bool ? (bool ? "1" : "0") : String.valueOf(value));
                }
                lines.add(String.join(",", row));
            }
            Files.write(nodesOutputFile, lines, StandardCharsets.UTF_8);

            Map<String, Object> graphMetrics = new LinkedHashMap<>();
            graphMetrics.put("density", calcularDensidade());
            graphMetrics.put("average_clustering", calcularAglomeracaoMedia());
            graphMetrics.put("assortativity", calcularAssortatividade());
            graphMetrics.put("modularity", calcularModularidade());
            JsonSimples.escreverArquivo(graphOutputFile, graphMetrics);
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel exportar metricas.", exception);
        }
    }

    private Map<Integer, Integer> shortestDistancesFrom(int source) {
        Map<Integer, Integer> distances = new TreeMap<>();
        for (int node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            int current = queue.remove();
            for (int neighbor : successors(current)) {
                if (distances.get(neighbor) == Integer.MAX_VALUE) {
                    distances.put(neighbor, distances.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }
        return distances;
    }

    private Set<Integer> successors(int node) {
        return edges.getOrDefault(node, Map.of()).keySet();
    }

    private Set<Integer> predecessors(int node) {
        return inEdges.getOrDefault(node, Map.of()).keySet();
    }

    private Set<Integer> undirectedNeighbors(int node) {
        Set<Integer> neighbors = new HashSet<>(successors(node));
        neighbors.addAll(predecessors(node));
        return neighbors;
    }

    private boolean hasAnyDirection(int first, int second) {
        return edgeWeight(first, second) > 0.0 || edgeWeight(second, first) > 0.0;
    }

    private double edgeWeight(int source, int target) {
        return edges.getOrDefault(source, Map.of()).getOrDefault(target, 0.0);
    }

    private double totalOutWeight(int node) {
        return edges.getOrDefault(node, Map.of()).values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private double weightedDegree(int node) {
        double out = edges.getOrDefault(node, Map.of()).values().stream().mapToDouble(Double::doubleValue).sum();
        double in = inEdges.getOrDefault(node, Map.of()).values().stream().mapToDouble(Double::doubleValue).sum();
        return out + in;
    }

    private double totalEdgeWeight() {
        return edges.values().stream()
                .flatMap(map -> map.values().stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private Set<Integer> neighborCommunities(int node, Map<Integer, Integer> community) {
        Set<Integer> communities = new TreeSet<>();
        for (int neighbor : undirectedNeighbors(node)) {
            communities.add(community.get(neighbor));
        }
        return communities;
    }

    private double modularityGain(
            int node,
            int targetCommunity,
            Map<Integer, Integer> community,
            Map<Integer, Double> nodeDegree,
            Map<Integer, Double> communityDegree,
            double totalWeight
    ) {
        double internalWeight = 0.0;
        for (int neighbor : undirectedNeighbors(node)) {
            if (community.get(neighbor) == targetCommunity) {
                internalWeight += edgeWeight(node, neighbor) + edgeWeight(neighbor, node);
            }
        }

        double sigmaTotal = communityDegree.getOrDefault(targetCommunity, 0.0);
        double k = nodeDegree.get(node);
        return internalWeight / totalWeight - (sigmaTotal * k) / (2.0 * totalWeight * totalWeight);
    }

    private Map<Integer, Integer> normalizeCommunities(Map<Integer, Integer> community) {
        Map<Integer, Integer> normalizedIds = new TreeMap<>();
        int nextId = 0;
        Map<Integer, Integer> normalized = new TreeMap<>();
        for (int node : nodes) {
            int raw = community.get(node);
            if (!normalizedIds.containsKey(raw)) {
                normalizedIds.put(raw, nextId++);
            }
            normalized.put(node, normalizedIds.get(raw));
        }
        return normalized;
    }

    private Map<Integer, Double> zeros() {
        Map<Integer, Double> values = new TreeMap<>();
        for (int node : nodes) {
            values.put(node, 0.0);
        }
        return values;
    }

    private Map<Integer, Object> objectMap(Map<Integer, ?> values) {
        Map<Integer, Object> objects = new TreeMap<>();
        for (Map.Entry<Integer, ?> entry : values.entrySet()) {
            objects.put(entry.getKey(), entry.getValue());
        }
        return objects;
    }
}
