# Mapa de arquivos

## Codigo principal

`codigos/src/main/java/br/puc/grafos/core/graph/`

API propria de grafos, implementada com lista e matriz de adjacencia.

`codigos/src/main/java/br/puc/grafos/github/`

Records e enums que representam interacoes do GitHub e seus pesos.

`codigos/src/main/java/br/puc/grafos/servicos/`

Construcao dos grafos de colaboracao a partir das interacoes.

`codigos/src/main/java/br/puc/grafos/leitura/`

Leitura de JSON local e transformacao em `DadosInteracoes`.

`codigos/src/main/java/br/puc/grafos/arquivos/`

Exportacao e leitura de CSVs no formato usado pelo Gephi.

`codigos/src/main/java/br/puc/grafos/extrator/`

Esqueleto do minerador GitHub. A configuracao e tokens ja estao separados, mas
a consulta GraphQL real ainda esta pendente nesta branch.

`codigos/src/main/java/br/puc/grafos/aplicacao/`

Classe de demonstracao e comando principal da versao intermediaria.

`codigos/src/main/java/br/puc/grafos/utilitarios/`

Utilitarios simples para JSON, CSV e logs sem dependencias extras.

## Testes

`codigos/src/test/java/br/puc/grafos/core/graph/`

Testes da API obrigatoria.

`codigos/src/test/java/br/puc/grafos/servicos/`

Testes da construcao dos grafos de colaboracao.

`codigos/src/test/java/br/puc/grafos/leitura/`

Testes da leitura de JSON local.

`codigos/src/test/java/br/puc/grafos/arquivos/`

Testes de exportacao e recarregamento dos CSVs.

## Documentos

`modelagem/`

Modelagem textual e diagrama.

`relatorio/`

Espaco reservado para o relatorio.

`target/`

Saida gerada pelo Maven. Nao e codigo-fonte.
