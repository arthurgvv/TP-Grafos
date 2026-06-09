package br.puc.grafos.arquivos;

import java.nio.file.Path;

/**
 * Caminhos dos dois CSVs exportados para o Gephi.
 */
public record ArquivosGephi(
        Path verticesFile,
        Path edgesFile
) {
}
