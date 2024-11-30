package com.dev.disciple.docManager.serviceimpl;

import com.dev.disciple.docManager.common.BorderType;
import com.dev.disciple.docManager.service.DocumentConverterService;
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


    public File convertDocumentToPdf(MultipartFile inputFile, BorderType borderType, String pageSize) {

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

                // Margins around the image
                float margin = 20; // Adjust this value as needed for the desired margin

                // Calculate scaling factors to fit the image within the page considering margins
                float scaleX = (pageWidth - 2 * margin) / imageWidth;
                float scaleY = (pageHeight - 2 * margin) / imageHeight;
                float scale = Math.min(scaleX, scaleY);

                // Calculate position considering margins
                float xOffset = margin + (pageWidth - 2 * margin - (imageWidth * scale)) / 2;
                float yOffset = margin + (pageHeight - 2 * margin - (imageHeight * scale)) / 2;

                // Border thickness based on BorderType
                float borderThickness = 0;
                if (borderType != null) {
                    switch (borderType) {
                        case THIN:
                            borderThickness = 1; // Thin border thickness
                            break;
                        case MEDIUM:
                            borderThickness = 3; // Medium border thickness
                            break;
                        case THICK:
                            borderThickness = 5; // Thick border thickness
                            break;
                        case NO_BORDER:
                            borderThickness = 0; // No border
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported border type: " + borderType);
                    }
                }

                // Check if a border is needed
                if (borderThickness > 0) {
                    // Set border color (e.g., black)
                    contentStream.setLineWidth(borderThickness);
                    contentStream.setStrokingColor(255, 255, 255); // RGB for black

                    // Draw the border rectangle around the image, with padding to avoid overlap
                    float borderPadding = borderThickness / 2; // Half of the border thickness as padding
                    contentStream.addRect(
                            xOffset - borderPadding,
                            yOffset - borderPadding,
                            imageWidth * scale + 2 * borderPadding,
                            imageHeight * scale + 2 * borderPadding
                    );
                    contentStream.stroke();
                }

                // Draw the image inside the border or as is
                contentStream.drawImage(pdImage, xOffset, yOffset, imageWidth * scale, imageHeight * scale);
            }

            // Save the document to the output file
            document.save(outputFile);
        } catch (Exception exception) {
            logger.error("Error while converting the file.", exception);
        }

        return outputFile;
    }

//    @Override
//    public File convertDocumentToPdf(MultipartFile inputFile, BorderType borderType,String pageSize) {
//
//        // Validate the input file
//        if (inputFile == null || inputFile.isEmpty()) {
//            throw new IllegalArgumentException("Input file cannot be null or empty");
//        }
//
//        // Temporary output PDF file
//        File outputFile = null;
//
//        try (PDDocument document = new PDDocument()) {
//            outputFile = File.createTempFile("converted_", ".pdf");
//            // Create a new page for the PDF
//            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
//            document.addPage(page);
//
//            // Load the image as a BufferedImage
//            BufferedImage bufferedImage = ImageIO.read(inputFile.getInputStream());
//            if (bufferedImage == null) {
//                throw new IOException("Invalid image format or unable to read the image");
//            }
//
//            // Convert BufferedImage to PDImageXObject
//            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
//
//            // Start drawing the image on the page
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                // Page dimensions
//                float pageWidth = page.getMediaBox().getWidth();
//                float pageHeight = page.getMediaBox().getHeight();
//
//                // Image dimensions
//                float imageWidth = bufferedImage.getWidth();
//                float imageHeight = bufferedImage.getHeight();
//
//                // Calculate scaling factors to fit the image within the page
//                float scaleX = pageWidth / imageWidth;
//                float scaleY = pageHeight / imageHeight;
//                float scale = Math.min(scaleX, scaleY);
//
//                // Calculate centered position
//                float xOffset = (pageWidth - (imageWidth * scale)) / 2;
//                float yOffset = (pageHeight - (imageHeight * scale)) / 2;
//
//                // Border thickness based on BorderType
//                float borderThickness = 0;
//                if (borderType != null) {
//                    switch (borderType) {
//                        case THIN:
//                            borderThickness = 1; // Thin border thickness
//                            break;
//                        case MEDIUM:
//                            borderThickness = 3; // Medium border thickness
//                            break;
//                        case THICK:
//                            borderThickness = 5; // Thick border thickness
//                            break;
//                        case NO_BORDER:
//                            borderThickness = 0; // No border
//                            break;
//                        default:
//                            throw new IllegalArgumentException("Unsupported border type: " + borderType);
//                    }
//                }
//
//                // Check if a border is needed
//                if (borderThickness > 0) {
//                    // Set border color (e.g., black)
//                    contentStream.setLineWidth(borderThickness);
//                    contentStream.setStrokingColor(255, 255, 255); // RGB for black
//
//                    // Draw the border rectangle around the image, with a padding to avoid overlap
//                    float borderPadding = borderThickness / 2; // Half of the border thickness as padding
//                    contentStream.moveTo(xOffset - borderPadding, yOffset - borderPadding);
//                    contentStream.lineTo(xOffset + imageWidth * scale + borderPadding, yOffset - borderPadding);
//                    contentStream.lineTo(xOffset + imageWidth * scale + borderPadding, yOffset + imageHeight * scale + borderPadding);
//                    contentStream.lineTo(xOffset - borderPadding, yOffset + imageHeight * scale + borderPadding);
//                    contentStream.closePath();
//                    contentStream.stroke();
//                }
//
//                // Draw the image inside the border or as is
//                contentStream.drawImage(pdImage, xOffset, yOffset, imageWidth * scale, imageHeight * scale);
//            }
//
//            // Save the document to the output file
//            document.save(outputFile);
//        } catch (Exception exception) {
//            logger.error("Error while converting the file.", exception);
//        }
//
//        return outputFile;
//    }
}
