# Como executar

Esta branch esta em versao intermediaria. Ela compila, roda testes e monta
grafos a partir de JSON local.

## Testes

```bash
mvn test
```

## Demo da API

```bash
mvn test
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos demo
```

## Status da etapa

```bash
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos status
```

## Construir grafo local

```bash
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos build -i data/httpx.json -o tables -t integrated -r matrix
```

Tambem funciona em portugues:

```bash
java -cp target/classes br.puc.grafos.aplicacao.AplicacaoGrafos construir -i data/httpx.json -o tables -t comments -r list
```

## Ainda nao fechado nesta branch

- `fetch` para buscar dados reais no GitHub.
- `analyze` para calcular metricas finais.
- Geracao do PDF final.
