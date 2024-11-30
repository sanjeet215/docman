package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.common.FileType;
import com.dev.disciple.docManager.serviceimpl.DocManConverterServiceImpl;
import com.dev.disciple.docManager.serviceimpl.ImageConverterServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DocumentConverterFactory {

    private final ImageConverterServiceImpl imageConverterServiceImpl;

    private final DocManConverterServiceImpl docManConverterServiceImpl;

    @Autowired
    public DocumentConverterFactory(ImageConverterServiceImpl imageConverterServiceImpl, DocManConverterServiceImpl docManConverterServiceImpl) {
        this.imageConverterServiceImpl = imageConverterServiceImpl;
        this.docManConverterServiceImpl = docManConverterServiceImpl;
    }


    public DocumentConverterService getConverter(FileType fileType, String borderType, String pageSize) {
        DocumentConverterService documentConverterService = null;
        switch (fileType) {
            case TEXT:
                break;
            case EXCEL:
                break;
            case IMAGE:
                documentConverterService = getImageConverter(borderType, pageSize);
                break;
            default:
                break;
        }

        return documentConverterService;
    }

    private DocumentConverterService getImageConverter(String borderType, String pageSize) {
        DocumentConverterService documentConverterService = null;
        if (StringUtils.isBlank(borderType) && StringUtils.isBlank(pageSize)) {
            documentConverterService = docManConverterServiceImpl;
        } else if (StringUtils.isNotBlank(borderType) && StringUtils.isNotBlank(pageSize)) {
            documentConverterService = imageConverterServiceImpl;
        }
        return documentConverterService;
    }
}
