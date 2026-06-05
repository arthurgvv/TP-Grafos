// Este pacote guarda os conceitos ligados aos dados minerados do GitHub.
package br.puc.grafos.github;

/**
 * Representa uma interacao antes dela virar aresta no grafo.
 *
 * <p>Na linguagem do trabalho: loginOrigem e loginDestino sao usuarios do GitHub.
 * O loginOrigem fez alguma acao, e o loginDestino e o dono ou usuario afetado
 * por essa acao. Depois, o builder transforma essa interacao em uma aresta
 * direcionada source -> target.</p>
 *
 * @param loginOrigem usuario que realizou a acao
 * @param loginDestino usuario dono do artefato afetado
 * @param type tipo da interacao, usado para decidir o peso
 * @param url link do GitHub usado como evidencia no relatorio
 */
public record InteracaoGitHub(
        // Login de quem comentou, revisou, fechou issue ou fez merge.
        String loginOrigem,
        // Login da pessoa relacionada ao artefato atingido pela acao.
        String loginDestino,
        // Tipo da interacao; cada tipo tem um peso definido no enum TipoInteracao.
        TipoInteracao tipo,
        // URL usada como rastreabilidade: prova de onde a interacao veio.
        String url
) {
    // O record e imutavel: depois de criado, esses quatro valores nao mudam.
}
