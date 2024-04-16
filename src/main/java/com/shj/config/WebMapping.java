package com.shj.config;


import java.util.HashMap;
import java.util.Map;

public class WebMapping {
    public static final Map<String, String> Content_Type_Mapping = new HashMap<>();
    public static final Map<String, Integer> HTTP_Status_Mapping = new HashMap<>();
    // 初始化映射表
    static {
        Content_Type_Mapping.put("str","str1");
    }
}
