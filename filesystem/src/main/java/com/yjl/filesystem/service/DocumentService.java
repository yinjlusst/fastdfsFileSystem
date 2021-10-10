package com.yjl.filesystem.service;

import com.yjl.filesystem.bean.DmsProcessDocument;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {


    public String saveDocument(DmsProcessDocument dmsProcessDocument) {
        return "success";
    }
}
