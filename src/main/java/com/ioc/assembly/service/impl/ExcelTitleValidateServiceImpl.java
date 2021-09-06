package com.ioc.assembly.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ioc.assembly.bean.RuleDO;
import com.ioc.assembly.service.ExcelTitleValidateService;
import com.ioc.assembly.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验配置的标题和传入的标题是否一致实现类
 */
@Slf4j
@Service
public class ExcelTitleValidateServiceImpl implements ExcelTitleValidateService {



    @Override
    public List<RuleDO> validateExcelTitle(Sheet sheet, String path) {

        JSONArray originalTitleArray = new JSONArray();
        JSONArray requestTitleArray = new JSONArray();
        JSONArray jsonArray = new JSONArray();
        List<RuleDO> ruleDOList = new ArrayList<>();
        boolean firstRow = true;
        try {
            // 获取规则json文件
            ClassPathResource resource = new ClassPathResource(path);

            JSONObject json = JSON.parseObject(IOUtils.toString(
                    new FileInputStream(resource.getFile()), Charsets.UTF_8.toString()));

            jsonArray = json.getJSONArray("rule");

            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()){
                JSONObject jsonObject = (JSONObject) iterator.next();
                RuleDO ruleDO = new RuleDO();
                Map<String,Object> ruleInfoMap = new HashMap<>();
                Set<String> keySet = jsonObject.keySet();
                for (String key: keySet){
                    if("title".equals(key)){
                        originalTitleArray.add(jsonObject.getString(key));
                    }else{
                        // 校验key是否符合规则 因某列是可以不写校验规则的 所以不校验title的JSONArray和校验规则的JSONArray一致！
                        if(validateKeyRule(key)){
                            ruleDO.setRule(jsonObject.getString(key));
                            ruleInfoMap.put("col",Integer.parseInt(key.substring(key.length()-1)));
                        }else{
                            ruleInfoMap.put(key,jsonObject.get(key));
                        }
                    }
                }
                ruleDO.setMap(ruleInfoMap);
                ruleDOList.add(ruleDO);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("请确认路径是否正确: path:" + path);
        }
        log.info("ruleDOList:  "+ruleDOList.toString());
        // 获取传入的表头信息
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            //首行  提取注解
            if(firstRow){
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = ExcelUtils.getCellValue(cell);
                    requestTitleArray.add(cellValue);
                }
                firstRow = false;
            }

        }
        log.info("请求的titles：{}",requestTitleArray.toJSONString());
        log.info("原始的titles：{}",originalTitleArray.toJSONString());
        log.info("请求的hashcode：{}",requestTitleArray.hashCode());
        log.info("原始的hashcode：{}",originalTitleArray.hashCode());

       if(requestTitleArray.hashCode() == originalTitleArray.hashCode()){
            return ruleDOList;
        }else{
           throw new RuntimeException("请检查传入的Excel标题是否和模板相同！");
       }

    }

    @Override
    public List<RuleDO> validateExcelTitleJson(Sheet sheet, String json) {
        JSONArray originalTitleArray = new JSONArray();
        JSONArray requestTitleArray = new JSONArray();
        JSONArray jsonArray = new JSONArray();
        List<RuleDO> ruleDOList = new ArrayList<>();
        boolean firstRow = true;
        try {
            // 获取规则json文件
            JSONObject requestJsonObject = JSON.parseObject(json);
            jsonArray = requestJsonObject.getJSONArray("rule");

            Iterator<Object> iterator = jsonArray.iterator();
            while (iterator.hasNext()){
                JSONObject jsonObject = (JSONObject) iterator.next();
                RuleDO ruleDO = new RuleDO();
                Map<String,Object> ruleInfoMap = new HashMap<>();
                Set<String> keySet = jsonObject.keySet();
                for (String key: keySet){
                    if("title".equals(key)){
                        originalTitleArray.add(jsonObject.getString(key));
                    }else{
                        // 校验key是否符合规则 因某列是可以不写校验规则的 所以不校验title的JSONArray和校验规则的JSONArray一致！
                        if(validateKeyRule(key)){
                            ruleDO.setRule(jsonObject.getString(key));
                            ruleInfoMap.put("col",Integer.parseInt(key.substring(key.length()-1)));
                        }else{
                            ruleInfoMap.put(key,jsonObject.get(key));
                        }
                    }
                }
                ruleDO.setMap(ruleInfoMap);
                ruleDOList.add(ruleDO);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("请确认json是否正确: json:" + json);
        }
        log.info("ruleDOList:  {}",ruleDOList.toString());
        // 获取传入的表头信息
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            //首行  提取注解
            if(firstRow){
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = ExcelUtils.getCellValue(cell);
                    requestTitleArray.add(cellValue);
                }
                firstRow = false;
            }

        }
        log.info("请求的titles：{}",requestTitleArray.toJSONString());
        log.info("原始的titles：{}",originalTitleArray.toJSONString());
        log.info("请求的hashcode：{}",requestTitleArray.hashCode());
        log.info("原始的hashcode：{}",originalTitleArray.hashCode());

        if(requestTitleArray.hashCode() == originalTitleArray.hashCode()){
            return ruleDOList;
        }else{
            throw new RuntimeException("请检查传入的Excel标题是否和模板相同！");
        }
    }


    /**
     * 校验key为 C开头，数字结尾；
     * @param rule
     * @return
     */
    private  Boolean validateKeyRule(String rule){
        // 首先校验key的长度是否为2
        if(2 != rule.length()){
            return  false;
        }
        // 校验是以C开头
        if(!rule.startsWith("C")){
            return false;
        }
        // 校验是以数字结尾
        if(!validateNumbWithEnd(rule)){
            return false;
        }
        return true;
    }

    private Boolean validateNumbWithEnd(String rl){
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(rl.charAt(rl.length()-1)+"");
            if (!isNum.matches()) {
                return false;
            }
        }catch (Exception e){
           e.printStackTrace();
           return false;
        }

        return  true;
    }

}
