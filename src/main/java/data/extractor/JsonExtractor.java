package data.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component("json")
public class JsonExtractor implements DataExtractor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String extract(String content, String path) throws Exception {
        JsonNode current = mapper.readTree(content);
        for (String part : path.split("/")) {
            if (part.isEmpty()) continue;
            if (part.startsWith("[") && part.endsWith("]")) {
                current = current.get(Integer.parseInt(part.substring(1, part.length() - 1)));
            } else {
                current = current.get(part);
            }
            if (current == null || current.isMissingNode()) throw new IllegalArgumentException("Путь не найден: " + part);
        }
        return current.asText();
    }
}