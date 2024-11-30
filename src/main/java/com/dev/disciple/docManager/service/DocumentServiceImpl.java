package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.BorderType;
import com.dev.disciple.docManager.common.CommonUtils;
import com.dev.disciple.docManager.common.FileType;
import com.dev.disciple.docManager.dto.DocumentMetaData;
import com.dev.disciple.docManager.processors.DefaultDocumentProcessor;
import org.apache.commons.lang3.StringUtils;
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

    public DocumentServiceImpl(DefaultDocumentProcessor defaultDocumentProcessor, DocumentConverterFactory documentConverterFactory, FileService fileService) {
        this.defaultDocumentProcessor = defaultDocumentProcessor;
        this.documentConverterFactory = documentConverterFactory;
        this.fileService = fileService;
    }

    @Override
    public DocumentMetaData extractMetaData(MultipartFile file,String borderType,String pageSize) {

        String fileExtension = StringUtils.EMPTY;
        DocumentMetaData metaData = new DocumentMetaData();
        try {
            metaData = defaultDocumentProcessor.processDocument(file);
            fileExtension = metaData.getFileType();
            logger.debug("extractMetaData: fileExtension: {}", fileExtension);
        } catch (Exception exception) {
            logger.error("error while converting.");
        }
        if (StringUtils.isBlank(fileExtension)) {
            logger.debug("extractMetaData(): original file name: {}",file.getOriginalFilename());
            fileExtension = CommonUtils.getFileExtension(file.getOriginalFilename());
            metaData.setFileType(fileExtension);
        }
        FileType fileType = CommonUtils.getFileType(fileExtension);

        DocumentConverterService documentConverterService = documentConverterFactory.getConverter(fileType,borderType,pageSize);
        try {
//            File outputFile = documentConverterService.convertDocumentToPdf(file, BorderType.THIN);
//            fileService.saveFileAtAGivenLocation(outputFile, outputFile.getName(), "resources/testResults");

            File outputFileNew = documentConverterService.convertDocumentToPdf(file, BorderType.MEDIUM,pageSize);
            fileService.saveFileAtAGivenLocation(outputFileNew, outputFileNew.getName(), "resources/testResults");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return metaData;
    }
}
