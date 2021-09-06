package com.ioc.assembly.service;


import com.ioc.assembly.bean.RuleDO;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * 校验表头信息接口
 */
public interface ExcelTitleValidateService {

    /**
     * 校验传入的表头信息是否和预订的一致
     * @param path
     */
    public List<RuleDO> validateExcelTitle(Sheet sheet, String path);



    /**
     * 校验传入的表头信息是否和预订的一致
     * @param sheet
     * @param json
     * @return
     */
    public List<RuleDO> validateExcelTitleJson(Sheet sheet, String json);
}
