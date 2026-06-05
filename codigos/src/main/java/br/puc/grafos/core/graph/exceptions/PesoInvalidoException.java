// Pacote reservado para excecoes da API de grafos.
package br.puc.grafos.core.graph.exceptions;

/**
 * Erro usado quando um peso invalido e informado.
 *
 * <p>Neste projeto, peso de vertice e de aresta precisa ser positivo e finito.
 * Assim evitamos peso zero, negativo, infinito ou NaN.</p>
 */
public class PesoInvalidoException extends GrafoException {

    // Construtor recebe a mensagem especifica decidida pelo metodo validador.
    public PesoInvalidoException(String message) {
        // Repassa a mensagem para a excecao base.
        super(message);
    }
}
