package br.puc.grafos.arquivos;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.AdjacencyListGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabricaGrafoTest {

    @TempDir
    Path tempDir;

    @Test
    void exportAndLoadGephiTables() {
        AbstractGraph graph = new AdjacencyListGraph(3);
        graph.setVertexLabel(0, "Arthur");
        graph.setVertexLabel(1, "Victor");
        graph.setVertexLabel(2, "Laura");
        graph.addEdge(0, 1, 2.5);
        graph.addEdge(1, 2, 4.0);

        Path outputDir = tempDir.resolve("node").resolve("integrated");
        ArquivosGephi files = ExportadorCsvGephi.exportar(graph, outputDir);

        assertTrue(Files.exists(files.verticesFile()));
        assertTrue(Files.exists(files.edgesFile()));

        AbstractGraph loaded = FabricaGrafo.deGephi(outputDir, TipoRepresentacaoGrafo.LIST);
        assertEquals(3, loaded.getVertexCount());
        assertEquals(2, loaded.getEdgeCount());
        assertEquals("Arthur", loaded.getVertexLabel(0));
        assertEquals(2.5, loaded.getEdgeWeight(0, 1));
        assertEquals(4.0, loaded.getEdgeWeight(1, 2));
    }
}
