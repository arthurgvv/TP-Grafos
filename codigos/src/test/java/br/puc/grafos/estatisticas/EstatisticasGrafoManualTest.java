package br.puc.grafos.estatisticas;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.AdjacencyListGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstatisticasGrafoManualTest {

    @TempDir
    Path tempDir;

    @Test
    void calculateCentralityAndExportMetrics() {
        AbstractGraph graph = new AdjacencyListGraph(3);
        graph.setVertexLabel(0, "a");
        graph.setVertexLabel(1, "b");
        graph.setVertexLabel(2, "c");
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        EstatisticasGrafoManual statistics = new EstatisticasGrafoManual(graph);

        assertEquals(0.5, statistics.calcularDensidade());
        assertEquals(1.0, statistics.calcularCentralidadeGrau().get(0));

        double pagerankSum = statistics.calcularPageRank(0.85, 100, 1e-6)
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        assertEquals(1.0, pagerankSum, 1e-6);

        Path nodes = tempDir.resolve("nodes.csv");
        Path graphMetrics = tempDir.resolve("graph.json");
        statistics.exportarMetricasParaCsv(nodes, graphMetrics);

        assertTrue(Files.exists(nodes));
        assertTrue(Files.exists(graphMetrics));
        Map<String, Map<Integer, Object>> metrics = statistics.calcularTodasMetricas();
        assertTrue(metrics.containsKey("pagerank"));
        assertTrue(metrics.containsKey("community"));
    }

    @Test
    void closenessIgnoresUnreachableVertices() {
        AbstractGraph graph = new AdjacencyListGraph(3);
        graph.addEdge(0, 1);

        EstatisticasGrafoManual statistics = new EstatisticasGrafoManual(graph);

        assertEquals(1.0, statistics.calcularCentralidadeProximidade().get(0));
        assertEquals(0.0, statistics.calcularCentralidadeProximidade().get(1));
        assertEquals(0.0, statistics.calcularCentralidadeProximidade().get(2));
    }
}
