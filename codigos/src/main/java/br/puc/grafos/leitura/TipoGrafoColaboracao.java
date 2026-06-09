package br.puc.grafos.leitura;

import java.util.Arrays;

/**
 * Tipos de grafo aceitos pelo comando build.
 */
public enum TipoGrafoColaboracao {
    INTEGRATED("integrated"),
    COMMENTS("comments"),
    REVIEWS("reviews"),
    CLOSED("closed");

    private final String codigo;

    TipoGrafoColaboracao(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoGrafoColaboracao fromCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(tipo -> tipo.codigo.equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipo de grafo invalido: " + codigo + ". Use integrated, comments, reviews ou closed."));
    }
}
