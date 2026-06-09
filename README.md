# TP Grafos - Analise de Colaboracao no GitHub

Trabalho pratico da disciplina de Teoria de Grafos e Computabilidade.

O objetivo e modelar a colaboracao em um repositorio real do GitHub usando grafos direcionados e ponderados. Cada usuario sera representado como um vertice, e cada interacao entre usuarios sera representada como uma aresta.

Implementacao oficial: **Java 21 com Maven**.

## Repositorio escolhido

Repositorio base: `encode/httpx`

Motivos da escolha:

- tem mais de 5.000 estrelas;
- e um projeto Python real e conhecido;
- possui issues, pull requests, comentarios e revisoes;
- o dominio e simples de explicar: um cliente HTTP moderno para Python;
- o tamanho e bom para mineracao, sem ser tao gigantesco quanto projetos como `fastapi/fastapi`.

## Estrutura

```text
codigos/
  src/main/java/br/puc/grafos/
    aplicacao/           # demos e comandos de execucao
    core/graph/          # API propria de grafos
    extrator/            # mineracao GitHub via GraphQL em Java
    github/              # modelos de interacao do GitHub
    arquivos/            # exportacao/importacao Gephi
    leitura/             # JSON do GitHub -> interacoes -> grafo
    servicos/            # transformacao de interacoes em grafos
    estatisticas/        # estrategia manual de metricas
    utilitarios/         # JSON, CSV e log simples
  src/test/java/br/puc/grafos/
modelagem/               # modelagem textual e diagramas
relatorio/               # relatorio em LaTeX com template SBC
```

## Arquitetura seguida do projeto-base

O projeto agora replica em Java os principais padroes do trabalho-base em Python:

- comando principal com `fetch`/`buscar`, `build`/`construir` e `analyze`/`analisar`;
- camada `extrator` para buscar dados do GitHub;
- `FabricaDadosInteracoes` para criar os tipos de grafo;
- `InterpretadorGrafo` para transformar interacoes em `AbstractGraph`;
- `FabricaGrafo.deGephi(...)` para recarregar CSVs;
- estrategia `EstatisticasGrafoManual` para calcular metricas sem biblioteca pronta de grafos;
- exportacao Gephi em `graph_vertexes.csv` e `graph_edges.csv`.

## Fases do desenvolvimento

1. Fundacao da API de grafos: pronta.
2. Modelagem do repositorio `encode/httpx`: pronta.
3. Modelos de interacao e construcao de grafos: pronta.
4. Exportacao para Gephi: pronta em CSV simples e em duas tabelas.
5. Mineracao GitHub em Java: estrutura pronta via comando `fetch`.
6. Metricas de rede em Java: estrategia manual pronta.
7. Fonte do relatorio em LaTeX: pronto; resultados reais e PDF final: pendentes.

## Regras importantes do trabalho

- O grafo e simples: sem lacos e sem multiplas arestas.
- As arestas sao direcionadas.
- A API de grafos foi implementada manualmente.
- Bibliotecas prontas de grafos, como `networkx`, nao devem ser usadas.
- `add_edge(u, v)` nao duplica arestas. Quando a relacao ja existe, o peso e acumulado para representar mais intensidade de interacao.

## Pesos usados no grafo integrado

| Interacao | Peso |
|---|---:|
| Comentario em issue ou pull request | 2 |
| Abertura de issue comentada por outro usuario | 3 |
| Fechamento de issue por outro usuario | 3 |
| Revisao/aprovacao de pull request | 4 |
| Merge de pull request | 5 |

## Como rodar os testes

```bash
mvn test
```

## Como rodar a demo da API

```bash
mvn test
java -cp target/classes br.puc.grafos.aplicacao.DemonstracaoApi
```

## Como rodar a arquitetura completa

```bash
# Buscar dados reais do GitHub, usando GITHUB_TOKEN no .env
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos fetch
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos buscar

# Construir um grafo e exportar para Gephi
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build -i data/httpx.json -o tables -t integrated -r matrix
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos construir -i data/httpx.json -o tables -t integrated -r matrix

# Calcular metricas do grafo exportado
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analyze -i tables/httpx/integrated -o statistics -s manual
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analisar -i tables/httpx/integrated -o statistics -s manual
```

Observacao: este projeto nao usa bibliotecas prontas de grafos.
