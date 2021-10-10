package com.yjl.filesystem.bean;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.beans.Transient;

@Data
public class DmsProcessDocument {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uploadUserId;
    private String uploadDate;
    private String updateTimes;
    private String size;
    private String documentName;
    private String url;
}
