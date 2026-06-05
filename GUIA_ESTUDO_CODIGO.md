# Guia de estudo do codigo Java

Arthur, este guia explica o codigo em uma ordem boa para estudar e responder o
professor. A implementacao oficial agora esta em Java e a maioria dos nomes
proprios do nosso codigo ficou em portugues.

## Ideia central do trabalho

O projeto analisa colaboracao no repositorio `encode/httpx`.

- Usuario do GitHub = vertice.
- Interacao entre usuarios = aresta direcionada.
- Peso da aresta = importancia/intensidade da interacao.

Exemplo:

```text
Arthur revisa um pull request de Victor
Arthur -> Victor, peso 4
```

Frase pronta:

> O grafo e direcionado porque a acao tem sentido. Quem faz a review e a origem
> da aresta; quem criou o pull request e o destino.

## Nomes em portugues e nomes mantidos do PDF

Mantivemos `AbstractGraph`, `AdjacencyListGraph` e `AdjacencyMatrixGraph`
porque esses nomes fazem parte da API pedida no enunciado. Para estudar com
mais facilidade, as implementacoes reais usam nomes em portugues:

- `GrafoListaAdjacencia` implementa lista de adjacencia.
- `GrafoMatrizAdjacencia` implementa matriz de adjacencia.
- `AdjacencyListGraph` apenas herda de `GrafoListaAdjacencia`.
- `AdjacencyMatrixGraph` apenas herda de `GrafoMatrizAdjacencia`.

Frase pronta:

> Eu mantive os nomes do PDF como compatibilidade, mas usei classes em portugues
> para deixar a leitura do codigo mais intuitiva.

## Estrutura Java

```text
codigos/src/main/java/br/puc/grafos/
  app/
    DemonstracaoApi.java
  core/graph/
    AbstractGraph.java
    GrafoListaAdjacencia.java
    GrafoMatrizAdjacencia.java
    AdjacencyListGraph.java
    AdjacencyMatrixGraph.java
    ArestaGrafo.java
    exceptions/
  github/
    InteracaoGitHub.java
    TipoInteracao.java
  services/
    ConstrutorGrafoColaboracao.java
    GrafoConstruido.java
```

## `AbstractGraph`

Arquivo: `codigos/src/main/java/br/puc/grafos/core/graph/AbstractGraph.java`

Essa classe e a base da API. Ela define tudo que uma implementacao de grafo
precisa ter.

Ela guarda:

- `vertexCount`: quantidade de vertices;
- `edgeCount`: quantidade de arestas;
- `vertexWeights`: pesos dos vertices;
- `vertexLabels`: nomes legiveis dos vertices.

O que ela centraliza:

- validacao de vertices;
- validacao de pesos;
- verificacao de aresta existente;
- metodos do enunciado;
- conectividade;
- exportacao para Gephi.

Frase pronta:

> A classe abstrata permite que lista e matriz tenham a mesma API. Assim, o
> resto do sistema nao precisa saber qual estrutura interna esta sendo usada.

## Metodos importantes da API

`getVertexCount()`

Retorna quantos vertices existem no grafo.

`getEdgeCount()`

Retorna quantas arestas existem no grafo.

`addEdge(u, v)`

Cria uma aresta direcionada de `u` para `v` com peso padrao `1.0`.

`addEdge(u, v, weight)`

Cria ou reforca uma aresta com peso. Se a aresta ja existe, o peso e somado.
Isso evita arestas duplicadas e respeita o grafo simples.

`removeEdge(u, v)`

Remove a aresta `u -> v`. Se ela nao existe, lanca excecao.

`hasEdge(u, v)`

Verifica se a aresta `u -> v` existe.

`getVertexInDegree(v)`

Conta quantas arestas chegam no vertice `v`.

`getVertexOutDegree(v)`

Conta quantas arestas saem do vertice `v`.

`isConnected()`

Verifica conectividade forte. Em grafo direcionado, isso significa que todos os
vertices conseguem alcancar todos os outros respeitando a direcao das arestas.

`isCompleteGraph()`

Em um grafo direcionado simples completo, todo par de vertices distintos possui
aresta. Para `n` vertices, o maximo e `n * (n - 1)`.

`exportToGEPHI(path)`

Exporta CSV com origem, destino, peso e labels. Esse arquivo pode ser importado
no Gephi.

## `GrafoListaAdjacencia`

Arquivo: `codigos/src/main/java/br/puc/grafos/core/graph/GrafoListaAdjacencia.java`

Usa lista de mapas:

```java
List<Map<Integer, Double>> adjacency;
```

Exemplo:

```text
adjacency.get(0).get(2) = 5.0
```

Significa que existe a aresta `0 -> 2` com peso `5.0`.

Vantagem:

- boa para grafos esparsos;
- guarda apenas arestas existentes;
- combina bem com redes de colaboracao, porque nem todo mundo interage com todo
  mundo.

Frase pronta:

> Na lista de adjacencia, cada vertice guarda seus sucessores. Isso economiza
> memoria quando o grafo tem poucas arestas em relacao ao numero de vertices.

## `GrafoMatrizAdjacencia`

Arquivo: `codigos/src/main/java/br/puc/grafos/core/graph/GrafoMatrizAdjacencia.java`

Usa matriz de pesos:

```java
double[][] matrix;
```

Exemplo:

```text
matrix[0][2] = 5.0
```

Significa que existe a aresta `0 -> 2` com peso `5.0`.

Vantagem:

- consulta `hasEdge(u, v)` e direta;
- e boa quando o grafo e denso ou quando vamos consultar muitas arestas.

Frase pronta:

> Na matriz, valor zero significa ausencia de aresta; valor positivo significa
> que a aresta existe e aquele valor e o peso.

## Lista x matriz

| Ponto | Lista | Matriz |
|---|---|---|
| Memoria | Melhor para grafo esparso | Usa `V * V` posicoes |
| Consultar aresta | Rapido com `Map` | Direto por indice |
| Exportar | Percorre arestas reais | Percorre matriz toda |
| Grau de saida | Tamanho do mapa | Varre linha |
| Grau de entrada | Varre mapas | Varre coluna |

## Excecoes

Pasta: `codigos/src/main/java/br/puc/grafos/core/graph/exceptions/`

- `VerticeInvalidoException`: vertice fora do intervalo.
- `ArestaInvalidaException`: aresta nao existe.
- `LacoInvalidoException`: tentativa de criar `u -> u`.
- `PesoInvalidoException`: peso invalido.
- `GrafoException`: excecao base da API.

Frase pronta:

> As excecoes deixam claro qual regra do grafo foi violada.

## `TipoInteracao`

Arquivo: `codigos/src/main/java/br/puc/grafos/github/TipoInteracao.java`

Define os pesos:

- `COMENTARIO_ISSUE`: peso 3;
- `COMENTARIO_PR`: peso 2;
- `FECHAMENTO_ISSUE`: peso 3;
- `REVISAO_PR`: peso 4;
- `MERGE_PR`: peso 5.

Frase pronta:

> Interacoes tecnicas mais fortes, como review e merge, recebem peso maior que
> comentarios.

## `InteracaoGitHub`

Arquivo: `codigos/src/main/java/br/puc/grafos/github/InteracaoGitHub.java`

Representa uma interacao antes dela virar aresta.

Campos:

- `loginOrigem`: quem fez a acao;
- `loginDestino`: dono do artefato afetado;
- `tipo`: tipo da interacao;
- `url`: evidencia/link no GitHub.

## `ConstrutorGrafoColaboracao`

Arquivo: `codigos/src/main/java/br/puc/grafos/services/ConstrutorGrafoColaboracao.java`

Transforma interacoes em grafos.

`construirGrafoIntegrado(interacoes)`

Cria um grafo com todas as interacoes juntas.

`construirGrafosSeparados(interacoes)`

Cria quatro grafos:

- `comments`;
- `issue_close`;
- `reviews_merges`;
- `integrated`.

Frase pronta:

> O construtor separa a modelagem da estrutura de dados. Ele recebe interacoes
> do GitHub e decide quais vertices e arestas devem entrar no grafo.

## `DemonstracaoApi`

Arquivo: `codigos/src/main/java/br/puc/grafos/app/DemonstracaoApi.java`

E uma aplicacao separada que usa a API de grafos.

Ela cria:

- um `GrafoListaAdjacencia`;
- um `GrafoMatrizAdjacencia`;

E executa as mesmas operacoes nos dois.

Frase pronta:

> A demonstracao mostra que as duas implementacoes obedecem ao mesmo contrato
> definido pela classe abstrata.

## Perguntas provaveis

**Por que Java?**

Porque o enunciado permite Java ou Python, e Java deixa a orientacao a objetos
mais clara para explicar heranca, classe abstrata e implementacoes concretas.

**Por que o repo analisado pode ser Python se nosso codigo e Java?**

Porque analisamos interacoes do GitHub, nao o codigo-fonte do projeto. Issues,
PRs, comentarios e reviews independem da linguagem do repositorio.

**Por que nao usa biblioteca de grafos?**

Porque o PDF proibe bibliotecas prontas de grafos. A estrutura foi implementada
manualmente.

**O que acontece se adicionar a mesma aresta duas vezes?**

Ela nao duplica. O peso e acumulado.

**O que acontece se tentar criar laco?**

Lanca `LacoInvalidoException`, porque grafo simples nao permite `u -> u`.

## Ordem para estudar

0. `MAPA_ARQUIVOS.md`
1. `TipoInteracao.java`
2. `InteracaoGitHub.java`
3. `AbstractGraph.java`
4. `GrafoListaAdjacencia.java`
5. `GrafoMatrizAdjacencia.java`
6. `ConstrutorGrafoColaboracao.java`
7. `DemonstracaoApi.java`
8. Testes em `codigos/src/test/java`
