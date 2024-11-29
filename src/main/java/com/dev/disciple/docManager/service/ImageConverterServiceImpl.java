package com.dev.disciple.docManager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;


@Component
public class ImageConverterServiceImpl implements DocumentConverterService {

    private static final Logger logger = LoggerFactory.getLogger(ImageConverterServiceImpl.class);
    @Override
    public File convertDocumentToPdf(MultipartFile inputFile) throws IOException {
        // Validate the input file
        if (inputFile == null || inputFile.isEmpty()) {
            throw new IllegalArgumentException("Input file cannot be null or empty");
        }

        // Temporary output PDF file
        File outputFile = File.createTempFile("converted_", ".pdf");

        try (PDDocument document = new PDDocument()) {
            // Create a new page for the PDF
            PDPage page = new PDPage();
            document.addPage(page);

            // Load the image as a PDImageXObject
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(
                    document,
                    inputFile.getBytes(),
                    inputFile.getOriginalFilename()
            );

            // Start drawing the image on the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Scale the image to fit the page
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();
                float imageWidth = pdImage.getWidth();
                float imageHeight = pdImage.getHeight();

                // Calculate scaling factors
                float scaleX = pageWidth / imageWidth;
                float scaleY = pageHeight / imageHeight;
                float scale = Math.min(scaleX, scaleY);

                // Calculate centered position
                float xOffset = (pageWidth - (imageWidth * scale)) / 2;
                float yOffset = (pageHeight - (imageHeight * scale)) / 2;

                // Draw the image
                contentStream.drawImage(pdImage, xOffset, yOffset, imageWidth * scale, imageHeight * scale);
            }
            document.save(outputFile);
        }

        return outputFile;
    }
}
