package com.ioc.assembly.service.impl;



import com.ioc.assembly.bean.RuleDO;
import com.ioc.assembly.service.ExcelPreService;
import com.ioc.assembly.service.ExcelTitleValidateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * Excel预处理实现类
 */
@Slf4j
@Service
public class ExcelPreServiceImpl implements ExcelPreService {

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    @Autowired
    private ExcelStrategyService excelStrategyService;

    @Autowired
    private ExcelTitleValidateService excelTitleValidateService;

    @Override
    public void excelPre(MultipartFile file,String path) {

        String fileName = file.getOriginalFilename();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            log.error("上传文件格式不正确");
        }

        Workbook workbook = null;
        try{
            InputStream is = file.getInputStream();
            if (fileName.endsWith(EXCEL2007)) {
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
                workbook = new HSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheetAt(0);
            List<RuleDO> rulesList = excelTitleValidateService.validateExcelTitle(sheet,path);
            log.info("=====校验规则集合：{}======",rulesList);
            if(!CollectionUtils.isEmpty(rulesList)){
                rulesList.stream().forEach(ruleDO -> {
                    // 支持多规则校验
                    String[] ruleArray = ruleDO.getRule().split(",");
                    for(String rl :ruleArray){
                        excelStrategyService.validByType(sheet,ruleDO.getMap(),rl);
                    }

                });
             }

        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }catch (Exception e){
            log.error("未找到要处理的类，请确认传入的校验类型是否正确！");
        }
    }

    public static void main(String[] args) {
        String str = "validateStr";
        String[] strs = str.split(",");
        for(String s:strs){
            System.out.println(s.toString());
        }

    }
}


