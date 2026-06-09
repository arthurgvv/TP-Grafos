package br.puc.grafos.arquivos;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.AdjacencyListGraph;
import br.puc.grafos.core.graph.AdjacencyMatrixGraph;

import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * Escolhe a implementacao concreta do grafo.
 */
public enum TipoRepresentacaoGrafo {
    LIST("list", AdjacencyListGraph::new),
    MATRIX("matrix", AdjacencyMatrixGraph::new);

    private final String codigo;
    private final IntFunction<AbstractGraph> factory;

    TipoRepresentacaoGrafo(String codigo, IntFunction<AbstractGraph> factory) {
        this.codigo = codigo;
        this.factory = factory;
    }

    public IntFunction<AbstractGraph> factory() {
        return factory;
    }

    public static TipoRepresentacaoGrafo fromCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(tipo -> tipo.codigo.equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Representacao invalida: " + codigo + ". Use list ou matrix."));
    }
}
