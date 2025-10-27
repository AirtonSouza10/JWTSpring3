package com.service.desk.enumerator;

public enum ReportEnum {
    FILIAIS("Filial.jasper");

    private final String fileName;
    ReportEnum(String fileName){ this.fileName = fileName; }
    public String getFileName(){ return fileName; }

}
