package br.puc.grafos.estatisticas;

import br.puc.grafos.core.graph.AbstractGraph;

import java.nio.file.Path;
import java.util.Map;

/**
 * Contrato de estrategias de calculo de metricas do grafo.
 */
public abstract class EstatisticasGrafo {

    protected final AbstractGraph graph;

    protected EstatisticasGrafo(AbstractGraph graph) {
        this.graph = graph;
    }

    public abstract Map<Integer, Double> calcularCentralidadeGrau();

    public abstract Map<Integer, Double> calcularCentralidadeGrauEntrada();

    public abstract Map<Integer, Double> calcularCentralidadeGrauSaida();

    public abstract Map<Integer, Double> calcularCentralidadeIntermediacao();

    public abstract Map<Integer, Double> calcularCentralidadeProximidade();

    public abstract Map<Integer, Double> calcularPageRank(double alpha, int maxIterations, double tolerance);

    public abstract Map<Integer, Double> calcularCentralidadeAutovetor(int maxIterations, double tolerance);

    public abstract double calcularDensidade();

    public abstract Map<Integer, Double> calcularCoeficienteAglomeracao();

    public abstract double calcularAglomeracaoMedia();

    public abstract double calcularAssortatividade();

    public abstract Map<Integer, Integer> detectarComunidades();

    public abstract double calcularModularidade();

    public abstract Map<Integer, Boolean> identificarVerticesPonte();

    public abstract Map<String, Map<Integer, Object>> calcularTodasMetricas();

    public abstract void exportarMetricasParaCsv(Path nodesOutputFile, Path graphOutputFile);
}
