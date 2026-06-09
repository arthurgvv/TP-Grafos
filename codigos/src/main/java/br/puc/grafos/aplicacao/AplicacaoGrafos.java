package br.puc.grafos.aplicacao;

import br.puc.grafos.arquivos.ArquivosGephi;
import br.puc.grafos.arquivos.ExportadorCsvGephi;
import br.puc.grafos.arquivos.TipoRepresentacaoGrafo;
import br.puc.grafos.core.graph.AbstractGraph;
import br.puc.grafos.leitura.DadosInteracoes;
import br.puc.grafos.leitura.FabricaDadosInteracoes;
import br.puc.grafos.leitura.InterpretadorGrafo;
import br.puc.grafos.leitura.TipoGrafoColaboracao;
import br.puc.grafos.utilitarios.RegistroLog;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Aplicacao principal da versao intermediaria.
 *
 * <p>Nesta etapa o projeto ja monta grafos a partir de JSON local e exporta
 * tabelas para o Gephi. A mineracao real e as metricas completas ficaram
 * separadas para a proxima entrega.</p>
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
            case "demo", "demonstrar" -> DemonstracaoApi.main(commandArgs);
            case "build", "construir" -> construirGrafo(commandArgs);
            case "status" -> imprimirStatus();
            default -> printHelp();
        }
    }

    private static void construirGrafo(String[] args) {
        Path input = Path.of(opcao(args, "-i", "--input", "data/httpx.json"));
        Path output = Path.of(opcao(args, "-o", "--output", "tables"));
        TipoGrafoColaboracao graphType = TipoGrafoColaboracao.fromCodigo(
                opcao(args, "-t", "--type", "integrated"));
        TipoRepresentacaoGrafo representation = TipoRepresentacaoGrafo.fromCodigo(
                opcao(args, "-r", "--representation", "matrix"));

        RegistroLog.info("Construindo grafo " + graphType.getCodigo() + " a partir de " + input + "...");
        DadosInteracoes data = FabricaDadosInteracoes.construir(input, graphType);
        AbstractGraph graph = new InterpretadorGrafo(representation.factory()).obterGrafo(data);

        Path graphOutput = output.resolve(nomeBase(input)).resolve(graphType.getCodigo());
        ArquivosGephi files = ExportadorCsvGephi.exportar(graph, graphOutput);

        RegistroLog.info("Grafo criado com " + graph.getVertexCount() + " vertices e "
                + graph.getEdgeCount() + " arestas.");
        RegistroLog.info("Vertices exportados para " + files.verticesFile());
        RegistroLog.info("Arestas exportadas para " + files.edgesFile());
    }

    private static void imprimirStatus() {
        System.out.println("""
                Versao intermediaria do TP:
                - API propria de grafos pronta.
                - Leitura de JSON local em andamento avancado.
                - Exportacao CSV para Gephi pronta.
                - Mineracao GitHub real e metricas finais ficam para a proxima etapa.
                """);
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

    private static void printHelp() {
        System.out.println("""
                Uso:
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos demo
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos status
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build [-i data/httpx.json] [-o tables] [-t integrated|comments|reviews|closed] [-r matrix|list]
                  java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos construir [-i data/httpx.json] [-o tables] [-t integrated|comments|reviews|closed] [-r matrix|list]

                Planejado para a proxima etapa:
                  fetch/buscar dados reais do GitHub
                  analyze/analisar metricas finais
                """);
    }
}
