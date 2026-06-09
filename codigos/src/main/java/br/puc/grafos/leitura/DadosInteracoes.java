package br.puc.grafos.leitura;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Estrutura intermediaria equivalente ao DadosInteracoes do projeto-base.
 */
public class DadosInteracoes {

    private final Set<String> logins = new TreeSet<>();
    private final Map<String, Map<String, Double>> interactions = new TreeMap<>();

    public void adicionarLogin(String login) {
        if (login != null && !login.isBlank()) {
            logins.add(login);
        }
    }

    public void adicionarInteracao(String source, String target) {
        adicionarInteracao(source, target, 1.0);
    }

    public void adicionarInteracao(String source, String target, double weight) {
        if (source == null || target == null || source.isBlank() || target.isBlank()) {
            return;
        }
        adicionarLogin(source);
        adicionarLogin(target);
        if (source.equals(target)) {
            return;
        }
        if (weight <= 0.0) {
            throw new IllegalArgumentException("Peso de interacao deve ser positivo.");
        }

        interactions
                .computeIfAbsent(source, ignored -> new TreeMap<>())
                .merge(target, weight, Double::sum);
    }

    public Set<String> autores() {
        return Collections.unmodifiableSet(logins);
    }

    public Map<String, Map<String, Double>> interacoes() {
        return Collections.unmodifiableMap(interactions);
    }

    public double obterPeso(String source, String target) {
        return interactions.getOrDefault(source, Map.of()).getOrDefault(target, 1.0);
    }
}
