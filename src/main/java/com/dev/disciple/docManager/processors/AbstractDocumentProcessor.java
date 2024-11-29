package com.dev.disciple.docManager.processors;

import com.dev.disciple.docManager.dto.DocumentMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

abstract class AbstractDocumentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDocumentProcessor.class);

    public final DocumentMetaData processDocument(MultipartFile file) {
        return getDocumentMetaData(file);
    }

    protected DocumentMetaData getDocumentMetaData(MultipartFile file) {
        DocumentMetaData documentMetaData = new DocumentMetaData();
        try (InputStream input = file.getInputStream()) {
            // Use Tika for parsing
            Tika tika = new Tika();

            // Detect the file type
            String fileType = tika.detect(input);
            logger.debug("getDocumentMetaData: fileType: {}", fileType);

            // Extract metadata
            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            parser.parse(input, handler, metadata, new ParseContext());

            documentMetaData.setFileType(fileType);

            // Display metadata
            for (String name : metadata.names()) {
                logger.debug("getDocumentMetaData: metadata: {}", name);
            }

            logger.debug("getDocumentMetaData: metaData final: {}, created Date: {}", metadata,"don't have for now");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return documentMetaData;
    }
}
