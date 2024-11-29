package com.dev.disciple.docManager.common;

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

}
