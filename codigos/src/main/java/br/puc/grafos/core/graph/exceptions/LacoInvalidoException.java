// Pacote reservado para excecoes da API de grafos.
package br.puc.grafos.core.graph.exceptions;

/**
 * Erro usado quando tentam criar laco em grafo simples.
 *
 * <p>Laco e uma aresta do vertice para ele mesmo, por exemplo 2 -> 2.
 * O enunciado pede grafos simples, entao esse caso nao pode entrar.</p>
 */
public class LacoInvalidoException extends GrafoException {

    // Construtor recebe o vertice que tentou apontar para ele mesmo.
    public LacoInvalidoException(int vertex) {
        // Monta uma mensagem explicando a regra violada.
        super("Grafo simples nao permite laco no vertice " + vertex + ".");
    }
}
