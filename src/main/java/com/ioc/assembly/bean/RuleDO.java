package com.ioc.assembly.bean;

import java.io.Serializable;
import java.util.Map;

public class RuleDO implements Serializable {

    private static final long serialVersionUID = -2345050250856843488L;

    private String rule;

    private Map<String,Object> map;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "RuleDO{" +
                "rule='" + rule + '\'' +
                ", map=" + map +
                '}';
    }
}
