package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DocumentConverterFactory {

    private ImageConverterServiceImpl imageConverterServiceImpl;
    @Autowired
    public DocumentConverterFactory(ImageConverterServiceImpl imageConverterServiceImpl) {
        this.imageConverterServiceImpl = imageConverterServiceImpl;
    }


    public DocumentConverterService getConverter(FileType fileType) {
        DocumentConverterService documentConverterService = null;
        switch (fileType) {
            case TEXT:
                break;
            case EXCEL:
                break;
            case IMAGE:
                documentConverterService = imageConverterServiceImpl;
                break;
            default: break;
        }

        return documentConverterService;
    }
}
