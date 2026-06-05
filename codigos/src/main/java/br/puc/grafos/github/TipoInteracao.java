// Este pacote representa dados e regras das interacoes coletadas no GitHub.
package br.puc.grafos.github;

/**
 * Enumera os tipos de interacao do trabalho e o peso de cada um.
 *
 * <p>O professor pode perguntar por que estes pesos existem. A resposta e:
 * eles transformam tipos diferentes de colaboracao em numeros comparaveis no
 * grafo integrado. Comentarios contam menos, reviews contam mais, e merge conta
 * mais ainda porque e uma acao forte de integracao de codigo.</p>
 */
public enum TipoInteracao {
    // Comentario em issue: no enunciado vale peso 3.
    COMENTARIO_ISSUE("issue_comment", 3.0),
    // Comentario em pull request: no enunciado vale peso 2.
    COMENTARIO_PR("pr_comment", 2.0),
    // Fechamento de issue: no enunciado vale peso 3.
    FECHAMENTO_ISSUE("issue_close", 3.0),
    // Review de pull request: no enunciado vale peso 4.
    REVISAO_PR("pr_review", 4.0),
    // Merge de pull request: no enunciado vale peso 5.
    MERGE_PR("pr_merge", 5.0);

    // Codigo textual curto para exportacao, logs ou identificacao.
    private final String codigo;
    // Peso numerico usado quando a interacao vira aresta no grafo.
    private final double peso;

    // Construtor de cada valor do enum; e chamado automaticamente nas constantes acima.
    TipoInteracao(String codigo, double peso) {
        // Guarda o codigo textual daquele tipo de interacao.
        this.codigo = codigo;
        // Guarda o peso daquele tipo de interacao.
        this.peso = peso;
    }

    // Retorna o codigo textual, por exemplo "pr_merge".
    public String getCodigo() {
        // Devolve o campo privado code.
        return codigo;
    }

    // Retorna o peso numerico, por exemplo 5.0 para merge.
    public double getPeso() {
        // Devolve o campo privado weight.
        return peso;
    }
}
