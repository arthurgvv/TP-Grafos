package br.puc.grafos.arquivos;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.ArestaGrafo;
import br.puc.grafos.utilitarios.SuporteCsv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta o grafo no formato de duas tabelas usado pelo projeto-base.
 */
public final class ExportadorCsvGephi {

    private ExportadorCsvGephi() {
    }

    public static ArquivosGephi exportar(AbstractGraph graph, Path path) {
        Path baseFile = resolveBaseFile(path);
        Path parent = baseFile.getParent();
        Path verticesFile = parent.resolve(baseFileName(baseFile) + "_vertexes.csv");
        Path edgesFile = parent.resolve(baseFileName(baseFile) + "_edges.csv");

        try {
            Files.createDirectories(parent);
            Files.write(verticesFile, vertexLines(graph), StandardCharsets.UTF_8);
            Files.write(edgesFile, edgeLines(graph), StandardCharsets.UTF_8);
            return new ArquivosGephi(verticesFile, edgesFile);
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel exportar CSVs para Gephi.", exception);
        }
    }

    private static Path resolveBaseFile(Path path) {
        String fileName = path.getFileName() == null ? "" : path.getFileName().toString();
        if (fileName.contains(".")) {
            return path;
        }
        return path.resolve("graph.csv");
    }

    private static String baseFileName(Path path) {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(0, dot) : fileName;
    }

    private static List<String> vertexLines(AbstractGraph graph) {
        List<String> lines = new ArrayList<>();
        lines.add("Id,Label,Weight");
        for (int vertex = 0; vertex < graph.getVertexCount(); vertex++) {
            lines.add(vertex
                    + "," + SuporteCsv.escapar(graph.getVertexLabel(vertex))
                    + "," + graph.getVertexWeight(vertex));
        }
        return lines;
    }

    private static List<String> edgeLines(AbstractGraph graph) {
        List<String> lines = new ArrayList<>();
        lines.add("Source,Target,Weight,Type");
        for (ArestaGrafo edge : graph.iterEdges()) {
            lines.add(edge.source() + "," + edge.target() + "," + edge.weight() + ",Directed");
        }
        return lines;
    }
}
