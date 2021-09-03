package com.ioc.assembly.service.impl;

import com.ioc.assembly.consts.ExcelConsts;
import com.ioc.assembly.service.ExcelAdapterService;
import com.ioc.assembly.util.StrUtil;
import com.ioc.assembly.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service(value = ExcelConsts.EXCEL_VALIDATE_STRING_TYPE)
public class ExcelStrAdapterServiceImpl implements ExcelAdapterService {


    @Override
    public void valid(Sheet sheet, Map<String,Object> paramMap) {
        log.info("中文校验开始..........");
        Boolean isNotNull = false;
        Integer col = (Integer) paramMap.get("col");
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
                    if(!StrUtil.checkNameChese(cellValue)){
                        throw new RuntimeException( cellValue + "不是中文，校验规则不匹配！" );
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("第" + i + "行，第" + col + "列数据错误，请检查数据是否正确！");
            }
        }
        log.info("中文校验结束..........");
    }


}
