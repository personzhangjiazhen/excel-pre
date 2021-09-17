package com.ioc.assembly.service.impl;

import com.ioc.assembly.consts.ExcelConsts;
import com.ioc.assembly.responsity.TableUniqueResponsity;
import com.ioc.assembly.service.ExcelAdapterService;
import com.ioc.assembly.service.ExcelMysqlParamsPreService;
import com.ioc.assembly.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service(value = ExcelConsts.EXCEL_VALIDATE_MYSQL_TYPE_UNIQUE)
public class ExcelMysqlUniqueAdapterServiceImpl implements ExcelAdapterService {

    @Autowired
    private TableUniqueResponsity tableUniqueResponsity;
    @Autowired
    private ExcelMysqlParamsPreService excelMysqlParamsPreService;

    @Override
    public void valid(Sheet sheet, Map<String,Object> paramMap) {
        Boolean fVExist = true; // 是否开启校验返回值是否存在
        Boolean isNotNull = false; // 是否开启是否为空校验
        Boolean containsCell = false; // 是否把cell值拼接到sql上
        log.info("数据库值唯一校验开始............");
        Integer col = (Integer) paramMap.get("col");

        // 获取是否判断值为空
        if(null != paramMap.get("inn") ){
            isNotNull = (Boolean) paramMap.get("inn");
        }
        if(null != paramMap.get("fVExist")){
            fVExist = (Boolean) paramMap.get("fVExist");
        }

        for (int i = sheet.getFirstRowNum()+1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Cell cell = row.getCell(col-1);
            String cellValue = ExcelUtils.getCellValue(cell);
            try {
                if(isNotNull){
                    if(StringUtils.isEmpty(cellValue)){
                        throw new RuntimeException( "值为空，校验规则不匹配！" );
                    }
                }

                List<?> resultList  = new ArrayList<>();
                if(StringUtils.isNotEmpty(cellValue)){
                    if(null != paramMap.get("tableName") && null != paramMap.get("filedName")){
                        // 表名
                        String tableName = (String) paramMap.get("tableName");
                        // 字段名
                        String filedName = (String) paramMap.get("filedName");

                        resultList  = tableUniqueResponsity.selectTableUnique(tableName,filedName,cellValue);

                    }else if(null != paramMap.get("sql") && null != paramMap.get("params")){
                        String sql = (String) paramMap.get("sql");
                        // 字段名
                        String params = (String) paramMap.get("params");
                        if(null != paramMap.get("containsCell")){
                            containsCell = (Boolean) paramMap.get("containsCell");
                        }else{ // 如果containsCell为false，才走这个逻辑，因为这个逻辑已经包含了containsCell这个逻辑
                            params = excelMysqlParamsPreService.paramsPre(cellValue,params);
                        }
                        // 把当前列的值作为参数拼接到参数值中
                        if(containsCell){
                            params = cellValue + ";" + params;
                        }
                        resultList  = tableUniqueResponsity.selectTableData(sql,params);
                    }else{
                        throw new RuntimeException( "请检查是否配置正确！" );
                    }


                    // 默认校验值存在
                    if(fVExist){
                        // 不能为空
                        if(null == resultList || resultList.size() != 1){
                            throw new RuntimeException( cellValue+"不是唯一值,校验规则不匹配！" );
                        }
                    }else{ // 校验值不存在
                        // 不做任何处理
                        if(null != resultList && resultList.size() >= 1){
                            throw new RuntimeException( cellValue+"值已存在,校验规则不匹配！" );
                        }
                    }
                }

            }catch (Exception e){
                log.error("Exception message:{}",e.getMessage());
                throw new RuntimeException("第"+i+"行，第"+col+"列数据错误，请检查数据是否正确！");
            }

        }
        log.info("数据库值唯一校验结束............");
    }
}
