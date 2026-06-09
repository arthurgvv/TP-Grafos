# TP Grafos - Analise de Colaboracao no GitHub

Versao intermediaria do trabalho pratico de Teoria de Grafos e Computabilidade.

O projeto modela colaboracoes em um repositorio do GitHub como um grafo
direcionado e ponderado. Cada usuario vira um vertice e cada interacao vira uma
aresta com peso.

Status desta branch: **aproximadamente 65% pronta**. A arquitetura principal ja
foi reorganizada, mas a mineracao real via GitHub e as metricas finais ainda
ficaram para a proxima etapa.

## Repositorio escolhido

Repositorio base: `encode/httpx`.

Motivos:

- projeto real e conhecido;
- possui issues, pull requests, comentarios e revisoes;
- tem volume suficiente para gerar grafos interessantes;
- o dominio e simples de explicar.

## Estrutura

```text
codigos/
  src/main/java/br/puc/grafos/
    aplicacao/           # demonstracao e comandos iniciais
    core/graph/          # API propria de grafos
    github/              # modelos das interacoes
    leitura/             # JSON local -> interacoes
    servicos/            # construcao dos grafos de colaboracao
    arquivos/            # exportacao/importacao CSV Gephi
    extrator/            # esqueleto da mineracao GitHub
    utilitarios/         # JSON, CSV e log simples
  src/test/java/br/puc/grafos/
modelagem/               # modelagem textual e diagramas
relatorio/               # espaco para relatorio
```

## O que ja funciona

- API propria de grafos em lista e matriz de adjacencia.
- Modelos de interacao do GitHub com pesos.
- Construcao de grafo integrado e grafos por tipo.
- Leitura de JSON local com formato parecido com o retorno do GitHub.
- Exportacao de vertices e arestas em CSV para Gephi.
- Recarregamento dos CSVs exportados.
- Testes JUnit para API, construcao, leitura e exportacao.

## Ainda em desenvolvimento

- Consulta GraphQL real no GitHub.
- Salvamento automatico dos dados reais em `data/`.
- Calculo completo de metricas de rede.
- Resultados finais, interpretacao e PDF do relatorio.

## Como rodar

```bash
mvn test
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos demo
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos status
```

Para construir um grafo a partir de um JSON local:

```bash
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build -i data/httpx.json -o tables -t integrated -r matrix
```

Tipos aceitos: `integrated`, `comments`, `reviews`, `closed`.

Representacoes aceitas: `matrix`, `list`.

Observacao: o projeto nao usa bibliotecas prontas de grafos.
