package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.CommonUtils;
import com.dev.disciple.docManager.common.FileType;
import com.dev.disciple.docManager.dto.DocumentMetaData;
import com.dev.disciple.docManager.processors.DefaultDocumentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class DocumentServiceImpl implements DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);
    private final DefaultDocumentProcessor defaultDocumentProcessor;

    private final DocumentConverterFactory documentConverterFactory;

    private final FileService fileService;

    public DocumentServiceImpl(DefaultDocumentProcessor defaultDocumentProcessor,DocumentConverterFactory documentConverterFactory,FileService fileService) {
        this.defaultDocumentProcessor = defaultDocumentProcessor;
        this.documentConverterFactory = documentConverterFactory;
        this.fileService = fileService;
    }
    @Override
    public DocumentMetaData extractMetaData(MultipartFile file) {
        DocumentMetaData metaData = defaultDocumentProcessor.processDocument(file);
        logger.debug("extractMetaData: file type: {}",metaData.getFileType());
        FileType fileType = CommonUtils.getFileType(metaData.getFileType());
        DocumentConverterService documentConverterService = documentConverterFactory.getConverter(fileType);
        try {
            File outputFile = documentConverterService.convertDocumentToPdf(file);
            fileService.saveFileAtAGivenLocation(outputFile,outputFile.getName(),"resources/testResults");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return metaData;
    }
}
