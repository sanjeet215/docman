package com.dev.disciple.docManager.serviceimpl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfSplitterService {

    private static final Logger logger = LoggerFactory.getLogger(PdfSplitterService.class);

    /**
     * Splits a PDF based on the user-defined range of pages and saves the resulting PDFs.
     *
     * @param inputFile       MultipartFile input (PDF file).
     * @param outputDirectory Directory where the split PDF files will be saved.
     * @param pageRanges      List of page ranges (e.g., "1-3", "5-7").
     * @return List of split PDF files.
     * @throws IOException If there is an error reading or writing files.
     */
    public List<File> splitPdfByRange(MultipartFile inputFile, File outputDirectory, List<String> pageRanges) throws IOException {
        // Validate input file and output directory
        if (inputFile == null || inputFile.isEmpty()) {
            throw new IllegalArgumentException("Input file cannot be null or empty");
        }
        if (outputDirectory == null || !outputDirectory.exists()) {
            assert outputDirectory != null;
            if (!outputDirectory.mkdirs()) {
                throw new IOException("Failed to create output directory.");
            }
        }

        List<File> splitFiles = new ArrayList<>();

        // Convert MultipartFile to a temporary file
        File tempInputFile = File.createTempFile("uploaded_", ".pdf");
        inputFile.transferTo(tempInputFile);

        try (PDDocument document = PDDocument.load(tempInputFile)) {
            int totalPages = document.getNumberOfPages();
            logger.info("Total pages in the document: {}", totalPages);

            if(pageRanges == null || CollectionUtils.isEmpty(pageRanges)) {
                pageRanges = new ArrayList<>();
                for (int i = 1; i <= totalPages; i++) {
                    pageRanges.add(i + "-" + i); // Each page as its own range (e.g., "1-1", "2-2")
                }
                logger.info("Page ranges were not provided. Auto-generated ranges: {}", pageRanges);
            }


            for (String range : pageRanges) {
                // Parse the range (e.g., "1-3" or "5-7")
                String[] rangeParts = range.split("-");
                if (rangeParts.length != 2) {
                    logger.error("Invalid range format: {}", range);
                    continue;
                }

                int startPage = Integer.parseInt(rangeParts[0]) - 1; // Convert to zero-based index
                int endPage = Integer.parseInt(rangeParts[1]) - 1;

                if (startPage < 0 || endPage >= totalPages || startPage > endPage) {
                    logger.error("Invalid range values: {}", range);
                    continue;
                }

                // Create a new PDF for the specified range
                try (PDDocument rangeDoc = new PDDocument()) {
                    for (int i = startPage; i <= endPage; i++) {
                        PDPage page = document.getPage(i);
                        rangeDoc.addPage(page);
                    }

                    // Save the range-based split PDF
                    File outputFile = new File(outputDirectory, "split_pages_" + (startPage + 1) + "-" + (endPage + 1) + ".pdf");
                    rangeDoc.save(outputFile);
                    splitFiles.add(outputFile);
                }
            }
        } catch (Exception e) {
            logger.error("Error while splitting the PDF", e);
            throw e;
        } finally {
            // Delete the temporary input file
            if (!tempInputFile.delete()) {
                logger.warn("Failed to delete temporary input file: {}", tempInputFile.getAbsolutePath());
            }
        }

        return splitFiles;
    }
}
