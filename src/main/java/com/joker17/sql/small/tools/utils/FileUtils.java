package com.joker17.sql.small.tools.utils;

import java.io.File;

public class FileUtils {

    /**
     * mkdirs
     *
     * @param parentFile
     */
    public static void mkdirs(File parentFile) {
        if (parentFile == null) {
            return;
        }
        if (!parentFile.exists() || !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }
    }

    /**
     * 获取文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isFileAndExists(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        return true;
    }


}
