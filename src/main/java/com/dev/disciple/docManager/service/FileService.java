package com.dev.disciple.docManager.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface FileService {
    public void saveFileAtAGivenLocation(File inputFile, String outputFileName, String outputDirectory);
}
