# Como executar o projeto Java

## 1. Verificar Java e Maven

```bash
java -version
mvn -version
```

O projeto usa Java 21 e Maven.

## 2. Rodar testes

```bash
mvn test
```

Resultado esperado no estado atual:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 3. Rodar demo da API de grafos

```bash
mvn test
java -cp target/classes br.puc.grafos.aplicacao.DemonstracaoApi
```

Essa demo mostra a mesma sequencia de operacoes usando lista e matriz de
adjacencia.

## 4. Rodar comandos da arquitetura completa

Comandos equivalentes ao projeto-base, agora em Java:

```bash
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos fetch
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos buscar
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build -i data/httpx.json -o tables -t integrated -r matrix
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos construir -i data/httpx.json -o tables -t integrated -r matrix
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analyze -i tables/httpx/integrated -o statistics -s manual
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos analisar -i tables/httpx/integrated -o statistics -s manual
```

Tipos aceitos em `build -t`:

- `integrated`
- `comments`
- `reviews`
- `closed`

Representacoes aceitas em `build -r`:

- `matrix`
- `list`

## 5. Configurar mineracao GitHub

Crie um arquivo `.env` com base em `.env.example`:

```env
GITHUB_TOKEN=seu_token
GITHUB_REPO_OWNER=encode
GITHUB_REPO_NAME=httpx
MAX_ISSUES=20
MAX_PULLS=10
```

## 6. Proxima etapa

A arquitetura de mineracao, construcao e analise ja esta migrada para Java.
Agora falta rodar uma amostra real e transformar os resultados em texto do
relatorio.

## 7. Observacao sobre arquivos `.class`

Se aparecer uma pasta `target/` ou arquivos `.class`, nao estude por eles. Eles
sao gerados pelo Maven quando o Java compila. O codigo comentado fica em
`codigos/src/main/java`.
