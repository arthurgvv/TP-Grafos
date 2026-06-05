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
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 3. Rodar demo da API de grafos

```bash
mvn test
java -cp target/classes br.puc.grafos.app.DemonstracaoApi
```

Essa demo mostra a mesma sequencia de operacoes usando lista e matriz de
adjacencia.

## 4. Proxima etapa

A mineracao real do GitHub ainda sera migrada para Java. A API de grafos,
construtor de grafos e testes ja estao prontos.

## Observacao sobre arquivos `.class`

Se aparecer uma pasta `target/` ou arquivos `.class`, nao estude por eles. Eles
sao gerados pelo Maven quando o Java compila. O codigo comentado fica em
`codigos/src/main/java`.
