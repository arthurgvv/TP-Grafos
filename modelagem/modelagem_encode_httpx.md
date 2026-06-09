# Modelagem inicial - encode/httpx

## Repositorio escolhido

O repositorio escolhido para a analise foi `encode/httpx`, disponivel em:

https://github.com/encode/httpx

HTTPX e um cliente HTTP moderno para Python. Ele e adequado para o trabalho
porque possui mais de 5.000 estrelas, atividade de desenvolvimento, issues,
pull requests, revisoes e comentarios.

## Ideia central

Cada colaborador sera representado como um vertice. As relacoes entre
colaboradores serao representadas como arestas direcionadas.

Exemplo:

```text
Arthur revisa um pull request de Victor

Arthur -> Victor
```

Nesse caso, Arthur e a origem da aresta porque executou a acao, e Victor e o
destino porque e o autor do pull request.

## Tipos de grafos

O trabalho pede grafos separados por tipo de relacao e um grafo integrado.

1. Grafo de comentarios:
   - comentarios em issues;
   - comentarios em pull requests.

2. Grafo de fechamento de issues:
   - usuario que fechou a issue aponta para o autor da issue.

3. Grafo de revisoes e merges:
   - reviewer aponta para o autor do pull request;
   - usuario que fez merge aponta para o autor do pull request.

4. Grafo integrado:
   - junta todas as interacoes anteriores;
   - acumula os pesos quando o mesmo par de usuarios interage mais de uma vez.

## Pesos

| Interacao | Aresta | Peso |
|---|---|---:|
| Comentario em issue ou PR | autor do comentario -> autor do artefato | 2 |
| Abertura de issue comentada por outro usuario | autor da issue -> autor do comentario | 3 |
| Fechamento de issue por outro usuario | quem fechou -> autor da issue | 3 |
| Revisao/aprovacao de PR | reviewer -> autor do PR | 4 |
| Merge de PR | quem fez merge -> autor do PR | 5 |

## Decisao importante para explicar ao professor

O grafo e direcionado porque a interacao tem sentido. Revisar o PR de alguem
nao e a mesma coisa que receber uma revisao. Se os dois usuarios interagirem nos
dois sentidos, o grafo tera arestas antiparalelas.

O grafo e simples porque nao criamos varias arestas iguais entre o mesmo par de
usuarios. Quando a relacao se repete, somamos o peso da aresta. Assim, a
intensidade da colaboracao aparece no peso.
