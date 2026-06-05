// Este arquivo existe para manter compatibilidade com o nome pedido no enunciado/PDF.
package br.puc.grafos.core.graph;

/**
 * Nome oficial em ingles mantido por seguranca da entrega.
 *
 * <p>A implementacao real esta em {@link GrafoMatrizAdjacencia}, que tem nome
 * mais intuitivo em portugues para estudo. Esta classe apenas herda tudo dela.</p>
 */
public class AdjacencyMatrixGraph extends GrafoMatrizAdjacencia {

    // Repassa a quantidade de vertices para a classe em portugues.
    public AdjacencyMatrixGraph(int numVertices) {
        // Chama o construtor de GrafoMatrizAdjacencia.
        super(numVertices);
    }
}
