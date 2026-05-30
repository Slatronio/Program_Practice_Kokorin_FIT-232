package data.dto;

public class ExtractRequest {
    private String type;
    private String data;
    private String path;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}