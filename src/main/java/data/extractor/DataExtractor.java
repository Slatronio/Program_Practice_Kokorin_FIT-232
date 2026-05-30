package data.extractor;

public interface DataExtractor {
    String extract(String content, String path) throws Exception;
}