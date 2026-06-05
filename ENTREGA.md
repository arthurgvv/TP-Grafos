# Plano de entrega por fases

Este arquivo acompanha o progresso do trabalho e ajuda a mostrar para o
professor quais partes ja foram implementadas.

## Fase 1 - Fundacao da API de grafos

- [x] Criar `AbstractGraph` em Java.
- [x] Criar `AdjacencyListGraph` em Java.
- [x] Criar `AdjacencyMatrixGraph` em Java.
- [x] Implementar validacao de vertices.
- [x] Impedir lacos.
- [x] Impedir multiplas arestas.
- [x] Acumular peso quando a mesma interacao aparece novamente.
- [x] Criar testes unitarios JUnit para a API obrigatoria.

## Fase 2 - Modelagem do problema

- [x] Escolher repositorio com mais de 5.000 estrelas.
- [x] Justificar escolha do `encode/httpx`.
- [x] Definir vertices como usuarios.
- [x] Definir arestas como interacoes direcionadas.
- [x] Definir pesos por tipo de interacao.
- [x] Criar documento de modelagem.
- [x] Criar diagrama inicial de classes.

## Fase 3 - Mineracao de dados

- [ ] Criar cliente REST do GitHub em Java.
- [ ] Criar minerador de issues em Java.
- [ ] Criar minerador de pull requests em Java.
- [ ] Extrair comentarios, reviews, fechamento de issue e merge.
- [ ] Testar mineracao com cliente falso.
- [ ] Rodar mineracao real com amostra do `encode/httpx`.

## Fase 4 - Construcao e exportacao dos grafos

- [x] Construir grafo integrado.
- [x] Construir grafos separados por tipo de relacao.
- [x] Exportar grafo para CSV compativel com Gephi.
- [ ] Exportar tambem para GEXF ou GraphML.

## Fase 5 - Metricas de analise

- [ ] Centralidade de grau.
- [ ] Betweenness centrality.
- [ ] Closeness centrality.
- [ ] PageRank.
- [ ] Densidade.
- [ ] Clustering coefficient.
- [ ] Comunidades ou bridging ties.

## Fase 6 - Relatorio e apresentacao

- [ ] Criar relatorio em LaTeX com template SBC.
- [ ] Inserir modelagem, diagramas e resultados.
- [ ] Preparar roteiro de apresentacao.
- [ ] Gerar arquivo final `.zip` ou `.rar`.
