# Mapa dos arquivos do projeto

Arthur, use este arquivo como um roteiro rapido para saber o que estudar e o
que e apenas apoio do projeto.

## Codigo que voce deve estudar primeiro

`codigos/src/main/java/br/puc/grafos/core/graph/AbstractGraph.java`

Classe abstrata da API de grafos. Mantive esse nome em ingles porque ele faz
parte da API pedida no PDF. Ela concentra regras comuns: quantidade de vertices,
quantidade de arestas, validacao, grau, sucessor, predecessor, conectividade,
grafo completo e exportacao para Gephi.

`codigos/src/main/java/br/puc/grafos/core/graph/GrafoListaAdjacencia.java`

Implementacao real usando lista de adjacencia. Cada vertice guarda um mapa dos
seus sucessores e pesos. Boa frase: economiza memoria quando o grafo e esparso.

`codigos/src/main/java/br/puc/grafos/core/graph/GrafoMatrizAdjacencia.java`

Implementacao real usando matriz de adjacencia. Cada posicao `matrix[u][v]`
guarda o peso da aresta `u -> v`. Valor zero significa ausencia de aresta.

`codigos/src/main/java/br/puc/grafos/core/graph/AdjacencyListGraph.java`

Classe pequena mantida por compatibilidade com o nome do PDF. Ela herda de
`GrafoListaAdjacencia`.

`codigos/src/main/java/br/puc/grafos/core/graph/AdjacencyMatrixGraph.java`

Classe pequena mantida por compatibilidade com o nome do PDF. Ela herda de
`GrafoMatrizAdjacencia`.

`codigos/src/main/java/br/puc/grafos/core/graph/ArestaGrafo.java`

Record simples usado para representar uma aresta quando percorremos o grafo.

`codigos/src/main/java/br/puc/grafos/core/graph/exceptions/`

Excecoes da API. Agora estao em portugues: `VerticeInvalidoException`,
`ArestaInvalidaException`, `PesoInvalidoException`, `LacoInvalidoException` e
`GrafoException`.

`codigos/src/main/java/br/puc/grafos/github/TipoInteracao.java`

Define os tipos de interacao e os pesos do trabalho: comentario, fechamento,
review e merge.

`codigos/src/main/java/br/puc/grafos/github/InteracaoGitHub.java`

Representa uma interacao do GitHub antes de virar aresta.

`codigos/src/main/java/br/puc/grafos/servicos/ConstrutorGrafoColaboracao.java`

Transforma interacoes em grafos. Este arquivo explica como os usuarios viram
vertices e como as interacoes viram arestas direcionadas e ponderadas.

`codigos/src/main/java/br/puc/grafos/servicos/GrafoConstruido.java`

Record que devolve o grafo pronto junto com o mapa `login -> indice`.

`codigos/src/main/java/br/puc/grafos/aplicacao/DemonstracaoApi.java`

Aplicacao de terminal que demonstra a API usando lista e matriz.

`codigos/src/main/java/br/puc/grafos/aplicacao/AplicacaoGrafos.java`

Aplicacao principal no padrao do projeto-base. Possui os comandos `fetch`,
`build` e `analyze`.

`codigos/src/main/java/br/puc/grafos/extrator/`

Camada de mineracao GitHub em Java. Inclui configuracao, gerenciamento simples
de token e servico GraphQL.

`codigos/src/main/java/br/puc/grafos/leitura/`

Camada equivalente ao `parser.py` do projeto-base. Le JSON do GitHub, cria
`DadosInteracoes` e transforma as interacoes em grafo com `InterpretadorGrafo`.

`codigos/src/main/java/br/puc/grafos/arquivos/`

Camada de entrada/saida. Exporta `graph_vertexes.csv` e `graph_edges.csv` para
Gephi e recarrega esses arquivos com `FabricaGrafo`.

`codigos/src/main/java/br/puc/grafos/estatisticas/`

Estrategia manual de metricas. Calcula centralidades, densidade, clustering,
PageRank, comunidades, modularidade e nos de ponte.

`codigos/src/main/java/br/puc/grafos/utilitarios/`

Utilitarios pequenos de JSON, CSV e log. Eles existem para evitar dependencia
externa e manter o projeto facil de explicar.

## Testes que ajudam a estudar

`codigos/src/test/java/br/puc/grafos/core/graph/ApiGrafoTest.java`

Testa a API obrigatoria de grafos. Serve como lista de perguntas provaveis do
professor.

`codigos/src/test/java/br/puc/grafos/servicos/ConstrutorGrafoColaboracaoTest.java`

Testa se as interacoes do GitHub viram grafos corretamente.

`codigos/src/test/java/br/puc/grafos/leitura/FabricaDadosInteracoesTest.java`

Testa se um JSON no formato do GitHub vira grafos `integrated`, `comments`,
`reviews` e `closed`.

`codigos/src/test/java/br/puc/grafos/arquivos/FabricaGrafoTest.java`

Testa exportacao e leitura dos CSVs do Gephi.

`codigos/src/test/java/br/puc/grafos/estatisticas/EstatisticasGrafoManualTest.java`

Testa metricas principais e exportacao de `nodes.csv` e `graph.json`.

`codigos/src/test/java/br/puc/grafos/extrator/ServicoGitHubTest.java`

Testa a mineracao usando um `HttpClient` falso, sem chamar a internet.

## Configuracao importante, mas nao e codigo de grafo

`pom.xml`

Configura Maven, Java 21, JUnit e as pastas `codigos/src/main/java` e
`codigos/src/test/java`.

`.gitignore`

Diz ao Git quais arquivos nao devem ser enviados para o repositorio, como
arquivos gerados e pastas temporarias.

## Documentacao util

`README.md`

Resumo do projeto, repositorio escolhido, regras e comandos principais.

`EXECUTAR.md`

Passo a passo para rodar testes e demo.

`GUIA_ESTUDO_CODIGO.md`

Explicacao em ordem de estudo, com frases prontas para responder ao professor.

`CONFORMIDADE_PDF.md`

Lista de conformidade com o enunciado do PDF.

`ENTREGA.md`

Organiza o que precisa estar pronto em cada etapa.

`STATUS.md`

Mostra o estado atual do projeto.

`PROGRESSO.txt`

Registro simples do que ja foi feito.

## Modelagem e relatorio

`modelagem/modelagem_encode_httpx.md`

Documento de modelagem do repositorio escolhido.

`modelagem/diagrama_de_classes.puml`

Diagrama PlantUML das classes. Ajuda a explicar heranca e relacionamento entre
as classes.

`relatorio/README.md`

Espaco reservado para o relatorio. Nao e codigo.

`relatorio/main.tex`

Fonte do relatorio no template SBC. Precisa ser compilado para gerar o PDF
final depois que os resultados reais forem inseridos.

## Arquivos gerados automaticamente

`target/`

Pasta criada pelo Maven. Nao precisa estudar como codigo-fonte. Ela contem
classes compiladas, relatorios de teste e metadados de build.

No VS Code, essa pasta fica escondida pela configuracao `.vscode/settings.json`
para evitar que voce abra sem querer os arquivos compilados.

`target/classes/**/*.class`

Arquivos `.class` sao bytecode Java compilado. Voce nao edita isso.

`target/test-classes/**/*.class`

Bytecode compilado dos testes. Tambem nao se edita.

`target/surefire-reports/`

Relatorios gerados quando rodamos `mvn test`.

`target/maven-status/`

Controle interno do Maven para saber o que ja foi compilado.

## Se aparecerem arquivos Python antigos

`__pycache__/`

Cache automatico do Python. Nao e codigo para estudar.

`*.pyc`

Arquivo Python compilado/cacheado. Nao se edita.

`*.egg-info/`

Metadados de pacote Python. Tambem nao e codigo do trabalho.
