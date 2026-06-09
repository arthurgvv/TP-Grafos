// Este pacote guarda aplicacoes executaveis, como uma demonstracao por terminal.
package br.puc.grafos.aplicacao;

// Importa o tipo abstrato para a demo aceitar matriz e lista pelo mesmo metodo.
import br.puc.grafos.core.graph.AbstractGraph;
// Importa a implementacao por lista de adjacencia.
import br.puc.grafos.core.graph.GrafoListaAdjacencia;
// Importa a implementacao por matriz de adjacencia.
import br.puc.grafos.core.graph.GrafoMatrizAdjacencia;

// Importa List para imprimir sucessores como uma lista simples.
import java.util.List;

/**
 * Aplicacao separada que consome a API obrigatoria de grafos.
 *
 * <p>Ela e util para apresentar ao professor porque mostra que a API funciona
 * tanto com GrafoListaAdjacencia quanto com GrafoMatrizAdjacencia sem mudar o
 * codigo cliente.</p>
 */
public class DemonstracaoApi {

    // Metodo principal; e o ponto de entrada quando rodamos a classe pelo Java.
    public static void main(String[] args) {
        // Demonstra o comportamento usando lista de adjacencia com 4 vertices.
        demonstrar(new GrafoListaAdjacencia(4));
        // Demonstra o mesmo comportamento usando matriz de adjacencia com 4 vertices.
        demonstrar(new GrafoMatrizAdjacencia(4));
    }

    // Metodo auxiliar que recebe qualquer grafo que herde de AbstractGraph.
    private static void demonstrar(AbstractGraph grafo) {
        // Define o nome do vertice 0.
        grafo.setVertexLabel(0, "Arthur");
        // Define o nome do vertice 1.
        grafo.setVertexLabel(1, "Victor");
        // Define o nome do vertice 2.
        grafo.setVertexLabel(2, "Simone");
        // Define o nome do vertice 3.
        grafo.setVertexLabel(3, "Laura");

        // Cria uma aresta direcionada 0 -> 1 com peso 2.
        grafo.addEdge(0, 1, 2.0);
        // Cria uma aresta direcionada 0 -> 2 com peso 3.
        grafo.addEdge(0, 2, 3.0);
        // Cria uma aresta direcionada 1 -> 2 com peso 4.
        grafo.addEdge(1, 2, 4.0);
        // Cria uma aresta direcionada 2 -> 3 com peso 5.
        grafo.addEdge(2, 3, 5.0);
        // Cria uma aresta direcionada 3 -> 0 com peso 1.
        grafo.addEdge(3, 0, 1.0);

        // Linha em branco para separar as duas demonstracoes no terminal.
        System.out.println();
        // Mostra qual implementacao esta sendo demonstrada.
        System.out.println(grafo.getClass().getSimpleName());
        // Imprime a quantidade de vertices.
        System.out.println("Vertices: " + grafo.getVertexCount());
        // Imprime a quantidade de arestas.
        System.out.println("Arestas: " + grafo.getEdgeCount());
        // Confere se existe aresta 0 -> 1.
        System.out.println("0 -> 1 existe? " + grafo.hasEdge(0, 1));
        // Confere se 2 e sucessor de 0, isto e, se existe 0 -> 2.
        System.out.println("2 e sucessor de 0? " + grafo.isSucessor(0, 2));
        // Confere se 0 e predecessor de 1, isto e, se existe 0 -> 1.
        System.out.println("0 e predecessor de 1? " + grafo.isPredessor(1, 0));
        // Mostra quantas arestas entram no vertice 2.
        System.out.println("Grau de entrada do vertice 2: " + grafo.getVertexInDegree(2));
        // Mostra quantas arestas saem do vertice 0.
        System.out.println("Grau de saida do vertice 0: " + grafo.getVertexOutDegree(0));
        // Mostra o peso da aresta 0 -> 1.
        System.out.println("Peso da aresta 0 -> 1: " + grafo.getEdgeWeight(0, 1));
        // Mostra os vertices alcancados diretamente a partir do vertice 0.
        System.out.println("Sucessores de 0: " + List.copyOf(grafo.getSuccessors(0)));
        // Mostra se o grafo nao possui nenhuma aresta.
        System.out.println("Grafo vazio? " + grafo.isEmptyGraph());
        // Mostra se todo par direcionado de vertices possui aresta.
        System.out.println("Grafo completo? " + grafo.isCompleteGraph());
        // Mostra se o grafo e fortemente conectado.
        System.out.println("Grafo fortemente conectado? " + grafo.isConnected());
    }
}
