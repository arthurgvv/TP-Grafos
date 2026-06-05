// Pacote dos testes do servico que monta grafos de colaboracao.
package br.puc.grafos.services;

// Importa o tipo abstrato para consultar o grafo construido.
import br.puc.grafos.core.graph.AbstractGraph;
// Importa o record que representa uma interacao do GitHub.
import br.puc.grafos.github.InteracaoGitHub;
// Importa os tipos de interacao e seus pesos.
import br.puc.grafos.github.TipoInteracao;
// Importa a anotacao de teste do JUnit.
import org.junit.jupiter.api.Test;

// Importa List para montar exemplos de interacoes.
import java.util.List;
// Importa Map para receber os grafos separados e o indice de usuarios.
import java.util.Map;

// Importa assertEquals para comparacoes nos testes.
import static org.junit.jupiter.api.Assertions.assertEquals;

// Testa a classe ConstrutorGrafoColaboracao.
class ConstrutorGrafoColaboracaoTest {

    // Testa se o grafo integrado soma pesos de interacoes repetidas entre os mesmos usuarios.
    @Test
    void grafoIntegradoAcumulaPesosEntreMesmosUsuarios() {
        // Monta uma lista pequena simulando interacoes reais do GitHub.
        List<InteracaoGitHub> interacoes = List.of(
                // Arthur comentou em PR relacionado a Victor: peso 2.
                new InteracaoGitHub("Arthur", "Victor", TipoInteracao.COMENTARIO_PR, "url-1"),
                // Arthur tambem revisou PR relacionado a Victor: peso 4.
                new InteracaoGitHub("Arthur", "Victor", TipoInteracao.REVISAO_PR, "url-2"),
                // Victor fez merge relacionado a Arthur: peso 5.
                new InteracaoGitHub("Victor", "Arthur", TipoInteracao.MERGE_PR, "url-3")
        );

        // Constroi um grafo integrado usando a implementacao padrao por lista.
        GrafoConstruido grafoConstruido = new ConstrutorGrafoColaboracao().construirGrafoIntegrado(interacoes);
        // Pega o grafo pronto de dentro do resultado.
        AbstractGraph graph = grafoConstruido.grafo();
        // Pega o mapa login -> indice para nao depender de ordem manual.
        Map<String, Integer> index = grafoConstruido.indiceUsuarios();

        // Confere que o vertice de Arthur recebeu label "Arthur".
        assertEquals("Arthur", graph.getVertexLabel(index.get("Arthur")));
        // Confere que existem duas arestas: Arthur -> Victor e Victor -> Arthur.
        assertEquals(2, graph.getEdgeCount());
        // Confere que Arthur -> Victor somou 2 + 4 = 6.
        assertEquals(6.0, graph.getEdgeWeight(index.get("Arthur"), index.get("Victor")));
        // Confere que Victor -> Arthur recebeu peso 5.
        assertEquals(5.0, graph.getEdgeWeight(index.get("Victor"), index.get("Arthur")));
    }

    // Testa se os grafos separados ficam nos grupos esperados.
    @Test
    void grafosSeparadosMantemGruposDeRelacoesEsperados() {
        // Cria uma interacao de cada grupo exigido pelo trabalho.
        List<InteracaoGitHub> interacoes = List.of(
                // Comentario em issue entra no grafo "comments".
                new InteracaoGitHub("Arthur", "Victor", TipoInteracao.COMENTARIO_ISSUE, "url-1"),
                // Fechamento de issue entra no grafo "issue_close".
                new InteracaoGitHub("Simone", "Victor", TipoInteracao.FECHAMENTO_ISSUE, "url-2"),
                // Review de PR entra no grafo "reviews_merges".
                new InteracaoGitHub("Laura", "Victor", TipoInteracao.REVISAO_PR, "url-3"),
                // Merge de PR tambem entra no grafo "reviews_merges".
                new InteracaoGitHub("Victor", "Laura", TipoInteracao.MERGE_PR, "url-4")
        );

        // Constroi todos os grafos: comments, issue_close, reviews_merges e integrated.
        Map<String, GrafoConstruido> graphs = new ConstrutorGrafoColaboracao().construirGrafosSeparados(interacoes);

        // Apenas uma interacao entrou em comments.
        assertEquals(1, graphs.get("comments").grafo().getEdgeCount());
        // Apenas uma interacao entrou em issue_close.
        assertEquals(1, graphs.get("issue_close").grafo().getEdgeCount());
        // Duas interacoes entraram em reviews_merges.
        assertEquals(2, graphs.get("reviews_merges").grafo().getEdgeCount());
        // O grafo integrado recebeu todas as quatro interacoes.
        assertEquals(4, graphs.get("integrated").grafo().getEdgeCount());
    }
}
