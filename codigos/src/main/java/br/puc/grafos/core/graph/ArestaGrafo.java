// Este arquivo pertence ao pacote da API principal de grafos.
package br.puc.grafos.core.graph;

/**
 * Representa uma aresta retornada quando percorremos todas as arestas do grafo.
 *
 * <p>Usamos um record porque ele ja cria automaticamente construtor, getters
 * com o nome dos campos, equals, hashCode e toString. Para o professor, a ideia
 * importante e: esta classe nao altera o grafo; ela apenas carrega os dados de
 * uma aresta encontrada.</p>
 *
 * @param source vertice de origem da aresta direcionada
 * @param target vertice de destino da aresta direcionada
 * @param weight peso atual da aresta
 */
public record ArestaGrafo(
        // Indice inteiro do vertice que sai apontando para outro vertice.
        int source,
        // Indice inteiro do vertice que recebe a aresta.
        int target,
        // Peso da relacao; no grafo integrado ele soma os pesos das interacoes.
        double weight
) {
    // O corpo fica vazio porque o record ja guarda os tres valores para nos.
}
