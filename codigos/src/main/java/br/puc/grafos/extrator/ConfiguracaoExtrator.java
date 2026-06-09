package br.puc.grafos.extrator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuracao do minerador GitHub, lida de variaveis de ambiente ou .env.
 */
public class ConfiguracaoExtrator {

    private final List<String> tokensAutenticacao;
    private final String urlBase;
    private final String donoRepositorio;
    private final String nomeRepositorio;
    private final int maximoIssues;
    private final int maximoPullRequests;

    public ConfiguracaoExtrator() {
        this(carregarValores());
    }

    ConfiguracaoExtrator(Map<String, String> values) {
        this.tokensAutenticacao = separarTokens(valor(values, "GITHUB_AUTH_TOKENS", valor(values, "GITHUB_TOKEN", "")));
        this.urlBase = valor(values, "GITHUB_BASE_URL", "https://api.github.com");
        this.donoRepositorio = valor(values, "GITHUB_REPO_OWNER", "encode");
        this.nomeRepositorio = valor(values, "GITHUB_REPO_NAME", "httpx");
        this.maximoIssues = converterInteiro(valor(values, "MAX_ISSUES", "20"));
        this.maximoPullRequests = converterInteiro(valor(values, "MAX_PULLS", "10"));
    }

    public List<String> tokensAutenticacao() {
        return tokensAutenticacao;
    }

    public String urlBase() {
        return urlBase;
    }

    public String donoRepositorio() {
        return donoRepositorio;
    }

    public String nomeRepositorio() {
        return nomeRepositorio;
    }

    public int maximoIssues() {
        return maximoIssues;
    }

    public int maximoPullRequests() {
        return maximoPullRequests;
    }

    private static Map<String, String> carregarValores() {
        Map<String, String> values = new HashMap<>(System.getenv());
        Path envFile = Path.of(".env");
        if (Files.exists(envFile)) {
            try {
                for (String line : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                        continue;
                    }
                    int separator = trimmed.indexOf('=');
                    String key = trimmed.substring(0, separator).trim();
                    String value = trimmed.substring(separator + 1).trim();
                    values.putIfAbsent(key, value);
                }
            } catch (IOException exception) {
                throw new IllegalStateException("Nao foi possivel ler .env.", exception);
            }
        }
        return values;
    }

    private static String valor(Map<String, String> values, String key, String fallback) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }
        return values.getOrDefault(key, fallback);
    }

    private static List<String> separarTokens(String raw) {
        List<String> tokens = new ArrayList<>();
        for (String token : raw.split(",")) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                tokens.add(trimmed);
            }
        }
        return List.copyOf(tokens);
    }

    private static int converterInteiro(String raw) {
        return Integer.parseInt(raw.trim());
    }
}
