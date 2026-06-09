// Este pacote contem servicos que montam grafos a partir de interacoes.
package br.puc.grafos.servicos;

// Importa a classe abstrata para o resultado poder guardar lista ou matriz.
import br.puc.grafos.core.graph.AbstractGraph;

// Importa Map para associar login de usuario ao indice numerico do vertice.
import java.util.Map;

/**
 * Guarda o resultado da construcao de um grafo.
 *
 * <p>O grafo trabalha com vertices por indice inteiro: 0, 1, 2, etc. Como no
 * GitHub os usuarios sao logins, este record carrega as duas coisas juntas:
 * o grafo pronto e o mapa que diz qual login virou qual indice.</p>
 *
 * @param graph grafo construido com os usuarios e arestas
 * @param userIndex mapa login -> indice do vertice
 */
public record GrafoConstruido(
        // Grafo pronto, podendo ser GrafoListaAdjacencia ou GrafoMatrizAdjacencia.
        AbstractGraph grafo,
        // Mapa para traduzir "Arthur" para 0, "Victor" para 1, e assim por diante.
        Map<String, Integer> indiceUsuarios
) {
    // O corpo fica vazio porque o record ja entrega os acessores grafo() e indiceUsuarios().
}
