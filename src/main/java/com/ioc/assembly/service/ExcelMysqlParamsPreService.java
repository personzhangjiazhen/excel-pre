package com.ioc.assembly.service;

public interface ExcelMysqlParamsPreService {

    /**
     * 对参数进行预处理
     * @param cellValue excel单元格值
     * @param params 参数值
     * @return
     */
    public String paramsPre(String cellValue,String params);
}
