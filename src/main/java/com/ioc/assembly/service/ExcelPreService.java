package com.ioc.assembly.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Excel预处理接口
 */
public interface ExcelPreService {

    /**
     * excel预处理
     * @param file excel文件流
     * @param path 配置规则的路径 例如：templates/NFCRule.json
     */
    public void excelPre(MultipartFile file,String path);

    /**
     * excel预处理
     * @param file excel文件流
     * @param json json字符串
     */
    public void excelPreJson(MultipartFile file,String json);

}
