// Pacote reservado para excecoes da API de grafos.
package br.puc.grafos.core.graph.exceptions;

/**
 * Excecao base para problemas da API de grafos.
 *
 * <p>Ela herda de RuntimeException para podermos lancar erros de uso da API,
 * como vertice invalido ou aresta inexistente, sem obrigar todo metodo a usar
 * throws na assinatura.</p>
 */
public class GrafoException extends RuntimeException {

    // Construtor recebe a mensagem que explica o erro para quem chamou a API.
    public GrafoException(String message) {
        // Envia a mensagem para RuntimeException guardar e exibir.
        super(message);
    }
}
