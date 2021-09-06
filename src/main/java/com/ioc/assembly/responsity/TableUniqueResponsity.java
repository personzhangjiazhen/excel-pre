package com.ioc.assembly.responsity;

import com.ioc.assembly.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class TableUniqueResponsity {

    @PersistenceContext
    EntityManager entityManager;

    public List<?> selectTableUnique(String tableName, String fieldName,String filedValue){
        List<?> objectList = null;
        String sql = "";
        try{
            sql = "select * from " + tableName + " where " + fieldName + " = " + filedValue;
            objectList = entityManager.createNativeQuery(sql).getResultList();
        }catch (PersistenceException exception){
            exception.printStackTrace();
            throw new RuntimeException("请检查表名、字段名、值是否正确，sql语句：" + sql);
        }

        return objectList;
    }


    public List<?> selectTableData(String sql, String params){
        List<?> objectList = null;
        sql = sql.replaceAll("[\\s\n ]+", " ");
        try{
            String []array = params.split(";");
            sql = StrUtil.sqlSetValue(sql,array);
            objectList = entityManager.createNativeQuery(sql).getResultList();
        }catch (PersistenceException exception){
            exception.printStackTrace();
            throw new RuntimeException("请检查表名、字段名、值是否正确，sql语句：" + sql);
        }

        return objectList;
    }
}
