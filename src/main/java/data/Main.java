package data;

import data.extractor.DataExtractor;
import data.extractor.DataExtractorFactory;

public class Main {
    public static void main(String[] args) {
        // Убедитесь, что файлы лежат в src/main/resources
        String jsonFile = "request-json.json";
        String xmlFile = "request-xml.json";

        try {
            // JSON
            DataExtractor jsonExtractor = DataExtractorFactory.create(jsonFile);
            System.out.println("JSON /name: " + jsonExtractor.extract(jsonFile, "/name"));
            System.out.println("JSON /relation/[1]/name: " + jsonExtractor.extract(jsonFile, "/relation/[1]/name"));

            // XML
            DataExtractor xmlExtractor = DataExtractorFactory.create(xmlFile);
            System.out.println("XML /name: " + xmlExtractor.extract(xmlFile, "/name"));
            System.out.println("XML /relation/[1]/name: " + xmlExtractor.extract(xmlFile, "/relation/[1]/name"));

        } catch (Exception e) {
            System.err.println("Ошибка при извлечении данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}