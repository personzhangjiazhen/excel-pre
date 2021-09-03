package com.ioc.assembly.service.impl;

import com.ioc.assembly.consts.ExcelConsts;
import com.ioc.assembly.service.ExcelAdapterService;
import com.ioc.assembly.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service(value = ExcelConsts.EXCEL_VALIDATE_COL_UNIQUE)
public class ExcelColUniqueAdapterServiceImpl implements ExcelAdapterService {

    @Override
    public void valid(Sheet sheet, Map<String,Object> paramMap) {
        log.info("某一列值唯一校验开始............");
        Integer col = (Integer) paramMap.get("col");
        Boolean isNotNull = false;
        if(null != paramMap.get("inn") ){
            isNotNull = (Boolean) paramMap.get("inn");
        }
        List<String> list = new ArrayList<String>();
        for (int i = sheet.getFirstRowNum()+1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Cell cell = row.getCell(col-1);
            String cellValue = ExcelUtils.getCellValue(cell);

            if(isNotNull){
                if(StringUtils.isEmpty(cellValue)){
                    throw new RuntimeException( "值为空，校验规则不匹配！" );
                }
            }

            if(StringUtils.isNotEmpty(cellValue)){
                    list.add(cellValue);
            }
        }
        Integer row = 0;
        try {
            String temp = "";
            for (int i = 0; i < list.size() - 1; i++)
            {
                temp = list.get(i);
                for (int j = i + 1; j < list.size(); j++)
                {
                    if (temp.equals(list.get(j)))
                    {
                        row = j + 1;
                        log.error("第" + (i + 1) + "个跟第" + (j + 1) + "个重复，值是：" + temp);
                        throw new RuntimeException( temp+"已存在,校验规则不匹配！" );
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();

            throw new RuntimeException("第"+row+"行，第"+col+"列数据错误，请检查数据是否正确！");
        }

        log.info("某一列值唯一校验结束..........");
    }
}
