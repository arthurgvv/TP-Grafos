package br.puc.grafos.arquivos;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.utilitarios.SuporteCsv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Fabrica grafos a partir dos CSVs exportados para o Gephi.
 */
public final class FabricaGrafo {

    private FabricaGrafo() {
    }

    public static AbstractGraph deGephi(Path input, TipoRepresentacaoGrafo graphType) {
        EntradaGephi entrada = resolveInput(input);
        return deGephi(entrada.edgesFile(), entrada.verticesFile(), graphType);
    }

    public static AbstractGraph deGephi(Path edgesFile, Path verticesFile, TipoRepresentacaoGrafo graphType) {
        List<Map<String, String>> vertices = SuporteCsv.lerCsv(verticesFile);
        int vertexCount = vertices.stream()
                .mapToInt(row -> Integer.parseInt(row.get("Id")))
                .max()
                .orElse(-1) + 1;

        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Arquivo de vertices vazio: " + verticesFile);
        }

        AbstractGraph graph = graphType.factory().apply(vertexCount);
        for (Map<String, String> row : vertices) {
            int id = Integer.parseInt(row.get("Id"));
            graph.setVertexLabel(id, row.getOrDefault("Label", String.valueOf(id)));
            graph.setVertexWeight(id, parseDouble(row.get("Weight"), 1.0));
        }

        for (Map<String, String> row : SuporteCsv.lerCsv(edgesFile)) {
            int source = Integer.parseInt(row.get("Source"));
            int target = Integer.parseInt(row.get("Target"));
            double weight = parseDouble(row.get("Weight"), 1.0);
            if (source != target) {
                graph.addEdge(source, target, weight);
            }
        }

        return graph;
    }

    private static EntradaGephi resolveInput(Path input) {
        if (Files.isDirectory(input)) {
            try (Stream<Path> files = Files.list(input)) {
                Path edges = files
                        .filter(path -> path.getFileName().toString().endsWith("_edges.csv"))
                        .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Arquivo *_edges.csv nao encontrado em " + input));
                String base = edges.getFileName().toString().replace("_edges.csv", "");
                Path vertices = input.resolve(base + "_vertexes.csv");
                if (!Files.exists(vertices)) {
                    throw new IllegalArgumentException("Arquivo de vertices nao encontrado: " + vertices);
                }
                return new EntradaGephi(edges, vertices);
            } catch (IOException exception) {
                throw new IllegalStateException("Nao foi possivel ler diretorio: " + input, exception);
            }
        }

        String raw = input.toString();
        Path edges = Path.of(raw + "_edges.csv");
        Path vertices = Path.of(raw + "_vertexes.csv");
        if (!Files.exists(edges) || !Files.exists(vertices)) {
            throw new IllegalArgumentException("Entrada Gephi invalida. Use um diretorio ou base sem sufixo: " + input);
        }
        return new EntradaGephi(edges, vertices);
    }

    private static double parseDouble(String value, double fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return Double.parseDouble(value);
    }

    private record EntradaGephi(
            Path edgesFile,
            Path verticesFile
    ) {
    }
}
