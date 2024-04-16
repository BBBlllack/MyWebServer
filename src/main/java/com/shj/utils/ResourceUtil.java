package com.shj.utils;


public class ResourceUtil {
    public static String getResourceStaticPath(){
        return ResourceUtil.class.getClassLoader().getResource("static").getPath();
    }
    public static String getResourcePath(){
        return ResourceUtil.class.getClassLoader().getResource("").getPath();
    }
}
