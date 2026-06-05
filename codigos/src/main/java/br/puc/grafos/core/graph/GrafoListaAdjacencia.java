package br.puc.grafos.core.graph; // Pacote da implementacao de grafos.

import br.puc.grafos.core.graph.exceptions.LacoInvalidoException; // Excecao para impedir laco.

import java.util.ArrayList; // Lista dinamica para vertices, predecessores e arestas.
import java.util.HashMap; // Mapa usado para guardar destino -> peso.
import java.util.List; // Interface de lista.
import java.util.Map; // Interface de mapa.

/**
 * Implementacao usando lista de adjacencia.
 *
 * <p>Cada vertice guarda um mapa de destinos e pesos. Essa estrutura e boa
 * para grafos esparsos, comuns em redes de colaboracao.</p>
 */
public class GrafoListaAdjacencia extends AbstractGraph { // Herda a API comum de AbstractGraph.

    /** adjacency.get(u).get(v) guarda o peso da aresta u -> v. */
    private final List<Map<Integer, Double>> adjacency; // Lista em que cada posicao representa um vertice.

    public GrafoListaAdjacencia(int numVertices) { // Construtor principal em portugues.
        super(numVertices); // Chama o construtor da classe abstrata.
        this.adjacency = new ArrayList<>(); // Cria a lista principal.

        for (int i = 0; i < numVertices; i++) { // Cria uma estrutura de vizinhos para cada vertice.
            adjacency.add(new HashMap<>()); // Cada HashMap guarda as arestas que saem do vertice i.
        }
    }

    @Override // Indica que estamos implementando metodo abstrato da superclasse.
    public boolean hasEdge(int source, int target) { // Verifica se existe source -> target.
        validateVertices(source, target); // Garante que origem e destino existem.
        return adjacency.get(source).containsKey(target); // Consulta se target esta no mapa de source.
    }

    @Override // Implementa a versao sem peso explicito.
    public void addEdge(int source, int target) { // Adiciona aresta com peso padrao.
        addEdge(source, target, 1.0); // Reaproveita a versao completa.
    }

    @Override // Implementa a versao ponderada.
    public void addEdge(int source, int target, double weight) { // Adiciona ou reforca aresta.
        validateVertices(source, target); // Valida os vertices.
        validateEdgeWeight(weight); // Valida o peso positivo.

        if (source == target) { // Se origem e destino sao iguais...
            throw new LacoInvalidoException(source); // ...seria laco, proibido em grafo simples.
        }

        Map<Integer, Double> neighbors = adjacency.get(source); // Pega os destinos de source.
        if (neighbors.containsKey(target)) { // Se a aresta ja existe...
            neighbors.put(target, neighbors.get(target) + weight); // ...soma o peso, nao duplica.
            return; // Sai porque nao precisa aumentar contador de arestas.
        }

        neighbors.put(target, weight); // Se nao existia, cria a aresta com peso.
        edgeCount++; // Incrementa o total de arestas reais.
    }

    @Override // Implementa remocao de aresta.
    public void removeEdge(int source, int target) { // Remove source -> target.
        requireEdge(source, target); // Exige que a aresta exista.
        adjacency.get(source).remove(target); // Remove o destino do mapa de source.
        edgeCount--; // Atualiza contador de arestas.
    }

    @Override // Implementa alteracao de peso.
    public void setEdgeWeight(int source, int target, double weight) { // Define novo peso.
        requireEdge(source, target); // A aresta precisa existir.
        validateEdgeWeight(weight); // Novo peso precisa ser positivo.
        adjacency.get(source).put(target, weight); // Substitui o peso antigo pelo novo.
    }

    @Override // Implementa consulta de peso.
    public double getEdgeWeight(int source, int target) { // Retorna peso de source -> target.
        requireEdge(source, target); // Garante existencia da aresta.
        return adjacency.get(source).get(target); // Busca peso no mapa.
    }

    @Override // Implementa grau de entrada.
    public int getVertexInDegree(int vertex) { // Conta quantas arestas chegam em vertex.
        validateVertex(vertex); // Valida o vertice consultado.
        int degree = 0; // Contador inicia em zero.

        for (Map<Integer, Double> neighbors : adjacency) { // Percorre mapa de saida de todos os vertices.
            if (neighbors.containsKey(vertex)) { // Se algum mapa aponta para vertex...
                degree++; // ...entao vertex recebeu uma aresta.
            }
        }

        return degree; // Retorna total de entradas.
    }

    @Override // Implementa grau de saida.
    public int getVertexOutDegree(int vertex) { // Conta quantas arestas saem de vertex.
        validateVertex(vertex); // Valida o vertice.
        return adjacency.get(vertex).size(); // Tamanho do mapa e o numero de sucessores.
    }

    @Override // Implementa lista de sucessores.
    public List<Integer> getSuccessors(int vertex) { // Retorna destinos alcancados por vertex.
        validateVertex(vertex); // Valida o vertice.
        return new ArrayList<>(adjacency.get(vertex).keySet()); // Copia as chaves do mapa.
    }

    @Override // Implementa lista de predecessores.
    public List<Integer> getPredecessors(int vertex) { // Retorna quem aponta para vertex.
        validateVertex(vertex); // Valida o vertice.
        List<Integer> predecessors = new ArrayList<>(); // Lista que sera preenchida.

        for (int source = 0; source < vertexCount; source++) { // Testa cada possivel origem.
            if (adjacency.get(source).containsKey(vertex)) { // Se source aponta para vertex...
                predecessors.add(source); // ...source e predecessor.
            }
        }

        return predecessors; // Retorna todos os predecessores.
    }

    @Override // Implementa iteracao por arestas.
    public List<ArestaGrafo> iterEdges() { // Devolve todas as arestas existentes.
        List<ArestaGrafo> edges = new ArrayList<>(); // Lista de saida.

        for (int source = 0; source < vertexCount; source++) { // Percorre cada origem.
            for (Map.Entry<Integer, Double> entry : adjacency.get(source).entrySet()) { // Percorre destinos/pesos.
                edges.add(new ArestaGrafo(source, entry.getKey(), entry.getValue())); // Cria objeto de leitura da aresta.
            }
        }

        return edges; // Retorna lista de arestas.
    }
}
