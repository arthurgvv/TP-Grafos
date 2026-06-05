package br.puc.grafos.core.graph; // Pacote da implementacao de grafos.

import br.puc.grafos.core.graph.exceptions.LacoInvalidoException; // Excecao para laco.

import java.util.ArrayList; // Lista dinamica para retornos.
import java.util.List; // Interface de lista.

/**
 * Implementacao usando matriz de adjacencia.
 *
 * <p>A celula matrix[u][v] guarda o peso da aresta u -> v. Valor zero indica
 * ausencia de aresta.</p>
 */
public class GrafoMatrizAdjacencia extends AbstractGraph { // Implementacao concreta com matriz.

    /** Matriz de pesos das arestas. */
    private final double[][] matrix; // matrix[origem][destino] = peso.

    public GrafoMatrizAdjacencia(int numVertices) { // Construtor principal em portugues.
        super(numVertices); // Inicializa campos comuns da classe abstrata.
        this.matrix = new double[numVertices][numVertices]; // Cria matriz V x V preenchida com 0.0.
    }

    @Override // Implementa consulta de aresta.
    public boolean hasEdge(int source, int target) { // Verifica se source -> target existe.
        validateVertices(source, target); // Valida os dois vertices.
        return matrix[source][target] > 0.0; // Peso maior que zero significa aresta existente.
    }

    @Override // Implementa addEdge sem peso.
    public void addEdge(int source, int target) { // Adiciona aresta com peso padrao.
        addEdge(source, target, 1.0); // Chama a versao completa.
    }

    @Override // Implementa addEdge com peso.
    public void addEdge(int source, int target, double weight) { // Cria ou reforca aresta.
        validateVertices(source, target); // Valida origem e destino.
        validateEdgeWeight(weight); // Peso precisa ser positivo.

        if (source == target) { // Se origem igual destino...
            throw new LacoInvalidoException(source); // ...seria laco, proibido.
        }

        if (matrix[source][target] == 0.0) { // Se a celula estava vazia...
            edgeCount++; // ...agora uma nova aresta sera criada.
        }

        matrix[source][target] += weight; // Soma peso para acumular interacoes repetidas.
    }

    @Override // Implementa remocao de aresta.
    public void removeEdge(int source, int target) { // Remove source -> target.
        requireEdge(source, target); // Garante que a aresta existe.
        matrix[source][target] = 0.0; // Zera a celula, indicando ausencia de aresta.
        edgeCount--; // Atualiza total de arestas.
    }

    @Override // Implementa troca de peso.
    public void setEdgeWeight(int source, int target, double weight) { // Define novo peso.
        requireEdge(source, target); // Exige aresta existente.
        validateEdgeWeight(weight); // Valida peso positivo.
        matrix[source][target] = weight; // Substitui peso antigo.
    }

    @Override // Implementa consulta de peso.
    public double getEdgeWeight(int source, int target) { // Retorna peso de source -> target.
        requireEdge(source, target); // Garante que a aresta existe.
        return matrix[source][target]; // Retorna valor da celula.
    }

    @Override // Implementa grau de entrada.
    public int getVertexInDegree(int vertex) { // Conta quantas arestas chegam em vertex.
        validateVertex(vertex); // Valida o vertice.
        int degree = 0; // Contador de entradas.

        for (int source = 0; source < vertexCount; source++) { // Percorre a coluna vertex.
            if (matrix[source][vertex] > 0.0) { // Se source aponta para vertex...
                degree++; // ...conta uma entrada.
            }
        }

        return degree; // Retorna grau de entrada.
    }

    @Override // Implementa grau de saida.
    public int getVertexOutDegree(int vertex) { // Conta quantas arestas saem de vertex.
        validateVertex(vertex); // Valida o vertice.
        int degree = 0; // Contador de saidas.

        for (int target = 0; target < vertexCount; target++) { // Percorre a linha vertex.
            if (matrix[vertex][target] > 0.0) { // Se existe aresta para target...
                degree++; // ...conta uma saida.
            }
        }

        return degree; // Retorna grau de saida.
    }

    @Override // Implementa sucessores.
    public List<Integer> getSuccessors(int vertex) { // Lista destinos de vertex.
        validateVertex(vertex); // Valida o vertice.
        List<Integer> successors = new ArrayList<>(); // Lista de destinos.

        for (int target = 0; target < vertexCount; target++) { // Percorre todos os possiveis destinos.
            if (matrix[vertex][target] > 0.0) { // Se ha aresta vertex -> target...
                successors.add(target); // ...target e sucessor.
            }
        }

        return successors; // Retorna lista de sucessores.
    }

    @Override // Implementa predecessores.
    public List<Integer> getPredecessors(int vertex) { // Lista origens que apontam para vertex.
        validateVertex(vertex); // Valida o vertice.
        List<Integer> predecessors = new ArrayList<>(); // Lista de origens.

        for (int source = 0; source < vertexCount; source++) { // Percorre todos os possiveis sources.
            if (matrix[source][vertex] > 0.0) { // Se existe source -> vertex...
                predecessors.add(source); // ...source e predecessor.
            }
        }

        return predecessors; // Retorna lista de predecessores.
    }

    @Override // Implementa iteracao de arestas.
    public List<ArestaGrafo> iterEdges() { // Devolve todas as arestas existentes.
        List<ArestaGrafo> edges = new ArrayList<>(); // Lista que recebera as arestas.

        for (int source = 0; source < vertexCount; source++) { // Percorre todas as linhas.
            for (int target = 0; target < vertexCount; target++) { // Percorre todas as colunas.
                if (matrix[source][target] > 0.0) { // Se a celula representa uma aresta...
                    edges.add(new ArestaGrafo(source, target, matrix[source][target])); // Cria objeto da aresta.
                }
            }
        }

        return edges; // Retorna todas as arestas.
    }
}
