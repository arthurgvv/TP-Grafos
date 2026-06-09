package br.puc.grafos.utilitarios;

import java.time.LocalDateTime;

/**
 * Logger simples para os comandos de terminal do projeto.
 */
public final class RegistroLog {

    private RegistroLog() {
    }

    public static void info(String message) {
        for (String line : message.split("\\R", -1)) {
            System.out.println("[" + LocalDateTime.now() + "] " + line);
        }
    }
}
