# Conformidade com o enunciado

Este documento compara o projeto com os requisitos do PDF.

## Etapa 1 - Modelagem e planejamento

| Requisito | Status | Observacao |
|---|---|---|
| Repositorio publico com mais de 5.000 estrelas | OK | `encode/httpx` |
| Usuarios como vertices | OK | Documentado na modelagem |
| Interacoes como arestas direcionadas | OK | Aresta `acao -> autor do artefato` |
| Grafos separados por tipo de relacao | OK | `comments`, `issue_close`, `reviews_merges` |
| Grafo integrado ponderado | OK | Implementado no builder |
| Pesos de interacao | OK | 2, 3, 4 e 5 |
| Testes para mineracao | Pendente | Sera migrado para Java |

## Etapa 2 - Desenvolvimento da ferramenta

| Requisito | Status | Implementacao |
|---|---|---|
| Classe abstrata `AbstractGraph` | OK | `codigos/src/main/java/br/puc/grafos/core/graph/AbstractGraph.java` |
| Matriz de adjacencia | OK | `AdjacencyMatrixGraph` |
| Lista de adjacencia | OK | `AdjacencyListGraph` |
| `getVertexCount` | OK | Alias e metodo Python |
| `getEdgeCount` | OK | Alias e metodo Python |
| `hasEdge` | OK | Alias e metodo Python |
| `addEdge` | OK | Alias e metodo Python |
| `removeEdge` | OK | Alias e metodo Python |
| `isSucessor` | OK | Alias e metodo Python |
| `isPredessor` | OK | Alias e metodo Python |
| `isDivergent` | OK | Alias e metodo Python |
| `isConvergent` | OK | Alias e metodo Python |
| `isIncident` | OK | Alias e metodo Python |
| `getVertexInDegree` | OK | Alias e metodo Python |
| `getVertexOutDegree` | OK | Alias e metodo Python |
| Pesos de vertices | OK | `set_vertex_weight`, `get_vertex_weight` |
| Pesos de arestas | OK | `set_edge_weight`, `get_edge_weight` |
| `isConnected` | OK | Conectividade forte |
| `isEmptyGraph` | OK | Sem arestas |
| `isCompleteGraph` | OK | `n * (n - 1)` arestas |
| `exportToGEPHI` | OK | CSV importavel no Gephi |
| Sem bibliotecas prontas de grafos | OK | Nao ha biblioteca pronta de grafos |

## Etapa 3 - Analise baseada em dados

| Requisito | Status |
|---|---|
| Centralidade de grau | Pendente |
| Betweenness | Pendente |
| Closeness | Pendente |
| PageRank ou eigenvector | Pendente |
| Densidade | Pendente |
| Clustering coefficient | Pendente |
| Comunidades ou bridging ties | Pendente |
| Relatorio em LaTeX | Pendente |
