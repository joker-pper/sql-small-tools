package com.joker17.sql.small.tools.utils;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    /**
     * 获取字符内容
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getAsString(File file) throws IOException {
        return FileCopyUtils.copyToString(new FileReader(file));
    }

}
