package br.puc.grafos.extrator;

import br.puc.grafos.utilitarios.JsonSimples;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente GraphQL do GitHub para minerar issues, PRs, comentarios e reviews.
 */
public class ServicoGitHub {

    private static final String REPOSITORY_QUERY = """
            query($owner: String!, $name: String!, $maximoIssues: Int!, $maxPulls: Int!) {
              repository(owner: $owner, name: $name) {
                pullRequests(first: $maxPulls, orderBy: {field: UPDATED_AT, direction: DESC}) {
                  nodes {
                    number
                    url
                    author { login }
                    comments(first: 50) {
                      nodes {
                        url
                        author { login }
                      }
                    }
                    reviews(first: 50) {
                      nodes {
                        url
                        state
                        author { login }
                      }
                    }
                    mergedBy { login }
                    mergeCommit { url }
                  }
                }
                issues(first: $maximoIssues, states: CLOSED, orderBy: {field: UPDATED_AT, direction: DESC}) {
                  nodes {
                    number
                    url
                    author { login }
                    comments(first: 50) {
                      nodes {
                        url
                        author { login }
                      }
                    }
                    timelineItems(first: 50, itemTypes: [CLOSED_EVENT]) {
                      nodes {
                        ... on ClosedEvent {
                          url
                          actor { login }
                        }
                      }
                    }
                  }
                }
              }
            }
            """;

    private final ConfiguracaoExtrator config;
    private final GerenciadorTokens gerenciadorTokens;
    private final HttpClient cliente;

    public ServicoGitHub(ConfiguracaoExtrator config) {
        this(config, HttpClient.newHttpClient());
    }

    ServicoGitHub(ConfiguracaoExtrator config, HttpClient cliente) {
        this.config = config;
        this.gerenciadorTokens = new GerenciadorTokens(config.tokensAutenticacao());
        this.cliente = cliente;
    }

    public Map<String, List<Object>> buscarResumoRepositorio() {
        Map<String, Object> variables = new LinkedHashMap<>();
        variables.put("owner", config.donoRepositorio());
        variables.put("name", config.nomeRepositorio());
        variables.put("maximoIssues", config.maximoIssues());
        variables.put("maxPulls", config.maximoPullRequests());

        Map<String, Object> corpoRequisicao = new LinkedHashMap<>();
        corpoRequisicao.put("query", REPOSITORY_QUERY);
        corpoRequisicao.put("variables", variables);

        Map<String, Object> response = executar(corpoRequisicao);
        Map<String, Object> data = JsonSimples.comoObjeto(response.get("data"));
        Map<String, Object> repository = JsonSimples.comoObjeto(data.get("repository"));

        Map<String, List<Object>> normalized = new LinkedHashMap<>();
        normalized.put("pullRequests", nosDaConexao(repository.get("pullRequests")));
        normalized.put("issues", nosDaConexao(repository.get("issues")));
        return normalized;
    }

    private Map<String, Object> executar(Map<String, Object> corpoRequisicao) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.urlBase() + "/graphql"))
                .header("Authorization", "Bearer " + gerenciadorTokens.tokenAtual())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonSimples.serializar(corpoRequisicao)))
                .build();

        try {
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IllegalStateException("GitHub retornou HTTP " + response.statusCode() + ": " + response.body());
            }

            Map<String, Object> parsed = JsonSimples.comoObjeto(JsonSimples.analisar(response.body()));
            if (parsed.containsKey("errors")) {
                throw new IllegalStateException("GitHub retornou erros GraphQL: " + JsonSimples.serializar(parsed.get("errors")));
            }
            return parsed;
        } catch (IOException exception) {
            throw new IllegalStateException("Falha de rede ao consultar GitHub.", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Consulta GitHub interrompida.", exception);
        }
    }

    private List<Object> nosDaConexao(Object connection) {
        if (connection instanceof Map<?, ?> map && map.get("nodes") instanceof List<?> nodes) {
            return List.copyOf(nodes);
        }
        return List.of();
    }
}
