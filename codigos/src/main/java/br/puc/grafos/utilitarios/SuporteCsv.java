package br.puc.grafos.utilitarios;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Le e escreve CSV pequeno sem dependencia externa.
 */
public final class SuporteCsv {

    private SuporteCsv() {
    }

    public static List<Map<String, String>> lerCsv(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return List.of();
            }

            List<String> headers = splitLine(lines.getFirst());
            List<Map<String, String>> rows = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).isBlank()) {
                    continue;
                }
                List<String> values = splitLine(lines.get(i));
                Map<String, String> row = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String value = j < values.size() ? values.get(j) : "";
                    row.put(headers.get(j), value);
                }
                rows.add(row);
            }
            return rows;
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel ler CSV: " + path, exception);
        }
    }

    public static String escapar(String value) {
        String safe = value == null ? "" : value;
        boolean needsQuotes = safe.contains(",") || safe.contains("\"") || safe.contains("\n") || safe.contains("\r");
        if (!needsQuotes) {
            return safe;
        }
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    private static List<String> splitLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }

        values.add(current.toString());
        return values;
    }
}
