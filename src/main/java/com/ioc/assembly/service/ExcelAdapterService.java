package com.ioc.assembly.service;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

public interface ExcelAdapterService {


    public void valid(Sheet sheet, Map<String,Object> paramMap);
}
