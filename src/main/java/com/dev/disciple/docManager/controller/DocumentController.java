package com.dev.disciple.docManager.controller;


import com.dev.disciple.docManager.common.AppConstants;
import com.dev.disciple.docManager.common.Document;
import com.dev.disciple.docManager.service.DocumentService;
import com.dev.disciple.docManager.serviceimpl.PdfSplitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;
    private final PdfSplitterService pdfSplitterService;

    public DocumentController(DocumentService documentService, PdfSplitterService pdfSplitterService) {
        this.documentService = documentService;
        this.pdfSplitterService = pdfSplitterService;
    }

    @PostMapping("/metadata")
    public ResponseEntity<Document> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "border", required = false) String borderType,
                                               @RequestParam(value = "pageSize", required = false) String pageSize) {

        logger.info(">> Input filename:{} ,border: {},pageSize: {} ",file.getOriginalFilename(),borderType,pageSize);
        Document document = new Document();
        document.put("data", "test");
        documentService.extractMetaData(file,borderType,pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(document);
    }

    @PostMapping("/split")
    public ResponseEntity<Document> splitPdf(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam(value = "ranges", required = false) List<String> ranges) throws IOException {

        logger.debug(">> SplitPdf fileName: {},ranges: {}",multipartFile.getOriginalFilename(),ranges);
        pdfSplitterService.splitPdfByRange(multipartFile, AppConstants.SPLITTED_OUTPUT_DIR,ranges);
        Document document = new Document();
        document.put("data", "test");
        return ResponseEntity.status(HttpStatus.OK).body(document);
    }
}
