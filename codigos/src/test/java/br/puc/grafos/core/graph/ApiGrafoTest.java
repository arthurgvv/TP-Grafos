// Pacote dos testes da API principal de grafos.
package br.puc.grafos.core.graph;

// Importa a excecao esperada quando uma aresta inexistente e usada incorretamente.
import br.puc.grafos.core.graph.exceptions.ArestaInvalidaException;
// Importa a excecao esperada quando o indice de vertice e invalido.
import br.puc.grafos.core.graph.exceptions.VerticeInvalidoException;
// Importa a excecao esperada quando o peso e invalido.
import br.puc.grafos.core.graph.exceptions.PesoInvalidoException;
// Importa a excecao esperada quando alguem tenta criar laco.
import br.puc.grafos.core.graph.exceptions.LacoInvalidoException;
// Importa a anotacao de teste simples do JUnit.
import org.junit.jupiter.api.Test;
// Importa suporte do JUnit para criar pasta temporaria em teste.
import org.junit.jupiter.api.io.TempDir;
// Importa teste parametrizado, usado para testar lista e matriz com o mesmo codigo.
import org.junit.jupiter.params.ParameterizedTest;
// Importa a fonte de dados dos testes parametrizados.
import org.junit.jupiter.params.provider.MethodSource;

// Importa utilitario para ler e verificar arquivo exportado.
import java.nio.file.Files;
// Importa Path para representar caminhos de arquivo.
import java.nio.file.Path;
// Importa funcao que recebe int e devolve um grafo.
import java.util.function.IntFunction;
// Importa Stream para listar as fabricas dos grafos.
import java.util.stream.Stream;

// Importa todos os asserts do JUnit, como assertEquals e assertTrue.
import static org.junit.jupiter.api.Assertions.*;

// Classe de testes da API de grafos exigida pelo trabalho.
class ApiGrafoTest {

    // Fonte dos testes parametrizados: cria tanto lista quanto matriz.
    static Stream<IntFunction<AbstractGraph>> graphFactories() {
        // Retorna duas fabricas: uma chama new GrafoListaAdjacencia, outra new GrafoMatrizAdjacencia.
        return Stream.of(GrafoListaAdjacencia::new, GrafoMatrizAdjacencia::new);
    }

    // Testa se o construtor cria grafo vazio com a quantidade correta de vertices.
    @ParameterizedTest
    // Diz ao JUnit para usar as fabricas acima.
    @MethodSource("graphFactories")
    void construtorCriaGrafoVazio(IntFunction<AbstractGraph> factory) {
        // Cria um grafo de 3 vertices usando a fabrica atual.
        AbstractGraph graph = factory.apply(3);

        // Confere que o grafo possui 3 vertices.
        assertEquals(3, graph.getVertexCount());
        // Confere que ele comeca sem arestas.
        assertEquals(0, graph.getEdgeCount());
        // Confere que isEmptyGraph entende que nao ha arestas.
        assertTrue(graph.isEmptyGraph());
    }

    // Testa se tamanho invalido e recusado.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void construtorRejeitaTamanhoInvalido(IntFunction<AbstractGraph> factory) {
        // Um grafo com 0 vertices nao faz sentido neste projeto e deve gerar erro.
        assertThrows(IllegalArgumentException.class, () -> factory.apply(0));
    }

    // Testa criacao de aresta direcionada e ponderada.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void addEdgeCriaArestaDirecionadaEPonderada(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Adiciona aresta 0 -> 1 com peso 2.
        graph.addEdge(0, 1, 2.0);

        // Confere que a aresta 0 -> 1 existe.
        assertTrue(graph.hasEdge(0, 1));
        // Confere que 1 -> 0 nao foi criada automaticamente, pois o grafo e direcionado.
        assertFalse(graph.hasEdge(1, 0));
        // Confere que existe exatamente uma aresta.
        assertEquals(1, graph.getEdgeCount());
        // Confere que o peso gravado e 2.
        assertEquals(2.0, graph.getEdgeWeight(0, 1));
    }

    // Testa a regra do trabalho: nao duplicar aresta, somar peso.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void addEdgeAcumulaPesoSemDuplicarAresta(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Adiciona uma primeira interacao 0 -> 1 com peso 2.
        graph.addEdge(0, 1, 2.0);
        // Adiciona outra interacao entre o mesmo par com peso 3.
        graph.addEdge(0, 1, 3.0);

        // Confere que ainda existe so uma aresta 0 -> 1.
        assertEquals(1, graph.getEdgeCount());
        // Confere que o peso acumulou: 2 + 3 = 5.
        assertEquals(5.0, graph.getEdgeWeight(0, 1));
    }

    // Testa que grafo simples nao aceita laco.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void addEdgeRejeitaLaco(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Tentar criar 1 -> 1 deve lancar LacoInvalidoException.
        assertThrows(LacoInvalidoException.class, () -> graph.addEdge(1, 1));
    }

    // Testa validacao de vertices fora do intervalo.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void addEdgeRejeitaVerticeInvalido(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com vertices 0, 1 e 2.
        AbstractGraph graph = factory.apply(3);

        // Vertice 99 nao existe e deve gerar VerticeInvalidoException.
        assertThrows(VerticeInvalidoException.class, () -> graph.addEdge(0, 99));
        // Vertice -1 tambem nao existe e deve gerar VerticeInvalidoException.
        assertThrows(VerticeInvalidoException.class, () -> graph.hasEdge(-1, 0));
    }

    // Testa validacao de peso.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void addEdgeRejeitaPesoInvalido(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Peso 0.0 e invalido porque aceitamos apenas pesos positivos.
        assertThrows(PesoInvalidoException.class, () -> graph.addEdge(0, 1, 0.0));
    }

    // Testa remocao de uma aresta existente.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void removeEdgeApagaArestaExistente(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);
        // Adiciona aresta 0 -> 1 com peso padrao.
        graph.addEdge(0, 1);

        // Remove a aresta 0 -> 1.
        graph.removeEdge(0, 1);

        // Confere que a aresta nao existe mais.
        assertFalse(graph.hasEdge(0, 1));
        // Confere que o contador de arestas voltou para zero.
        assertEquals(0, graph.getEdgeCount());
    }

    // Testa erro ao tentar remover aresta inexistente.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void removeEdgeRejeitaArestaInexistente(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices e nenhuma aresta.
        AbstractGraph graph = factory.apply(3);

        // Remover 0 -> 1 sem ela existir deve gerar ArestaInvalidaException.
        assertThrows(ArestaInvalidaException.class, () -> graph.removeEdge(0, 1));
    }

    // Testa grau de entrada e grau de saida em grafo direcionado.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void grausFuncionamParaGrafoDirecionado(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 4 vertices.
        AbstractGraph graph = factory.apply(4);

        // Cria aresta entrando em 1.
        graph.addEdge(0, 1);
        // Cria outra aresta entrando em 1.
        graph.addEdge(2, 1);
        // Cria aresta saindo de 1.
        graph.addEdge(1, 3);

        // Vertice 1 recebe duas arestas: 0 -> 1 e 2 -> 1.
        assertEquals(2, graph.getVertexInDegree(1));
        // Vertice 1 possui uma aresta saindo: 1 -> 3.
        assertEquals(1, graph.getVertexOutDegree(1));
        // Vertice 0 nao recebe nenhuma aresta neste teste.
        assertEquals(0, graph.getVertexInDegree(0));
    }

    // Testa sucessor, predecessor e relacoes entre arestas.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void sucessorPredecessorERelacoesFuncionam(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 4 vertices.
        AbstractGraph graph = factory.apply(4);

        // Cria 0 -> 1.
        graph.addEdge(0, 1);
        // Cria 0 -> 2.
        graph.addEdge(0, 2);
        // Cria 3 -> 2.
        graph.addEdge(3, 2);

        // 2 e sucessor de 0 porque existe 0 -> 2.
        assertTrue(graph.isSucessor(0, 2));
        // 0 e predecessor de 2 porque existe 0 -> 2.
        assertTrue(graph.isPredessor(2, 0));
        // As arestas 0 -> 1 e 0 -> 2 divergem porque saem da mesma origem.
        assertTrue(graph.isDivergent(0, 1, 0, 2));
        // As arestas 0 -> 2 e 3 -> 2 convergem porque chegam ao mesmo destino.
        assertTrue(graph.isConvergent(0, 2, 3, 2));
        // A aresta 0 -> 1 incide no vertice 0 porque 0 e sua origem.
        assertTrue(graph.isIncident(0, 1, 0));
        // A aresta 0 -> 1 nao incide no vertice 3.
        assertFalse(graph.isIncident(0, 1, 3));
    }

    // Testa peso de vertice e peso de aresta.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void pesosDeVerticeEArestaFuncionam(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Define peso do vertice 1.
        graph.setVertexWeight(1, 4.5);
        // Cria aresta 0 -> 1 com peso 2.
        graph.addEdge(0, 1, 2.0);
        // Altera o peso da aresta 0 -> 1 para 7.
        graph.setEdgeWeight(0, 1, 7.0);

        // Confere o peso do vertice 1.
        assertEquals(4.5, graph.getVertexWeight(1));
        // Confere o peso atualizado da aresta.
        assertEquals(7.0, graph.getEdgeWeight(0, 1));
    }

    // Testa o conceito de conexidade forte.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void conectadoSignificaFortementeConectado(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Cria caminho 0 -> 1.
        graph.addEdge(0, 1);
        // Cria caminho 1 -> 2.
        graph.addEdge(1, 2);
        // Cria caminho 2 -> 0, fechando um ciclo.
        graph.addEdge(2, 0);

        // Em grafo direcionado, isso torna todos alcancaveis a partir de todos.
        assertTrue(graph.isConnected());
    }

    // Testa que caminho em apenas uma direcao nao basta para conexidade forte.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void caminhoEmUmaDirecaoNaoEConexaoForte(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Cria 0 -> 1.
        graph.addEdge(0, 1);
        // Cria 1 -> 2.
        graph.addEdge(1, 2);

        // Como 2 nao volta para 1 nem 0, o grafo nao e fortemente conectado.
        assertFalse(graph.isConnected());
    }

    // Testa a definicao de grafo completo direcionado.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void grafoCompletoExigeTodoParDirecionado(IntFunction<AbstractGraph> factory) {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);

        // Percorre todas as possiveis origens.
        for (int source = 0; source < 3; source++) {
            // Percorre todos os possiveis destinos.
            for (int target = 0; target < 3; target++) {
                // Evita laco, porque grafo simples nao aceita source == target.
                if (source != target) {
                    // Adiciona a aresta direcionada source -> target.
                    graph.addEdge(source, target);
                }
            }
        }

        // Com 3 vertices, todas as 6 arestas direcionadas possiveis existem.
        assertTrue(graph.isCompleteGraph());
        // Formula para grafo completo direcionado simples: n * (n - 1) = 3 * 2 = 6.
        assertEquals(6, graph.getEdgeCount());
    }

    // Testa exportacao no formato aceito pelo Gephi.
    @ParameterizedTest
    // Executa o mesmo teste para lista e matriz.
    @MethodSource("graphFactories")
    void exportToGephiCriaCsv(IntFunction<AbstractGraph> factory, @TempDir Path tempDir) throws Exception {
        // Cria um grafo com 3 vertices.
        AbstractGraph graph = factory.apply(3);
        // Nomeia o vertice 0.
        graph.setVertexLabel(0, "Arthur");
        // Nomeia o vertice 1.
        graph.setVertexLabel(1, "Victor");
        // Cria aresta 0 -> 1 com peso 2.5.
        graph.addEdge(0, 1, 2.5);

        // Exporta o arquivo CSV para uma pasta temporaria criada pelo JUnit.
        Path output = graph.exportToGEPHI(tempDir.resolve("grafo_httpx").toString());

        // Confere que o arquivo foi realmente criado.
        assertTrue(Files.exists(output));
        // Le o conteudo do CSV para validar o texto.
        String content = Files.readString(output);
        // Confere o cabecalho do CSV.
        assertTrue(content.contains("Source,Target,Weight"));
        // Confere que a aresta exportada tem origem, destino, peso e labels.
        assertTrue(content.contains("0,1,2.5,Arthur,Victor"));
    }

    // Teste normal, sem parametrizacao, comparando lista e matriz lado a lado.
    @Test
    void listaEMatrizExibemMesmoComportamentoBasico() {
        // Cria a implementacao com lista.
        AbstractGraph listGraph = new GrafoListaAdjacencia(3);
        // Cria a implementacao com matriz.
        AbstractGraph matrixGraph = new GrafoMatrizAdjacencia(3);

        // Adiciona uma aresta na lista.
        listGraph.addEdge(0, 1, 2.0);
        // Adiciona a mesma aresta na matriz.
        matrixGraph.addEdge(0, 1, 2.0);

        // Confere que as duas implementacoes contam a mesma quantidade de arestas.
        assertEquals(listGraph.getEdgeCount(), matrixGraph.getEdgeCount());
        // Confere que as duas implementacoes guardam o mesmo peso.
        assertEquals(listGraph.getEdgeWeight(0, 1), matrixGraph.getEdgeWeight(0, 1));
    }
}
