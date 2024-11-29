package com.dev.disciple.docManager.common;

public enum FileType {
    WORD("doc", "docx"),
    EXCEL("xls", "xlsx"),
    IMAGE("image/png","image/jpeg","jpg", "jpeg", "png", "gif", "bmp"),
    PDF("pdf"),
    TEXT("txt"),
    UNKNOWN;

    private final String[] extensions;

    FileType(String... extensions) {
        this.extensions = extensions;
    }

    /**
     * Get the enum value based on the extension.
     *
     * @param extension the file extension (e.g., "jpg")
     * @return the corresponding FileType
     */
    public static FileType from(String extension) {
        if (extension == null || extension.isEmpty()) {
            return UNKNOWN;
        }
        String normalizedExtension = extension.toLowerCase();
        for (FileType fileType : values()) {
            for (String ext : fileType.extensions) {
                if (ext.equals(normalizedExtension)) {
                    return fileType;
                }
            }
        }
        return UNKNOWN;
    }
}

