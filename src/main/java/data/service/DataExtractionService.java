package data.service;

import data.extractor.DataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataExtractionService {
    private final Map<String, DataExtractor> extractors;

    @Autowired
    public DataExtractionService(Map<String, DataExtractor> extractors) {
        this.extractors = extractors;
    }

    public String process(String type, String data, String path) {
        DataExtractor extractor = extractors.get(type.toLowerCase());
        if (extractor == null) throw new IllegalArgumentException("Неподдерживаемый тип: " + type);
        try {
            return extractor.extract(data, path);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка извлечения: " + e.getMessage(), e);
        }
    }
}