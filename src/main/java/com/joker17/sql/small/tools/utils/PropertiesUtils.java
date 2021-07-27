package com.joker17.sql.small.tools.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private PropertiesUtils() {

    }

    /**
     * 加载properties
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(InputStream is) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } finally {
            is.close();
        }
        return properties;
    }

}
