# Conformidade com o enunciado do PDF

Este documento compara o projeto com os requisitos extraidos de
`C:/Users/gv/Downloads/tp-es (2).pdf`.

## Etapa 1 - Modelagem e planejamento

| Requisito | Status | Observacao |
|---|---|---|
| Repositorio publico com mais de 5.000 estrelas | OK | `encode/httpx` |
| Usuarios como vertices | OK | Documentado na modelagem e no relatorio |
| Interacoes como arestas direcionadas | OK | Aresta representa a acao tecnica entre usuarios |
| Grafo simples e direcionado | OK | Sem lacos e sem multiplas arestas |
| Arestas antiparalelas quando houver relacao bidirecional | OK | A API permite `u -> v` e `v -> u` como arestas distintas |
| Comentarios em issues | OK | Mineracao e parser contemplam comentarios |
| Fechamento de issues | OK | Mineracao e parser contemplam eventos de fechamento |
| Comentarios em pull requests | OK | Mineracao e parser contemplam comentarios |
| Abertura, revisao, aprovacao e merge de pull requests | OK | Abertura e tratada pelo autor do PR; revisoes/aprovacoes e merge entram como interacoes |
| Grafos separados por tipo de relacao | OK | `comments`, `closed`, `reviews`, alem do integrado |
| Grafo integrado ponderado | OK | Implementado em `FabricaDadosInteracoes` |
| Comentario em issue ou PR com peso 2 | OK | `TipoInteracao.COMENTARIO_ISSUE` e `COMENTARIO_PR` |
| Abertura de issue comentada por outro usuario com peso 3 | OK | `ABERTURA_ISSUE_COMENTADA` |
| Revisao/aprovacao de PR com peso 4 | OK | `REVISAO_PR` |
| Merge de PR com peso 5 | OK | `MERGE_PR` |
| Documento de modelagem e plano | OK | `modelagem/`, `ENTREGA.md` e `relatorio/main.tex` |
| Testes unitarios da mineracao | OK | `ServicoGitHubTest` usa `HttpClient` falso; parser tambem e testado |

## Etapa 2 - Desenvolvimento da ferramenta

| Requisito | Status | Implementacao |
|---|---|---|
| Classe abstrata `AbstractGraph` | OK | Mantida com nome exigido pelo PDF |
| Classe `AdjacencyMatrixGraph` | OK | Wrapper exigido; implementacao em `GrafoMatrizAdjacencia` |
| Classe `AdjacencyListGraph` | OK | Wrapper exigido; implementacao em `GrafoListaAdjacencia` |
| Construtores com `int numVertices` | OK | Presentes nas duas implementacoes |
| `getVertexCount` | OK | Implementado |
| `getEdgeCount` | OK | Implementado |
| `hasEdge` | OK | Implementado |
| `addEdge` | OK | Implementado e idempotente |
| `removeEdge` | OK | Implementado |
| `isSucessor` | OK | Implementado com grafia do PDF |
| `isPredessor` | OK | Implementado com grafia do PDF |
| `isDivergent` | OK | Implementado |
| `isConvergent` | OK | Implementado |
| `isIncident` | OK | Implementado |
| `getVertexInDegree` | OK | Implementado |
| `getVertexOutDegree` | OK | Implementado |
| `setVertexWeight` e `getVertexWeight` | OK | Implementados |
| `setEdgeWeight` e `getEdgeWeight` | OK | Implementados |
| `isConnected` | OK | Conectividade forte em grafo direcionado |
| `isEmptyGraph` | OK | Verdadeiro quando nao ha arestas |
| `isCompleteGraph` | OK | Usa `n * (n - 1)` |
| `exportToGEPHI(String path)` | OK | CSV compativel com Gephi |
| Excecoes para indices invalidos e operacoes inconsistentes | OK | Pacote `exceptions` |
| Proibicao de lacos | OK | Testado |
| Proibicao de multiplas arestas | OK | Aresta repetida acumula peso |
| Proibicao de bibliotecas prontas de grafos | OK | Nao ha NetworkX ou similar |
| Aplicacao separada consumindo a API | OK | `DemonstracaoApi` e `AplicacaoGrafos` |
| Testes da API e plano de aceitacao | OK | JUnit e documentos de execucao |

## Etapa 3 - Analise baseada em dados

| Requisito | Status | Observacao |
|---|---|---|
| Centralidade de grau | OK | `EstatisticasGrafoManual` |
| Betweenness centrality | OK | Implementacao manual |
| Closeness centrality | OK | Implementacao manual |
| PageRank ou eigenvector | OK | Ambos implementados |
| Densidade | OK | Implementada |
| Clustering coefficient | OK | Implementado |
| Assortatividade | OK | Implementada |
| Deteccao de comunidades/modularidade | OK | Implementada |
| Bridging ties | OK | Vertices de ponte implementados |
| Relatorio em LaTeX com template SBC | Parcial | Fonte criado em `relatorio/main.tex`; falta gerar PDF localmente |
| Relatorio entre 7 e 15 paginas | Pendente | Confirmar apos compilar o PDF |
| Resultados reais no relatorio | Pendente | Exige rodar `fetch`, `build` e `analyze` com token GitHub |
| Apresentacao oral/demonstracao | Pendente | Preparar roteiro/slides se solicitado |
| Video entre 5 e 10 minutos | Pendente | Gravar apos gerar resultados |
| Todos os membros participarem | OK | Relatorio indica Arthur, Amanda e Pedro, com responsabilidades individuais |
| Arquivo `.zip` ou `.rar` com codigo, `.tex` e PDF | Pendente | Deve ser feito no fechamento da entrega |

## Alerta de entrega final

O codigo esta em conformidade com a API, modelagem, mineracao testavel,
exportacao e metricas. Para evitar nota zero pela regra final do PDF, ainda
faltam os artefatos finais: dados reais, resultados no relatorio, PDF do
relatorio, apresentacao/video e pacote `.zip` ou `.rar`.
