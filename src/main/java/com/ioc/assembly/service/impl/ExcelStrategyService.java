package com.ioc.assembly.service.impl;


import com.ioc.assembly.service.ExcelAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Excel策略模式类
 */
@Slf4j
@Service
public class ExcelStrategyService {

    private final Map<String, ExcelAdapterService> excelAdapterServiceMap = new ConcurrentHashMap<>();

    public ExcelStrategyService(Map<String,ExcelAdapterService> excelAdapterServices){
        log.info("-------------构造函数加载中--------------");
        this.excelAdapterServiceMap.clear();
        excelAdapterServices.forEach((k, v)-> this.excelAdapterServiceMap.put(k, v));
        log.info("-------------构造函数加载成功--------------");
    }

    public void validByType(Sheet sheet, Map<String,Object> paramMap,String type){
        excelAdapterServiceMap.get(type).valid(sheet,paramMap);
    }



}
