package com.joker17.sql.small.tools.support;


public class ClassPathHelper {

    private final static String targetClassPath = ClassPathHelper.class.getClassLoader().getResource("").getPath();

    public static String getFilePath(String filePath) {
        return targetClassPath + filePath;
    }
}
