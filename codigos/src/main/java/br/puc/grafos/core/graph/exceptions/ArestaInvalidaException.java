// Pacote reservado para excecoes da API de grafos.
package br.puc.grafos.core.graph.exceptions;

/**
 * Erro usado quando uma operacao exige uma aresta que nao existe.
 *
 * <p>Exemplo: remover 0 -> 1 sem essa aresta estar cadastrada e uma operacao
 * invalida, entao a API avisa usando esta excecao.</p>
 */
public class ArestaInvalidaException extends GrafoException {

    // Construtor recebe origem e destino da aresta que deveria existir.
    public ArestaInvalidaException(int source, int target) {
        // Monta mensagem clara com a direcao da aresta procurada.
        super("A aresta " + source + " -> " + target + " nao existe.");
    }
}
