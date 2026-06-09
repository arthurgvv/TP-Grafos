package br.puc.grafos.aplicacao;

import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.extrator.ConfiguracaoExtrator;
import br.puc.grafos.extrator.BuscadorDadosGitHub;
import br.puc.grafos.extrator.ServicoGitHub;
import br.puc.grafos.arquivos.ArquivosGephi;
import br.puc.grafos.arquivos.ExportadorCsvGephi;
import br.puc.grafos.arquivos.FabricaGrafo;
import br.puc.grafos.arquivos.TipoRepresentacaoGrafo;
import br.puc.grafos.leitura.InterpretadorGrafo;
import br.puc.grafos.leitura.DadosInteracoes;
import br.puc.grafos.leitura.FabricaDadosInteracoes;
import br.puc.grafos.leitura.TipoGrafoColaboracao;
import br.puc.grafos.estatisticas.EstatisticasGrafoManual;
import br.puc.grafos.utilitarios.RegistroLog;
import br.puc.grafos.utilitarios.JsonSimples;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Aplicacao principal com comandos de mineracao, construcao e analise.
 */
public class AplicacaoGrafos {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (command) {
            case "fetch", "buscar" -> buscarDados();
            case "build", "construir" -> construirGrafo(commandArgs);
            case "analyze", "analisar" -> analisarGrafo(commandArgs);
            default -> printHelp();
        }
    }

    private static void buscarDados() {
        ConfiguracaoExtrator config = new ConfiguracaoExtrator();
        ServicoGitHub service = new ServicoGitHub(config);

        RegistroLog.info("Buscando dados do GitHub em " + config.donoRepositorio() + "/" + config.nomeRepositorio() + "...");
        Map<String, List<Object>> data = BuscadorDadosGitHub.buscarTudo(service);

        Path output = Path.of("data", config.nomeRepositorio() + ".json");
        JsonSimples.escreverArquivo(output, data);
        RegistroLog.info("Dados salvos em " + output);
    }

    private static void construirGrafo(String[] args) {
        Path input = Path.of(opcao(args, "-i", "--input", "data/node.json"));
        Path output = Path.of(opcao(args, "-o", "--output", "tables"));
        TipoGrafoColaboracao graphType = TipoGrafoColaboracao.fromCodigo(opcao(args, "-t", "--type", "integrated"));
        TipoRepresentacaoGrafo representation = TipoRepresentacaoGrafo.fromCodigo(
                opcao(args, "-r", "--representation", "matrix"));

        RegistroLog.info("Construindo grafo " + graphType.getCodigo() + " a partir de " + input + "...");
        DadosInteracoes data = FabricaDadosInteracoes.construir(input, graphType);
        AbstractGraph graph = new InterpretadorGrafo(representation.factory()).obterGrafo(data);
        RegistroLog.info("Grafo criado com " + graph.getVertexCount() + " vertices e " + graph.getEdgeCount() + " arestas.");

        Path graphOutput = output.resolve(nomeBase(input)).resolve(graphType.getCodigo());
        ArquivosGephi files = ExportadorCsvGephi.exportar(graph, graphOutput);
        RegistroLog.info("Vertices exportados para " + files.verticesFile());
        RegistroLog.info("Arestas exportadas para " + files.edgesFile());
    }

    private static void analisarGrafo(String[] args) {
        String inputRaw = opcao(args, "-i", "--input", null);
        if (inputRaw == null) {
            throw new IllegalArgumentException("Informe -i ou --input para o comando analyze.");
        }

        Path input = Path.of(inputRaw);
        Path output = Path.of(opcao(args, "-o", "--output", "statistics"));
        String strategy = opcao(args, "-s", "--strategy", "manual");
        if (!strategy.equals("manual") && !strategy.equals("both")) {
            throw new IllegalArgumentException("Em Java, a estrategia disponivel e manual.");
        }

        RegistroLog.info("Carregando grafo Gephi de " + input + "...");
        AbstractGraph graph = FabricaGrafo.deGephi(input, TipoRepresentacaoGrafo.LIST);
        RegistroLog.info("Analisando grafo com estrategia manual.");

        NomesSaida names = nomesSaida(input);
        Path strategyDir = output.resolve("manual").resolve(names.nomeBase()).resolve(names.tipoProcessamento());
        Path nodesMetricsFile = strategyDir.resolve("nodes.csv");
        Path graphMetricsFile = strategyDir.resolve("graph.json");

        new EstatisticasGrafoManual(graph).exportarMetricasParaCsv(nodesMetricsFile, graphMetricsFile);
        RegistroLog.info("Metricas dos vertices exportadas para " + nodesMetricsFile);
        RegistroLog.info("Metricas gerais exportadas para " + graphMetricsFile);
    }

    private static String opcao(String[] args, String shortName, String longName, String fallback) {
        for (int i = 0; i < args.length; i++) {
            if ((args[i].equals(shortName) || args[i].equals(longName)) && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return fallback;
    }

    private static String nomeBase(Path path) {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(0, dot) : fileName;
    }

    private static NomesSaida nomesSaida(Path input) {
        if (Files.isDirectory(input)) {
            Path parent = input.getParent();
            String nomeBase = parent == null ? "graph" : parent.getFileName().toString();
            return new NomesSaida(nomeBase, input.getFileName().toString());
        }

        Path parent = input.getParent();
        Path grandParent = parent == null ? null : parent.getParent();
        String tipoProcessamento = parent == null ? "graph" : parent.getFileName().toString();
        String nomeBase = grandParent == null ? "graph" : grandParent.getFileName().toString();
        return new NomesSaida(nomeBase, tipoProcessamento);
    }

    private static void printHelp() {
        System.out.println("""
                Uso:
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos fetch
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos buscar
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build [-i data/node.json] [-o tables] [-t integrated|comments|reviews|closed] [-r matrix|list]
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos construir [-i data/node.json] [-o tables] [-t integrated|comments|reviews|closed] [-r matrix|list]
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analyze -i tables/node/integrated [-o statistics] [-s manual]
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analisar -i tables/node/integrated [-o statistics] [-s manual]
                """);
    }

    private record NomesSaida(
            String nomeBase,
            String tipoProcessamento
    ) {
    }
}
