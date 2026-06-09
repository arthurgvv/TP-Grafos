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

- [x] Criar cliente HTTP/GraphQL do GitHub em Java.
- [x] Criar minerador de issues em Java.
- [x] Criar minerador de pull requests em Java.
- [x] Extrair comentarios, reviews, fechamento de issue e merge.
- [x] Testar transformacao dos dados com JSON de exemplo.
- [x] Testar cliente de mineracao com `HttpClient` falso.
- [ ] Rodar mineracao real com amostra do `encode/httpx`.

## Fase 4 - Construcao e exportacao dos grafos

- [x] Construir grafo integrado.
- [x] Construir grafos separados por tipo de relacao.
- [x] Exportar grafo para CSV compativel com Gephi.
- [x] Exportar Gephi em duas tabelas: vertices e arestas.
- [x] Recarregar grafo a partir dos CSVs do Gephi.

## Fase 5 - Metricas de analise

- [x] Centralidade de grau.
- [x] Betweenness centrality.
- [x] Closeness centrality.
- [x] PageRank.
- [x] Densidade.
- [x] Clustering coefficient.
- [x] Comunidades ou bridging ties.
- [x] Exportar metricas dos vertices em CSV.
- [x] Exportar metricas gerais em JSON.

## Fase 6 - Relatorio e apresentacao

- [x] Criar fonte do relatorio em LaTeX com template SBC.
- [x] Inserir modelagem e arquitetura no relatorio.
- [ ] Inserir resultados reais obtidos da mineracao.
- [ ] Gerar PDF final do relatorio.
- [ ] Preparar roteiro de apresentacao.
- [ ] Gravar video de demonstracao entre 5 e 10 minutos, se solicitado.
- [ ] Gerar arquivo final `.zip` ou `.rar`.
