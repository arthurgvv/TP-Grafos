package br.puc.grafos.extrator;

import java.util.List;
import java.util.Map;

/**
 * Fachada equivalente ao fetch_all do projeto-base.
 */
public final class BuscadorDadosGitHub {

    private BuscadorDadosGitHub() {
    }

    public static Map<String, List<Object>> buscarTudo(ServicoGitHub service) {
        return service.buscarResumoRepositorio();
    }
}
