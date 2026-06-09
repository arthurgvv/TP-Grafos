package br.puc.grafos.utilitarios;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser JSON minimo para manter o projeto sem dependencias externas.
 */
public final class JsonSimples {

    private JsonSimples() {
    }

    public static Object lerArquivo(Path path) {
        try {
            return analisar(Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel ler JSON: " + path, exception);
        }
    }

    public static void escreverArquivo(Path path, Object value) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, serializar(value), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel escrever JSON: " + path, exception);
        }
    }

    public static Object analisar(String conteudo) {
        AnalisadorJson analisador = new AnalisadorJson(conteudo);
        Object value = analisador.analisarValor();
        analisador.pularEspacos();
        if (!analisador.estaNoFim()) {
            throw analisador.erro("Conteudo extra apos o fim do JSON.");
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> comoObjeto(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("Valor JSON nao e objeto.");
    }

    @SuppressWarnings("unchecked")
    public static List<Object> comoLista(Object value) {
        if (value instanceof List<?> list) {
            return (List<Object>) list;
        }
        throw new IllegalArgumentException("Valor JSON nao e lista.");
    }

    public static String valorTexto(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    public static String serializar(Object value) {
        StringBuilder builder = new StringBuilder();
        escreverValor(builder, value);
        return builder.toString();
    }

    private static void escreverValor(StringBuilder builder, Object value) {
        if (value == null) {
            builder.append("null");
        } else if (value instanceof String text) {
            escreverTexto(builder, text);
        } else if (value instanceof Number || value instanceof Boolean) {
            builder.append(value);
        } else if (value instanceof Map<?, ?> map) {
            escreverObjeto(builder, map);
        } else if (value instanceof Iterable<?> iterable) {
            escreverLista(builder, iterable);
        } else if (value.getClass().isArray()) {
            List<Object> values = new ArrayList<>();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                values.add(Array.get(value, i));
            }
            escreverLista(builder, values);
        } else {
            escreverTexto(builder, String.valueOf(value));
        }
    }

    private static void escreverObjeto(StringBuilder builder, Map<?, ?> map) {
        builder.append('{');
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                builder.append(',');
            }
            first = false;
            escreverTexto(builder, String.valueOf(entry.getKey()));
            builder.append(':');
            escreverValor(builder, entry.getValue());
        }
        builder.append('}');
    }

    private static void escreverLista(StringBuilder builder, Iterable<?> values) {
        builder.append('[');
        boolean first = true;
        for (Object value : values) {
            if (!first) {
                builder.append(',');
            }
            first = false;
            escreverValor(builder, value);
        }
        builder.append(']');
    }

    private static void escreverTexto(StringBuilder builder, String value) {
        builder.append('"');
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> {
                    if (character < 32) {
                        builder.append(String.format("\\u%04x", (int) character));
                    } else {
                        builder.append(character);
                    }
                }
            }
        }
        builder.append('"');
    }

    private static final class AnalisadorJson {
        private final String conteudo;
        private int indice;

        private AnalisadorJson(String conteudo) {
            this.conteudo = conteudo;
        }

        private boolean estaNoFim() {
            return indice >= conteudo.length();
        }

        private void pularEspacos() {
            while (!estaNoFim() && Character.isWhitespace(conteudo.charAt(indice))) {
                indice++;
            }
        }

        private Object analisarValor() {
            pularEspacos();
            if (estaNoFim()) {
                throw erro("Fim inesperado do JSON.");
            }

            char character = conteudo.charAt(indice);
            return switch (character) {
                case '{' -> analisarObjeto();
                case '[' -> analisarLista();
                case '"' -> analisarTexto();
                case 't' -> analisarLiteral("true", Boolean.TRUE);
                case 'f' -> analisarLiteral("false", Boolean.FALSE);
                case 'n' -> analisarLiteral("null", null);
                default -> analisarNumero();
            };
        }

        private Map<String, Object> analisarObjeto() {
            exigir('{');
            Map<String, Object> object = new LinkedHashMap<>();
            pularEspacos();
            if (proximoEh('}')) {
                indice++;
                return object;
            }

            while (true) {
                pularEspacos();
                String key = analisarTexto();
                pularEspacos();
                exigir(':');
                Object value = analisarValor();
                object.put(key, value);
                pularEspacos();

                if (proximoEh(',')) {
                    indice++;
                    continue;
                }
                exigir('}');
                return object;
            }
        }

        private List<Object> analisarLista() {
            exigir('[');
            List<Object> values = new ArrayList<>();
            pularEspacos();
            if (proximoEh(']')) {
                indice++;
                return values;
            }

            while (true) {
                values.add(analisarValor());
                pularEspacos();
                if (proximoEh(',')) {
                    indice++;
                    continue;
                }
                exigir(']');
                return values;
            }
        }

        private String analisarTexto() {
            exigir('"');
            StringBuilder builder = new StringBuilder();
            while (!estaNoFim()) {
                char character = conteudo.charAt(indice++);
                if (character == '"') {
                    return builder.toString();
                }
                if (character != '\\') {
                    builder.append(character);
                    continue;
                }

                if (estaNoFim()) {
                    throw erro("Escape JSON incompleto.");
                }
                char escapado = conteudo.charAt(indice++);
                switch (escapado) {
                    case '"' -> builder.append('"');
                    case '\\' -> builder.append('\\');
                    case '/' -> builder.append('/');
                    case 'b' -> builder.append('\b');
                    case 'f' -> builder.append('\f');
                    case 'n' -> builder.append('\n');
                    case 'r' -> builder.append('\r');
                    case 't' -> builder.append('\t');
                    case 'u' -> builder.append(analisarUnicode());
                    default -> throw erro("Escape JSON invalido: \\" + escapado);
                }
            }
            throw erro("String JSON sem fechamento.");
        }

        private char analisarUnicode() {
            if (indice + 4 > conteudo.length()) {
                throw erro("Escape unicode incompleto.");
            }
            String hex = conteudo.substring(indice, indice + 4);
            indice += 4;
            return (char) Integer.parseInt(hex, 16);
        }

        private Object analisarLiteral(String esperado, Object value) {
            if (!conteudo.startsWith(esperado, indice)) {
                throw erro("Literal JSON invalido.");
            }
            indice += esperado.length();
            return value;
        }

        private Number analisarNumero() {
            int start = indice;
            if (proximoEh('-')) {
                indice++;
            }
            consumirDigitos();
            boolean decimal = false;
            if (proximoEh('.')) {
                decimal = true;
                indice++;
                consumirDigitos();
            }
            if (proximoEh('e') || proximoEh('E')) {
                decimal = true;
                indice++;
                if (proximoEh('+') || proximoEh('-')) {
                    indice++;
                }
                consumirDigitos();
            }

            if (start == indice) {
                throw erro("Numero JSON esperado.");
            }

            String number = conteudo.substring(start, indice);
            return decimal ? Double.parseDouble(number) : Long.parseLong(number);
        }

        private void consumirDigitos() {
            int start = indice;
            while (!estaNoFim() && Character.isDigit(conteudo.charAt(indice))) {
                indice++;
            }
            if (start == indice) {
                throw erro("Digito esperado.");
            }
        }

        private boolean proximoEh(char esperado) {
            return !estaNoFim() && conteudo.charAt(indice) == esperado;
        }

        private void exigir(char esperado) {
            if (!proximoEh(esperado)) {
                throw erro("Esperado '" + esperado + "'.");
            }
            indice++;
        }

        private IllegalArgumentException erro(String message) {
            return new IllegalArgumentException(message + " Posicao " + indice + ".");
        }
    }
}
