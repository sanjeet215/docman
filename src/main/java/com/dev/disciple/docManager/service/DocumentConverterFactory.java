package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DocumentConverterFactory {

    private final ImageConverterServiceImpl imageConverterServiceImpl;

    private final DocManConverterServiceImpl docManConverterServiceImpl;
    @Autowired
    public DocumentConverterFactory(ImageConverterServiceImpl imageConverterServiceImpl,DocManConverterServiceImpl docManConverterServiceImpl) {
        this.imageConverterServiceImpl = imageConverterServiceImpl;
        this.docManConverterServiceImpl = docManConverterServiceImpl;
    }


    public DocumentConverterService getConverter(FileType fileType) {
        DocumentConverterService documentConverterService = null;
        switch (fileType) {
            case TEXT:
                break;
            case EXCEL:
                break;
            case IMAGE:
                //documentConverterService = imageConverterServiceImpl;
                documentConverterService = docManConverterServiceImpl;
                break;
            default: break;
        }

        return documentConverterService;
    }
}
