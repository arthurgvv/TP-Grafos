package br.puc.grafos.leitura;

import br.puc.grafos.github.TipoInteracao;
import br.puc.grafos.utilitarios.JsonSimples;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Extrai relacoes de colaboracao a partir do JSON minerado do GitHub.
 */
public final class FabricaDadosInteracoes {

    private FabricaDadosInteracoes() {
    }

    public static DadosInteracoes construir(Path filePath, TipoGrafoColaboracao graphType) {
        Map<String, Object> root = repositoryRoot(JsonSimples.lerArquivo(filePath));
        return switch (graphType) {
            case INTEGRATED -> construirGrafoIntegradoPonderado(root);
            case COMMENTS -> construirGrafoComentariosIssuesPullRequests(root);
            case REVIEWS -> construirGrafoRevisoesMergesPullRequests(root);
            case CLOSED -> construirGrafoFechamentoIssues(root);
        };
    }

    public static DadosInteracoes construirGrafoFechamentoIssues(Path filePath) {
        return construirGrafoFechamentoIssues(repositoryRoot(JsonSimples.lerArquivo(filePath)));
    }

    public static DadosInteracoes construirGrafoRevisoesMergesPullRequests(Path filePath) {
        return construirGrafoRevisoesMergesPullRequests(repositoryRoot(JsonSimples.lerArquivo(filePath)));
    }

    public static DadosInteracoes construirGrafoComentariosIssuesPullRequests(Path filePath) {
        return construirGrafoComentariosIssuesPullRequests(repositoryRoot(JsonSimples.lerArquivo(filePath)));
    }

    public static DadosInteracoes construirGrafoIntegradoPonderado(Path filePath) {
        return construirGrafoIntegradoPonderado(repositoryRoot(JsonSimples.lerArquivo(filePath)));
    }

    private static DadosInteracoes construirGrafoFechamentoIssues(Map<String, Object> root) {
        DadosInteracoes data = new DadosInteracoes();
        for (Map<String, Object> issue : itemsFrom(root, "issues")) {
            String author = loginFromField(issue, "author");
            data.adicionarLogin(author);
            for (Map<String, Object> event : itemsFrom(issue, "timelineItems")) {
                String actor = loginFromField(event, "actor");
                data.adicionarInteracao(actor, author);
            }
        }
        return data;
    }

    private static DadosInteracoes construirGrafoRevisoesMergesPullRequests(Map<String, Object> root) {
        DadosInteracoes data = new DadosInteracoes();
        for (Map<String, Object> pullRequest : itemsFrom(root, "pullRequests")) {
            String author = loginFromField(pullRequest, "author");
            data.adicionarLogin(author);

            for (Map<String, Object> review : itemsFrom(pullRequest, "reviews")) {
                String reviewer = loginFromField(review, "author");
                data.adicionarInteracao(reviewer, author);
            }

            String merger = loginFromField(pullRequest, "mergedBy");
            data.adicionarInteracao(merger, author);
        }
        return data;
    }

    private static DadosInteracoes construirGrafoComentariosIssuesPullRequests(Map<String, Object> root) {
        DadosInteracoes data = new DadosInteracoes();

        for (Map<String, Object> pullRequest : itemsFrom(root, "pullRequests")) {
            String author = loginFromField(pullRequest, "author");
            data.adicionarLogin(author);
            for (Map<String, Object> comment : itemsFrom(pullRequest, "comments")) {
                String commenter = loginFromField(comment, "author");
                data.adicionarInteracao(commenter, author);
            }
        }

        for (Map<String, Object> issue : itemsFrom(root, "issues")) {
            String author = loginFromField(issue, "author");
            data.adicionarLogin(author);
            for (Map<String, Object> comment : itemsFrom(issue, "comments")) {
                String commenter = loginFromField(comment, "author");
                data.adicionarInteracao(commenter, author);
            }
        }

        return data;
    }

    private static DadosInteracoes construirGrafoIntegradoPonderado(Map<String, Object> root) {
        DadosInteracoes data = new DadosInteracoes();

        for (Map<String, Object> pullRequest : itemsFrom(root, "pullRequests")) {
            String author = loginFromField(pullRequest, "author");
            data.adicionarLogin(author);

            for (Map<String, Object> comment : itemsFrom(pullRequest, "comments")) {
                String commenter = loginFromField(comment, "author");
                data.adicionarInteracao(commenter, author, TipoInteracao.COMENTARIO_PR.getPeso());
            }

            for (Map<String, Object> review : itemsFrom(pullRequest, "reviews")) {
                String reviewer = loginFromField(review, "author");
                data.adicionarInteracao(reviewer, author, TipoInteracao.REVISAO_PR.getPeso());
            }

            String merger = loginFromField(pullRequest, "mergedBy");
            data.adicionarInteracao(merger, author, TipoInteracao.MERGE_PR.getPeso());
        }

        for (Map<String, Object> issue : itemsFrom(root, "issues")) {
            String author = loginFromField(issue, "author");
            data.adicionarLogin(author);

            for (Map<String, Object> comment : itemsFrom(issue, "comments")) {
                String commenter = loginFromField(comment, "author");
                data.adicionarInteracao(commenter, author, TipoInteracao.COMENTARIO_ISSUE.getPeso());
                data.adicionarInteracao(author, commenter, TipoInteracao.ABERTURA_ISSUE_COMENTADA.getPeso());
            }

            for (Map<String, Object> event : itemsFrom(issue, "timelineItems")) {
                String actor = loginFromField(event, "actor");
                data.adicionarInteracao(actor, author, TipoInteracao.FECHAMENTO_ISSUE.getPeso());
            }
        }

        return data;
    }

    private static Map<String, Object> repositoryRoot(Object value) {
        Map<String, Object> root = JsonSimples.comoObjeto(value);
        if (root.get("data") instanceof Map<?, ?> data) {
            root = castObject(data);
        }
        if (root.get("repository") instanceof Map<?, ?> repository) {
            root = castObject(repository);
        }
        return root;
    }

    private static List<Map<String, Object>> itemsFrom(Map<String, Object> object, String key) {
        return objectList(object.get(key));
    }

    private static List<Map<String, Object>> objectList(Object value) {
        if (value == null) {
            return List.of();
        }
        if (value instanceof Map<?, ?> map) {
            Object nodes = map.get("nodes");
            if (nodes != null) {
                return objectList(nodes);
            }
            return List.of(castObject(map));
        }
        if (value instanceof List<?> list) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map<?, ?> map) {
                    result.add(castObject(map));
                }
            }
            return result;
        }
        return List.of();
    }

    private static String loginFromField(Map<String, Object> object, String key) {
        return loginFromValue(object.get(key));
    }

    private static String loginFromValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            Object login = map.get("login");
            return login == null ? null : String.valueOf(login);
        }
        if (value instanceof String text) {
            return text;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castObject(Map<?, ?> map) {
        return (Map<String, Object>) map;
    }
}
