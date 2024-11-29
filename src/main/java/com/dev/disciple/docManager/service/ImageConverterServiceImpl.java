package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.BorderType;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageConverterServiceImpl implements DocumentConverterService {

    private static final Logger logger = LoggerFactory.getLogger(ImageConverterServiceImpl.class);

    @Override
    public File convertDocumentToPdf(MultipartFile inputFile, BorderType borderType) throws IOException {
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

            // Read the image as BufferedImage
            BufferedImage bufferedImage = ImageIO.read(inputFile.getInputStream());
            if (bufferedImage == null) {
                throw new IOException("Invalid image format or unable to read the image");
            }

            // Convert BufferedImage to PDImageXObject
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

            // Start drawing the image on the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();
                float imageWidth = bufferedImage.getWidth();
                float imageHeight = bufferedImage.getHeight();

                // Calculate scaling factors
                float scaleX = pageWidth / imageWidth;
                float scaleY = pageHeight / imageHeight;
                float scale = Math.min(scaleX, scaleY);

                // Calculate centered position
                float xOffset = (pageWidth - (imageWidth * scale)) / 2;
                float yOffset = (pageHeight - (imageHeight * scale)) / 2;

                // Draw the border based on the BorderType
                logger.debug("convertDocumentToPdf(): {}", borderType);
                switch (borderType) {
                    case THIN_BORDER -> {
                        drawBorder(contentStream, xOffset, yOffset, imageWidth * scale, imageHeight * scale, 25);
                    }
                    case THICK_BORDER -> {
                        drawBorder(contentStream, xOffset, yOffset, imageWidth * scale, imageHeight * scale, 5);
                    }
                    case NO_BORDER -> {
                        // No action needed for NO_BORDER
                    }
                }

                // Draw the image
                contentStream.drawImage(pdImage, xOffset, yOffset, imageWidth * scale, imageHeight * scale);
            }

            // Save the document to the output file
            document.save(outputFile);
        }

        return outputFile;
    }

    private void drawBorder(PDPageContentStream contentStream, float xOffset, float yOffset, float width, float height, float borderWidth) throws IOException {
        contentStream.setStrokingColor(Color.WHITE); // Border color
        contentStream.setLineWidth(borderWidth); // Border thickness
        contentStream.addRect(
                xOffset - borderWidth / 2,
                yOffset - borderWidth / 2,
                width + borderWidth,
                height + borderWidth
        );
        contentStream.stroke();
    }
}
