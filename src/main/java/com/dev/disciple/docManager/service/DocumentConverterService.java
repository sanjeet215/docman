package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.BorderType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public interface DocumentConverterService {
    File convertDocumentToPdf(MultipartFile inputFile, BorderType borderType) throws IOException;
}
