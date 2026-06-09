package br.puc.grafos.extrator;

import java.util.List;
import java.util.Map;

/**
 * Ponto de entrada do futuro cliente GitHub.
 *
 * <p>A configuracao e o gerenciamento de tokens ja foram separados nesta
 * versao intermediaria. A consulta GraphQL real sera ligada na proxima etapa.</p>
 */
public class ServicoGitHub {

    private final ConfiguracaoExtrator config;
    private final GerenciadorTokens gerenciadorTokens;

    public ServicoGitHub(ConfiguracaoExtrator config) {
        this.config = config;
        this.gerenciadorTokens = new GerenciadorTokens(config.tokensAutenticacao());
    }

    public ConfiguracaoExtrator configuracao() {
        return config;
    }

    public String tokenAtual() {
        return gerenciadorTokens.tokenAtual();
    }

    public Map<String, List<Object>> buscarResumoRepositorio() {
        throw new UnsupportedOperationException(
                "Mineracao GitHub via GraphQL ainda esta em desenvolvimento nesta branch.");
    }
}
