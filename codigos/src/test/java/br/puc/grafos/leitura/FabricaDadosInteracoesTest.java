package br.puc.grafos.leitura;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.AdjacencyMatrixGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FabricaDadosInteracoesTest {

    @TempDir
    Path tempDir;

    @Test
    void construirGrafoIntegradoPonderadoDeJsonGithub() throws Exception {
        Path input = sampleJson();

        DadosInteracoes data = FabricaDadosInteracoes.construir(input, TipoGrafoColaboracao.INTEGRATED);
        AbstractGraph graph = new InterpretadorGrafo(AdjacencyMatrixGraph::new).obterGrafo(data);
        Map<String, Integer> index = indexByLabel(graph);

        assertEquals(6, graph.getVertexCount());
        assertEquals(6, graph.getEdgeCount());
        assertEquals(2.0, graph.getEdgeWeight(index.get("bob"), index.get("alice")));
        assertEquals(4.0, graph.getEdgeWeight(index.get("carol"), index.get("alice")));
        assertEquals(5.0, graph.getEdgeWeight(index.get("dana"), index.get("alice")));
        assertEquals(2.0, graph.getEdgeWeight(index.get("bob"), index.get("eric")));
        assertEquals(3.0, graph.getEdgeWeight(index.get("eric"), index.get("bob")));
        assertEquals(3.0, graph.getEdgeWeight(index.get("frank"), index.get("eric")));
    }

    @Test
    void construirGrafosSeparadosDeJsonGithub() throws Exception {
        Path input = sampleJson();

        AbstractGraph comments = new InterpretadorGrafo().obterGrafo(
                FabricaDadosInteracoes.construir(input, TipoGrafoColaboracao.COMMENTS));
        AbstractGraph reviews = new InterpretadorGrafo().obterGrafo(
                FabricaDadosInteracoes.construir(input, TipoGrafoColaboracao.REVIEWS));
        AbstractGraph closed = new InterpretadorGrafo().obterGrafo(
                FabricaDadosInteracoes.construir(input, TipoGrafoColaboracao.CLOSED));

        assertEquals(2, comments.getEdgeCount());
        assertEquals(2, reviews.getEdgeCount());
        assertEquals(1, closed.getEdgeCount());
    }

    private Path sampleJson() throws Exception {
        Path input = tempDir.resolve("sample.json");
        Files.writeString(input, """
                {
                  "pullRequests": [
                    {
                      "author": {"login": "alice"},
                      "comments": [
                        {"author": {"login": "bob"}}
                      ],
                      "reviews": [
                        {"author": {"login": "carol"}}
                      ],
                      "mergedBy": {"login": "dana"}
                    }
                  ],
                  "issues": [
                    {
                      "author": {"login": "eric"},
                      "comments": [
                        {"author": {"login": "bob"}}
                      ],
                      "timelineItems": [
                        {"actor": {"login": "frank"}}
                      ]
                    }
                  ]
                }
                """);
        return input;
    }

    private Map<String, Integer> indexByLabel(AbstractGraph graph) {
        Map<String, Integer> index = new HashMap<>();
        for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
            index.put(graph.getVertexLabel(vertex), vertex);
        }
        return index;
    }
}
