package com.dev.disciple.docManager.service;


import com.dev.disciple.docManager.common.BorderType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class DocManConverterServiceImpl implements DocumentConverterService {

    private static final Logger logger = LoggerFactory.getLogger(DocManConverterServiceImpl.class);

    @Override
    public File convertDocumentToPdf(MultipartFile inputFile, BorderType borderType) {

        // Validate the input file
        if (inputFile == null || inputFile.isEmpty()) {
            throw new IllegalArgumentException("Input file cannot be null or empty");
        }

        // Temporary output PDF file
        File outputFile = null;

        try (PDDocument document = new PDDocument()) {
            outputFile = File.createTempFile("converted_", ".pdf");
            // Create a new page for the PDF
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            // Load the image as a BufferedImage
            BufferedImage bufferedImage = ImageIO.read(inputFile.getInputStream());
            if (bufferedImage == null) {
                throw new IOException("Invalid image format or unable to read the image");
            }

            // Convert BufferedImage to PDImageXObject
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);

            // Start drawing the image on the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Page dimensions
                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                // Image dimensions
                float imageWidth = bufferedImage.getWidth();
                float imageHeight = bufferedImage.getHeight();

                // Calculate scaling factors to fit the image within the page
                float scaleX = pageWidth / imageWidth;
                float scaleY = pageHeight / imageHeight;
                float scale = Math.min(scaleX, scaleY);

                // Calculate centered position
                float xOffset = (pageWidth - (imageWidth * scale)) / 2;
                float yOffset = (pageHeight - (imageHeight * scale)) / 2;

                // Draw the border around the image
                float borderThickness = 2; // Border thickness in points (adjust as needed)
                float imageX = xOffset;
                float imageY = yOffset;
                float scaledWidth = imageWidth * scale;
                float scaledHeight = imageHeight * scale;

                // Draw the border (rectangle)
                contentStream.setLineWidth(borderThickness);
                contentStream.setStrokingColor(255, 255, 255); // White color for the border

                // Move to the start position and draw the rectangle
                contentStream.moveTo(imageX - borderThickness, imageY - borderThickness);
                contentStream.lineTo(imageX + scaledWidth + borderThickness, imageY - borderThickness);
                contentStream.lineTo(imageX + scaledWidth + borderThickness, imageY + scaledHeight + borderThickness);
                contentStream.lineTo(imageX - borderThickness, imageY + scaledHeight + borderThickness);
                contentStream.closePath();
                contentStream.stroke();

                // Draw the image inside the border
                contentStream.drawImage(pdImage, imageX, imageY, scaledWidth, scaledHeight);
            }

            // Save the document to the output file
            document.save(outputFile);
        } catch (Exception exception) {
            logger.error("error while converting the file.");
        }

        return outputFile;
    }
}
