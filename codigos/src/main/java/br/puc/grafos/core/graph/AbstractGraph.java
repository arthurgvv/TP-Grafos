package br.puc.grafos.core.graph; // Define o pacote onde fica a API central de grafos.

import br.puc.grafos.core.graph.exceptions.ArestaInvalidaException; // Excecao para aresta inexistente.
import br.puc.grafos.core.graph.exceptions.PesoInvalidoException; // Excecao para peso invalido.
import br.puc.grafos.core.graph.exceptions.VerticeInvalidoException; // Excecao para vertice invalido.

import java.io.IOException; // Usado quando a exportacao para arquivo pode falhar.
import java.nio.charset.StandardCharsets; // Define UTF-8 ao escrever o CSV.
import java.nio.file.Files; // API Java para criar pastas e escrever arquivos.
import java.nio.file.Path; // Representa caminhos de arquivo de forma segura.
import java.util.ArrayDeque; // Pilha/fila eficiente usada na DFS.
import java.util.ArrayList; // Lista dinamica usada em percursos e exportacao.
import java.util.Deque; // Interface para usar estrutura como pilha.
import java.util.List; // Interface de listas usada na API.

/**
 * Classe abstrata que define a API comum para grafos direcionados simples.
 *
 * <p>Ela e abstrata porque nao escolhe como as arestas sao armazenadas.
 * A lista de adjacencia e a matriz de adjacencia herdam desta classe e
 * implementam os detalhes de armazenamento.</p>
 */
public abstract class AbstractGraph { // Classe-base: nao pode ser instanciada diretamente.

    /** Quantidade total de vertices do grafo. */
    protected final int vertexCount; // protected permite que as subclasses acessem.

    /** Quantidade atual de arestas existentes no grafo. */
    protected int edgeCount; // muda quando addEdge/removeEdge sao chamados.

    /** Peso de cada vertice, usado por metricas ou analises futuras. */
    private final double[] vertexWeights; // array indexado pelo numero do vertice.

    /** Rotulo legivel de cada vertice, normalmente o login do usuario. */
    private final String[] vertexLabels; // exemplo: indice 0 pode ter label "Arthur".

    /**
     * Cria um grafo vazio com a quantidade informada de vertices.
     *
     * @param numVertices quantidade de vertices; precisa ser positiva
     */
    protected AbstractGraph(int numVertices) { // Construtor chamado pelas subclasses.
        if (numVertices <= 0) { // Um grafo sem vertices nao atende nossa API.
            throw new IllegalArgumentException("O grafo deve ter pelo menos um vertice."); // Falha cedo.
        }

        this.vertexCount = numVertices; // Guarda o total fixo de vertices.
        this.edgeCount = 0; // Todo grafo nasce sem arestas.
        this.vertexWeights = new double[numVertices]; // Cria espaco para o peso de cada vertice.
        this.vertexLabels = new String[numVertices]; // Cria espaco para o rotulo de cada vertice.

        for (int i = 0; i < numVertices; i++) { // Percorre todos os indices de vertices.
            vertexWeights[i] = 1.0; // Peso padrao: 1.0.
            vertexLabels[i] = String.valueOf(i); // Label padrao: o proprio numero do vertice.
        }
    }

    /**
     * Verifica se o indice representa um vertice existente.
     */
    protected void validateVertex(int vertex) { // Metodo auxiliar para evitar repetir validacao.
        if (vertex < 0 || vertex >= vertexCount) { // Indice valido vai de 0 ate vertexCount - 1.
            throw new VerticeInvalidoException(vertex, vertexCount); // Avisa exatamente qual vertice falhou.
        }
    }

    /**
     * Valida varios vertices em uma unica chamada.
     */
    protected void validateVertices(int... vertices) { // Recebe quantidade variavel de vertices.
        for (int vertex : vertices) { // Percorre cada vertice informado.
            validateVertex(vertex); // Reusa a validacao individual.
        }
    }

    /**
     * Garante que o peso de uma aresta representa uma interacao real.
     */
    protected void validateEdgeWeight(double weight) { // Peso de aresta precisa ser positivo.
        if (weight <= 0.0) { // Peso zero indicaria ausencia de interacao.
            throw new PesoInvalidoException("O peso da aresta deve ser maior que zero."); // Erro semantico.
        }
    }

    /**
     * Garante que o peso de um vertice nao seja negativo.
     */
    protected void validateVertexWeight(double weight) { // Peso de vertice pode ser zero, mas nao negativo.
        if (weight < 0.0) { // Peso negativo nao faz sentido neste modelo.
            throw new PesoInvalidoException("O peso do vertice nao pode ser negativo."); // Erro semantico.
        }
    }

    /**
     * Exige que uma aresta exista antes de operar sobre ela.
     */
    protected void requireEdge(int source, int target) { // Usado por remove/get/set de peso.
        validateVertices(source, target); // Primeiro garante que os vertices existem.
        if (!hasEdge(source, target)) { // Depois verifica se a aresta source -> target existe.
            throw new ArestaInvalidaException(source, target); // Se nao existe, a operacao e inconsistente.
        }
    }

    public int getVertexCount() { // Metodo obrigatorio do enunciado.
        return vertexCount; // Retorna o total de vertices.
    }

    public int getEdgeCount() { // Metodo obrigatorio do enunciado.
        return edgeCount; // Retorna quantas arestas existem agora.
    }

    public boolean isEmptyGraph() { // Metodo obrigatorio do enunciado.
        return edgeCount == 0; // Grafo vazio, aqui, significa sem arestas.
    }

    public void setVertexWeight(int vertex, double weight) { // Define peso de um vertice.
        validateVertex(vertex); // Garante que o vertice existe.
        validateVertexWeight(weight); // Garante que o peso nao e negativo.
        vertexWeights[vertex] = weight; // Salva o peso no array.
    }

    public double getVertexWeight(int vertex) { // Consulta peso de um vertice.
        validateVertex(vertex); // Garante que o vertice existe antes de acessar o array.
        return vertexWeights[vertex]; // Retorna o peso salvo.
    }

    public void setVertexLabel(int vertex, String label) { // Define nome/rotulo legivel do vertice.
        validateVertex(vertex); // Garante que o indice existe.
        vertexLabels[vertex] = label; // Salva o label, ex: login do GitHub.
    }

    public String getVertexLabel(int vertex) { // Consulta o label do vertice.
        validateVertex(vertex); // Garante que o indice existe.
        return vertexLabels[vertex]; // Retorna o nome/rotulo salvo.
    }

    public abstract boolean hasEdge(int source, int target); // Subclasse decide como consultar aresta.

    public abstract void addEdge(int source, int target); // Adiciona aresta com peso padrao.

    public abstract void addEdge(int source, int target, double weight); // Adiciona aresta ponderada.

    public abstract void removeEdge(int source, int target); // Remove aresta existente.

    public abstract void setEdgeWeight(int source, int target, double weight); // Altera peso de aresta.

    public abstract double getEdgeWeight(int source, int target); // Consulta peso de aresta.

    public abstract int getVertexInDegree(int vertex); // Conta arestas que chegam.

    public abstract int getVertexOutDegree(int vertex); // Conta arestas que saem.

    public abstract List<Integer> getSuccessors(int vertex); // Lista destinos das arestas de saida.

    public abstract List<Integer> getPredecessors(int vertex); // Lista origens das arestas de entrada.

    public abstract List<ArestaGrafo> iterEdges(); // Lista todas as arestas existentes.

    /**
     * Verifica se target e sucessor de source.
     */
    public boolean isSucessor(int source, int target) { // Nome mantido como no PDF.
        return hasEdge(source, target); // Se source -> target existe, target e sucessor.
    }

    /**
     * Verifica se possiblePredessor aponta para vertex.
     *
     * <p>O nome segue o enunciado, que usa "Predessor".</p>
     */
    public boolean isPredessor(int vertex, int possiblePredessor) { // Nome mantido como no PDF.
        return hasEdge(possiblePredessor, vertex); // Se possiblePredessor -> vertex existe, ele e predecessor.
    }

    /**
     * Duas arestas sao divergentes quando saem da mesma origem.
     */
    public boolean isDivergent(int u1, int v1, int u2, int v2) { // Recebe duas arestas: u1->v1 e u2->v2.
        requireEdge(u1, v1); // Garante que a primeira aresta existe.
        requireEdge(u2, v2); // Garante que a segunda aresta existe.
        return u1 == u2 && v1 != v2; // Divergem se mesma origem e destinos diferentes.
    }

    /**
     * Duas arestas sao convergentes quando chegam no mesmo destino.
     */
    public boolean isConvergent(int u1, int v1, int u2, int v2) { // Recebe duas arestas.
        requireEdge(u1, v1); // Garante que u1 -> v1 existe.
        requireEdge(u2, v2); // Garante que u2 -> v2 existe.
        return v1 == v2 && u1 != u2; // Convergem se mesmo destino e origens diferentes.
    }

    /**
     * Um vertice e incidente quando participa da aresta como origem ou destino.
     */
    public boolean isIncident(int source, int target, int vertex) { // Verifica se vertex toca source->target.
        requireEdge(source, target); // A aresta precisa existir para falar de incidencia.
        validateVertex(vertex); // O vertice consultado tambem precisa existir.
        return vertex == source || vertex == target; // Incidente se e origem ou destino.
    }

    /**
     * Verifica conectividade forte em grafo direcionado.
     *
     * <p>Primeiro percorremos seguindo as arestas. Depois percorremos usando o
     * grafo reverso, representado pelos predecessores.</p>
     */
    public boolean isConnected() { // Para grafo direcionado, usamos conectividade forte.
        if (dfsFrom(0, true).size() != vertexCount) { // Verifica se todos sao alcancaveis saindo do 0.
            return false; // Se algum nao foi visitado, nao e fortemente conectado.
        }
        return dfsFrom(0, false).size() == vertexCount; // Verifica alcance no grafo reverso.
    }

    private List<Integer> dfsFrom(int start, boolean useSuccessors) { // DFS iterativa.
        boolean[] visited = new boolean[vertexCount]; // Marca quais vertices ja foram visitados.
        List<Integer> order = new ArrayList<>(); // Guarda a ordem/quantidade de visitados.
        Deque<Integer> stack = new ArrayDeque<>(); // Pilha usada pela DFS.
        stack.push(start); // Comeca pelo vertice escolhido.

        while (!stack.isEmpty()) { // Continua enquanto houver vertices para explorar.
            int vertex = stack.pop(); // Remove um vertice da pilha.
            if (visited[vertex]) { // Se ja foi visitado, evita repetir processamento.
                continue; // Pula para o proximo item da pilha.
            }

            visited[vertex] = true; // Marca o vertice como visitado.
            order.add(vertex); // Registra que esse vertice foi alcancado.

            List<Integer> neighbors = useSuccessors // Decide a direcao do percurso.
                    ? getSuccessors(vertex) // true: segue arestas normais.
                    : getPredecessors(vertex); // false: segue arestas invertidas.

            for (int neighbor : neighbors) { // Percorre vizinhos encontrados.
                if (!visited[neighbor]) { // So empilha quem ainda nao foi visitado.
                    stack.push(neighbor); // Adiciona vizinho para exploracao futura.
                }
            }
        }

        return order; // Retorna todos os vertices alcancados.
    }

    /**
     * Em grafo direcionado simples completo, todo par distinto possui aresta.
     */
    public boolean isCompleteGraph() { // Metodo obrigatorio do enunciado.
        return edgeCount == vertexCount * (vertexCount - 1); // Maximo dirigido sem lacos.
    }

    /**
     * Exporta o grafo para CSV de arestas importavel pelo Gephi.
     */
    public Path exportToGEPHI(String path) { // Metodo obrigatorio para visualizacao no Gephi.
        Path output = Path.of(path); // Converte texto em caminho de arquivo.
        if (!output.toString().toLowerCase().endsWith(".csv")) { // Se usuario nao passou .csv...
            output = Path.of(path + ".csv"); // ...adicionamos a extensao automaticamente.
        }

        List<String> lines = new ArrayList<>(); // Linhas que serao gravadas no arquivo.
        lines.add("Source,Target,Weight,SourceLabel,TargetLabel"); // Cabecalho entendido pelo Gephi.

        for (ArestaGrafo edge : iterEdges()) { // Percorre cada aresta real do grafo.
            lines.add(edge.source() // Origem numerica da aresta.
                    + "," + edge.target() // Destino numerico da aresta.
                    + "," + edge.weight() // Peso da aresta.
                    + "," + getVertexLabel(edge.source()) // Nome/label da origem.
                    + "," + getVertexLabel(edge.target())); // Nome/label do destino.
        }

        try { // Bloco que pode gerar IOException ao escrever arquivo.
            Path parent = output.getParent(); // Pega pasta pai do arquivo.
            if (parent != null) { // Se existe uma pasta no caminho...
                Files.createDirectories(parent); // ...garante que ela sera criada.
            }
            Files.write(output, lines, StandardCharsets.UTF_8); // Escreve o CSV em UTF-8.
            return output; // Retorna o caminho final gerado.
        } catch (IOException exception) { // Captura erro de escrita.
            throw new IllegalStateException("Nao foi possivel exportar o grafo.", exception); // Converte para erro de execucao.
        }
    }
}
