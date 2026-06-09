# Status do projeto

Status atual: arquitetura do projeto-base migrada para Java, mantendo a API
propria de grafos e nomes principais em portugues para facilitar estudo.

## Pronto

- Estrutura Maven criada.
- Codigo Java em `codigos/src/main/java`.
- Testes Java em `codigos/src/test/java`.
- API propria de grafos implementada em Java.
- Lista e matriz de adjacencia implementadas em Java.
- Testes automatizados com JUnit passando.
- Repositorio escolhido: `encode/httpx`.
- Exportacao CSV para Gephi implementada.
- Comando Java `fetch` criado para mineracao GitHub.
- Comando Java `build` criado para montar grafos por tipo.
- Comando Java `analyze` criado para calcular metricas.
- `FabricaDadosInteracoes`, `InterpretadorGrafo` e `FabricaGrafo` migrados para Java.
- Estrategia manual de metricas implementada em Java.
- Exportacao Gephi em duas tabelas: vertices e arestas.

## Em andamento

- Rodar amostra real do `encode/httpx`.
- Gerar arquivos reais de resultado em `tables/` e `statistics/`.
- Escrever interpretacao dos resultados no relatorio.

## Proximos passos

1. Configurar `.env` com token do GitHub.
2. Rodar `AplicacaoGrafos fetch`.
3. Rodar `AplicacaoGrafos build` para os quatro tipos de grafo.
4. Rodar `AplicacaoGrafos analyze`.
5. Gerar o PDF do relatorio em LaTeX.

## Resultado dos testes

Ultima validacao:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
