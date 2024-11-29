package com.dev.disciple.docManager.service;

import com.dev.disciple.docManager.dto.DocumentMetaData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface DocumentService {

    DocumentMetaData extractMetaData(MultipartFile file,String border,String pageSize);
}
