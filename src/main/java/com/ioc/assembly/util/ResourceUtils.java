package com.ioc.assembly.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

public class ResourceUtils {

    /**
     * 通过某个key获取JSONArray对象
     * @param key
     * @param path
     * @return
     * @throws IOException
     */
    public static JSONArray getRuleJSONArray(String key,String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        JSONObject json = JSON.parseObject(IOUtils.toString(
                resource.getInputStream(), Charsets.UTF_8.toString()));
        JSONArray jsonArray = json.getJSONArray(key);
        return jsonArray;
    }
}
