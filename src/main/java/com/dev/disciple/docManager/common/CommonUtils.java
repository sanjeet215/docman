package com.dev.disciple.docManager.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class CommonUtils {


    public static FileType getFileType(String fileType) {
        return FileType.from(fileType);
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            // No extension found or the file name ends with a dot
            return "";
        }

        return fileName.substring(dotIndex + 1);
    }


    public static PDRectangle getPDRectangle(String pageSize) {
        PDRectangle page = PDRectangle.A4;
        if(StringUtils.isNotBlank(pageSize)) {
            if(pageSize.equalsIgnoreCase("A4")) {
                page =  new PDRectangle(PDRectangle.A4.getHeight(),PDRectangle.A4.getWidth());
            } else if(pageSize.equalsIgnoreCase("A5")) {
                page =  new PDRectangle(PDRectangle.A5.getHeight(),PDRectangle.A5.getWidth());
            } else if(pageSize.equalsIgnoreCase("LETTER")) {
                page =  new PDRectangle(PDRectangle.LETTER.getHeight(),PDRectangle.LETTER.getWidth());
            } else if (pageSize.equalsIgnoreCase("LEGAL")) {
                page =  new PDRectangle(PDRectangle.LEGAL.getHeight(),PDRectangle.LEGAL.getWidth());
            }
        }
        return page;
    }
}
