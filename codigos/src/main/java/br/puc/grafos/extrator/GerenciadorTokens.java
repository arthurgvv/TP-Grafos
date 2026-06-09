package br.puc.grafos.extrator;

import java.util.List;

/**
 * Gerencia um ou mais tokens do GitHub.
 */
public class GerenciadorTokens {

    private final List<String> tokens;
    private int currentIndex;

    public GerenciadorTokens(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("Configure GITHUB_TOKEN ou GITHUB_AUTH_TOKENS para usar o comando fetch.");
        }
        this.tokens = List.copyOf(tokens);
    }

    public String tokenAtual() {
        return tokens.get(currentIndex);
    }

    public void alternar() {
        currentIndex = (currentIndex + 1) % tokens.size();
    }
}
