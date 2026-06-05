// Pacote reservado para excecoes da API de grafos.
package br.puc.grafos.core.graph.exceptions;

/**
 * Erro usado quando um indice de vertice esta fora do intervalo valido.
 *
 * <p>Exemplo: se o grafo tem 3 vertices, os indices validos sao 0, 1 e 2.
 * Tentar acessar o vertice 99 deve gerar esta excecao.</p>
 */
public class VerticeInvalidoException extends GrafoException {

    // Construtor recebe o vertice errado e a quantidade total de vertices.
    public VerticeInvalidoException(int vertex, int vertexCount) {
        // Monta uma mensagem didatica dizendo qual faixa de indices e aceita.
        super("Vertice " + vertex + " invalido. Use valores entre 0 e " + (vertexCount - 1) + ".");
    }
}
