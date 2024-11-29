package com.dev.disciple.docManager.controller;


import com.dev.disciple.docManager.common.Document;
import com.dev.disciple.docManager.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private DocumentService documentService;
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/metadata")
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("operation") String name,
                                               @RequestParam("page_size") String description) {

        logger.info(">>uploadFile:{}, file name: {}", file, file.getName());
        Document document = new Document();
        document.put("data", "test");
        documentService.extractMetaData(file);
        return ResponseEntity.status(HttpStatus.OK).body(document);
    }
}
