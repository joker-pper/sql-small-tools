package com.joker17.sql.small.tools.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;

public class JdbcUtils {

    private JdbcUtils() {

    }

    /**
     * 获取datasource
     *
     * @param properties
     * @return
     */
    public static DataSource getDataSource(Properties properties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(properties.getProperty("datasource.username"));
        hikariDataSource.setPassword(properties.getProperty("datasource.password"));
        hikariDataSource.setJdbcUrl(properties.getProperty("datasource.url"));
        hikariDataSource.setDriverClassName(properties.getProperty("datasource.driver-class-name"));
        return hikariDataSource;
    }

    /**
     * 获取jdbcTemplate
     *
     * @param dataSource
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 获取jdbcTemplate
     *
     * @param dataSourcePropertiesFile
     * @return
     */
    public static JdbcTemplate getJdbcTemplate(File dataSourcePropertiesFile) throws IOException {
        Properties properties = PropertiesUtils.loadProperties(new FileInputStream(dataSourcePropertiesFile));
        DataSource dataSource = getDataSource(properties);
        return getJdbcTemplate(dataSource);
    }

}
