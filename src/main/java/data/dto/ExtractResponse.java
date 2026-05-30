package data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractResponse {
    private String value;
    private String error;

    public ExtractResponse(String value, String error) {
        this.value = value;
        this.error = error;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}