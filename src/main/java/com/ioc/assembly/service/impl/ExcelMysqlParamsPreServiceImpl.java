package com.ioc.assembly.service.impl;



import com.ioc.assembly.service.ExcelMysqlParamsPreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "excelMysqlParamsPreServiceImpl")
public class ExcelMysqlParamsPreServiceImpl implements ExcelMysqlParamsPreService {

    @Override
    public String paramsPre(String cellValue, String params) {
        log.info("=============预处理入参:{}===========",params);
        String [] paramsArray = params.split(";");
        for (String value: paramsArray) {
            if("cell".equals(value)){
                params = params.replaceFirst("\\bcell\\b",cellValue);
            }
        }
        log.info("=============预处理出参:{}===========",params);
        return params;
    }


  /*  public static void main(String[] args) {
        String str = "(1,2,3);cell;cell;1;";
        String cellValue = "123";
        //System.out.println(RegexUtil.match("\\[(.*?)]","[1,2,3]"));
        //str = str.replaceFirst("\\[(.*?)]","534");
        String [] paramsArray = str.split(";");
        for (String value: paramsArray) {
            if("cell".equals(value)){
                str = str.replaceFirst("\\bcell\\b",cellValue);
            }
        }
        System.out.print(str);
    }*/
}
