package data.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonDataExtractor implements DataExtractor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String extract(String resourcePath, String fieldPath) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("Файл не найден в resources: " + resourcePath);

            JsonNode current = mapper.readTree(is);
            String[] parts = fieldPath.split("/");

            for (String part : parts) {
                if (part.isEmpty()) continue;

                if (part.startsWith("[") && part.endsWith("]")) {
                    int index = Integer.parseInt(part.substring(1, part.length() - 1));
                    current = current.get(index);
                } else {
                    current = current.get(part);
                }

                if (current == null || current.isMissingNode()) {
                    throw new IllegalArgumentException("Путь не найден в JSON: " + part);
                }
            }
            return current.asText();
        }
    }
}
