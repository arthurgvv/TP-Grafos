package br.puc.grafos.leitura;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.core.graph.AdjacencyMatrixGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Converte DadosInteracoes em uma instancia real de grafo.
 */
public class InterpretadorGrafo {

    private final IntFunction<AbstractGraph> graphFactory;

    public InterpretadorGrafo() {
        this(AdjacencyMatrixGraph::new);
    }

    public InterpretadorGrafo(IntFunction<AbstractGraph> graphFactory) {
        this.graphFactory = graphFactory;
    }

    public AbstractGraph obterGrafo(DadosInteracoes data) {
        List<String> logins = new ArrayList<>(data.autores());
        if (logins.isEmpty()) {
            throw new IllegalArgumentException("Nao ha usuarios para construir o grafo.");
        }

        AbstractGraph graph = graphFactory.apply(logins.size());
        Map<String, Integer> mapper = new HashMap<>();
        for (int index = 0; index < logins.size(); index++) {
            mapper.put(logins.get(index), index);
            graph.setVertexLabel(index, logins.get(index));
            graph.setVertexWeight(index, 1.0);
        }

        for (Map.Entry<String, Map<String, Double>> sourceEntry : data.interacoes().entrySet()) {
            Integer source = mapper.get(sourceEntry.getKey());
            if (source == null) {
                continue;
            }
            for (Map.Entry<String, Double> targetEntry : sourceEntry.getValue().entrySet()) {
                Integer target = mapper.get(targetEntry.getKey());
                if (target != null && !source.equals(target)) {
                    graph.addEdge(source, target, targetEntry.getValue());
                }
            }
        }

        return graph;
    }
}
