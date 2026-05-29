package data.extractor;

public interface DataExtractor {
    String extract(String resourcePath, String fieldPath) throws Exception;
}