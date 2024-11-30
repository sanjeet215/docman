package com.dev.disciple.docManager.serviceimpl;

import com.dev.disciple.docManager.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public void saveFileAtAGivenLocation(File inputFile, String outputFileName, String outputDirectory) {

        logger.debug("saveFileAtAGivenLocation: inputFile: {},outputFileName: {},outputDirectory: {}",inputFile,outputFileName,outputDirectory);

        try {
            File outputDir = new File(outputDirectory);

            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    throw new IOException("Failed to create output directory: " + outputDirectory);
                }
            }

            File outputFile = new File(outputDir, outputFileName);

            if (!inputFile.renameTo(outputFile)) {
                throw new IOException("Failed to save converted file to: " + outputFile.getAbsolutePath());
            }

        } catch (Exception exception) {
            logger.error("Error while saving the file...exception: {}", exception.getMessage());
            exception.printStackTrace();
        }

    }
}
