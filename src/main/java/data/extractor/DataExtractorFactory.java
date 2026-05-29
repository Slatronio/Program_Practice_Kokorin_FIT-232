package data.extractor;

public class DataExtractorFactory {
    public static DataExtractor create(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Путь к файлу не может быть null");
        }
        String lower = filePath.toLowerCase();
        if (lower.endsWith(".json")) {
            return new JsonDataExtractor();
        } else if (lower.endsWith(".xml")) {
            return new XmlDataExtractor();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + filePath);
        }
    }
}
