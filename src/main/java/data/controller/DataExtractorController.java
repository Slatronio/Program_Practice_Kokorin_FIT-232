package data.controller;

import data.dto.ExtractRequest;
import data.dto.ExtractResponse;
import data.service.DataExtractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataExtractorController {
    private final DataExtractionService service;

    public DataExtractorController(DataExtractionService service) {
        this.service = service;
    }

    @PostMapping("/extract")
    public ResponseEntity<ExtractResponse> extract(@RequestBody ExtractRequest request) {
        try {
            String value = service.process(request.getType(), request.getData(), request.getPath());
            return ResponseEntity.ok(new ExtractResponse(value, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ExtractResponse(null, e.getMessage()));
        }
    }
}