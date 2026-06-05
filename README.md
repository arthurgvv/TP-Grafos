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
    app/                 # demos e comandos de execucao
    core/graph/          # API propria de grafos
    github/              # modelos de interacao do GitHub
    services/            # transformacao de interacoes em grafos
  src/test/java/br/puc/grafos/
modelagem/               # modelagem textual e diagramas
relatorio/               # espaco para o relatorio em LaTeX
```

## Fases do desenvolvimento

1. Fundacao da API de grafos: pronta.
2. Modelagem do repositorio `encode/httpx`: pronta.
3. Modelos de interacao e construcao de grafos: pronta.
4. Exportacao para Gephi: pronta em CSV.
5. Mineracao real do GitHub em Java: proxima fase.
6. Relatorio em LaTeX e resultados reais: pendente.

## Regras importantes do trabalho

- O grafo e simples: sem lacos e sem multiplas arestas.
- As arestas sao direcionadas.
- A API de grafos foi implementada manualmente.
- Bibliotecas prontas de grafos, como `networkx`, nao devem ser usadas.
- `add_edge(u, v)` nao duplica arestas. Quando a relacao ja existe, o peso e acumulado para representar mais intensidade de interacao.

## Pesos usados no grafo integrado

| Interacao | Peso |
|---|---:|
| Comentario em pull request | 2 |
| Comentario em issue | 3 |
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
java -cp target/classes br.puc.grafos.app.DemonstracaoApi
```

Observacao: este projeto nao usa bibliotecas prontas de grafos.
