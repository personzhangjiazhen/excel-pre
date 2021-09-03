package com.ioc.assembly.service.impl;

import com.ioc.assembly.consts.ExcelConsts;
import com.ioc.assembly.responsity.TableUniqueResponsity;
import com.ioc.assembly.service.ExcelAdapterService;
import com.ioc.assembly.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service(value = ExcelConsts.EXCEL_VALIDATE_MYSQL_TYPE_UNIQUE)
public class ExcelMysqlUniqueAdapterServiceImpl implements ExcelAdapterService {

    @Autowired
    private TableUniqueResponsity tableUniqueResponsity;


    @Override
    public void valid(Sheet sheet, Map<String,Object> paramMap) {
        log.info("数据库值唯一校验开始............");
        Integer col = (Integer) paramMap.get("col");

        Boolean isNotNull = false;
        if(null != paramMap.get("inn") ){
            isNotNull = (Boolean) paramMap.get("inn");
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

                if(StringUtils.isNotEmpty(cellValue)){
                    if(null != paramMap.get("tableName") && null != paramMap.get("filedName")){
                        // 表名
                        String tableName = (String) paramMap.get("tableName");
                        // 字段名
                        String filedName = (String) paramMap.get("filedName");
                        List<?> resultList  = tableUniqueResponsity.selectTableUnique(tableName,filedName,cellValue);
                        if(null == resultList || resultList.size() != 1){
                            throw new RuntimeException( cellValue+"不是唯一值,校验规则不匹配！" );
                        }
                    }else{
                        throw new RuntimeException( "请检查表名或字段名是否配置正确！" );
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
