// Este pacote contem servicos, ou seja, classes que executam uma regra do sistema.
package br.puc.grafos.services;

// Importa o tipo abstrato para o builder poder trabalhar com qualquer implementacao.
import br.puc.grafos.core.graph.AbstractGraph;
// Importa a implementacao baseada em lista de adjacencia.
import br.puc.grafos.core.graph.GrafoListaAdjacencia;
// Importa a implementacao baseada em matriz de adjacencia.
import br.puc.grafos.core.graph.GrafoMatrizAdjacencia;
// Importa a interacao bruta que veio do GitHub.
import br.puc.grafos.github.InteracaoGitHub;
// Importa os tipos de interacao e seus pesos.
import br.puc.grafos.github.TipoInteracao;

// ArrayList e usado para montar listas modificaveis durante filtros e preparacao.
import java.util.ArrayList;
// Comparator define a ordenacao alfabetica dos usuarios.
import java.util.Comparator;
// HashMap guarda login -> indice com acesso rapido.
import java.util.HashMap;
// LinkedHashMap preserva a ordem dos grafos separados no retorno.
import java.util.LinkedHashMap;
// List representa colecoes ordenadas de usuarios e interacoes.
import java.util.List;
// Map representa associacoes chave -> valor.
import java.util.Map;
// TreeSet guarda usuarios sem repeticao e ja ordenados.
import java.util.TreeSet;

/**
 * Transforma interacoes do GitHub nos grafos exigidos pelo enunciado.
 *
 * <p>Este e um dos arquivos mais importantes para explicar ao professor:
 * ele pega uma lista de interacoes, cria um indice numerico para cada usuario,
 * escolhe matriz ou lista de adjacencia e adiciona as arestas com seus pesos.</p>
 */
public class ConstrutorGrafoColaboracao {

    // Define se o construtor cria GrafoMatrizAdjacencia ou GrafoListaAdjacencia.
    private final boolean usarMatriz;

    // Construtor padrao: sem parametro, usamos lista de adjacencia.
    public ConstrutorGrafoColaboracao() {
        // Reaproveita o outro construtor passando false.
        this(false);
    }

    // Construtor configuravel: true cria matriz, false cria lista.
    public ConstrutorGrafoColaboracao(boolean usarMatriz) {
        // Guarda a escolha para ser usada quando criarGrafo for chamado.
        this.usarMatriz = usarMatriz;
    }

    /**
     * Constroi o grafo integrado, somando todos os tipos de interacao.
     *
     * <p>Grafo integrado significa: todas as relacoes entram no mesmo grafo.
     * Se Arthur ja tem aresta para Victor e outra interacao Arthur -> Victor aparece,
     * o peso e somado na mesma aresta.</p>
     */
    public GrafoConstruido construirGrafoIntegrado(List<InteracaoGitHub> interacoes) {
        // Primeiro descobrimos todos os usuarios e qual indice cada um vai receber.
        VerticesPreparados preparados = prepararVertices(interacoes);
        // Depois criamos um grafo unico usando todas as interacoes recebidas.
        return construirGrafoComUsuarios(preparados.usuarios(), preparados.indiceUsuarios(), interacoes);
    }

    /**
     * Constroi os tres grafos separados e o grafo integrado.
     *
     * <p>Esta separacao vem do enunciado: comentarios, fechamento de issue,
     * reviews/merges, e o integrado com tudo junto.</p>
     */
    public Map<String, GrafoConstruido> construirGrafosSeparados(List<InteracaoGitHub> interacoes) {
        // Garante que todos os grafos usem os mesmos usuarios e os mesmos indices.
        VerticesPreparados preparados = prepararVertices(interacoes);

        // Separa comentarios em issue e PR para formar o grafo de comentarios.
        List<InteracaoGitHub> comentarios = filtrarPorTipos(
                // Lista completa recebida.
                interacoes,
                // Comentario em issue entra neste grupo.
                TipoInteracao.COMENTARIO_ISSUE,
                // Comentario em pull request tambem entra neste grupo.
                TipoInteracao.COMENTARIO_PR
        );
        // Separa apenas interacoes de fechamento de issue.
        List<InteracaoGitHub> fechamentosIssue = filtrarPorTipos(interacoes, TipoInteracao.FECHAMENTO_ISSUE);
        // Separa reviews e merges de pull request.
        List<InteracaoGitHub> revisoesMerges = filtrarPorTipos(
                // Lista completa recebida.
                interacoes,
                // Review de PR entra neste grupo.
                TipoInteracao.REVISAO_PR,
                // Merge de PR tambem entra neste grupo.
                TipoInteracao.MERGE_PR
        );

        // LinkedHashMap mantem a ordem em que colocamos os grafos.
        Map<String, GrafoConstruido> grafos = new LinkedHashMap<>();
        // Grafo so com interacoes de comentario.
        grafos.put("comments", construirGrafoComUsuarios(preparados.usuarios(), preparados.indiceUsuarios(), comentarios));
        // Grafo so com fechamento de issue.
        grafos.put("issue_close", construirGrafoComUsuarios(preparados.usuarios(), preparados.indiceUsuarios(), fechamentosIssue));
        // Grafo com review e merge de PR.
        grafos.put("reviews_merges", construirGrafoComUsuarios(preparados.usuarios(), preparados.indiceUsuarios(), revisoesMerges));
        // Grafo integrado com todos os tipos.
        grafos.put("integrated", construirGrafoComUsuarios(preparados.usuarios(), preparados.indiceUsuarios(), interacoes));
        // Devolve os quatro grafos prontos.
        return grafos;
    }

    // Filtra a lista deixando passar somente os tipos informados no parametro varargs.
    private List<InteracaoGitHub> filtrarPorTipos(List<InteracaoGitHub> interacoes, TipoInteracao... tipos) {
        // Converte o array de tipos permitidos para lista, facilitando o contains.
        List<TipoInteracao> permitidos = List.of(tipos);
        // Cria uma lista vazia para acumular as interacoes aceitas.
        List<InteracaoGitHub> filtradas = new ArrayList<>();

        // Percorre cada interacao recebida.
        for (InteracaoGitHub interacao : interacoes) {
            // Se o tipo da interacao esta na lista de tipos permitidos...
            if (permitidos.contains(interacao.tipo())) {
                // ...guardamos essa interacao no resultado filtrado.
                filtradas.add(interacao);
            }
        }

        // Retorna somente as interacoes cujo tipo foi permitido.
        return filtradas;
    }

    // Prepara a lista de usuarios e o mapa login -> indice.
    private VerticesPreparados prepararVertices(List<InteracaoGitHub> interacoes) {
        // TreeSet remove duplicados e mantem os logins em ordem alfabetica.
        TreeSet<String> usuarios = new TreeSet<>(Comparator.naturalOrder());

        // Percorre todas as interacoes para encontrar todos os usuarios envolvidos.
        for (InteracaoGitHub interacao : interacoes) {
            // Adiciona quem fez a acao.
            usuarios.add(interacao.loginOrigem());
            // Adiciona quem foi alvo/relacionado na acao.
            usuarios.add(interacao.loginDestino());
        }

        // Se nao existe usuario, nao existe grafo util para construir.
        if (usuarios.isEmpty()) {
            // IllegalArgumentException indica erro no argumento passado para o metodo.
            throw new IllegalArgumentException("Nao ha usuarios para construir o grafo.");
        }

        // Converte o conjunto ordenado em lista para podermos acessar por indice.
        List<String> usuariosOrdenados = new ArrayList<>(usuarios);
        // Mapa que traduz login para o numero do vertice.
        Map<String, Integer> indiceUsuarios = new HashMap<>();

        // Percorre a lista ordenada gerando indices 0, 1, 2, ...
        for (int indice = 0; indice < usuariosOrdenados.size(); indice++) {
            // Associa o login atual ao indice atual.
            indiceUsuarios.put(usuariosOrdenados.get(indice), indice);
        }

        // Retorna os dois dados juntos usando um record privado.
        return new VerticesPreparados(usuariosOrdenados, indiceUsuarios);
    }

    // Cria um grafo para uma lista fixa de usuarios e adiciona as interacoes desejadas.
    private GrafoConstruido construirGrafoComUsuarios(
            // Lista de logins ja ordenada.
            List<String> usuarios,
            // Mapa login -> indice ja preparado.
            Map<String, Integer> indiceUsuarios,
            // Interacoes que devem entrar neste grafo especifico.
            List<InteracaoGitHub> interacoes
    ) {
        // Cria a estrutura de grafo escolhida: matriz ou lista.
        AbstractGraph grafo = criarGrafo(usuarios);

        // Percorre cada interacao que deve virar aresta.
        for (InteracaoGitHub interacao : interacoes) {
            // Adiciona a aresta correspondente no grafo.
            adicionarInteracao(grafo, indiceUsuarios, interacao);
        }

        // Retorna grafo e mapa; Map.copyOf evita que alguem altere o mapa depois.
        return new GrafoConstruido(grafo, Map.copyOf(indiceUsuarios));
    }

    // Cria matriz ou lista de adjacencia e coloca o label de cada vertice.
    private AbstractGraph criarGrafo(List<String> usuarios) {
        // Operador ternario: se usarMatriz for true, cria matriz; senao, lista.
        AbstractGraph grafo = usarMatriz
                // Implementacao com matriz de adjacencia.
                ? new GrafoMatrizAdjacencia(usuarios.size())
                // Implementacao com lista de adjacencia.
                : new GrafoListaAdjacencia(usuarios.size());

        // Percorre todos os usuarios para gravar o login como label do vertice.
        for (int indice = 0; indice < usuarios.size(); indice++) {
            // O vertice indice recebe o nome/login daquela posicao.
            grafo.setVertexLabel(indice, usuarios.get(indice));
        }

        // Devolve o grafo vazio, mas ja com os vertices nomeados.
        return grafo;
    }

    // Converte uma interacao em uma aresta direcionada e ponderada.
    private void adicionarInteracao(
            // Grafo que recebera a aresta.
            AbstractGraph grafo,
            // Mapa usado para encontrar o indice de cada login.
            Map<String, Integer> indiceUsuarios,
            // Interacao bruta que sera transformada em aresta.
            InteracaoGitHub interacao
    ) {
        // Busca o indice do usuario que realizou a acao.
        int origem = indiceUsuarios.get(interacao.loginOrigem());
        // Busca o indice do usuario alvo/relacionado.
        int destino = indiceUsuarios.get(interacao.loginDestino());

        // Se origem e destino sao iguais, seria um laco.
        if (origem == destino) {
            // Como o trabalho pede grafo simples, ignoramos interacao do usuario consigo mesmo.
            return;
        }

        // Adiciona origem -> destino usando o peso definido pelo tipo da interacao.
        grafo.addEdge(origem, destino, interacao.tipo().getPeso());
    }

    // Record privado para carregar juntos usuarios ordenados e mapa de indices.
    private record VerticesPreparados(
            // Lista de usuarios em ordem fixa.
            List<String> usuarios,
            // Mapa que transforma login em indice.
            Map<String, Integer> indiceUsuarios
    ) {
        // Corpo vazio: record ja fornece construtor e metodos usuarios()/indiceUsuarios().
    }
}
