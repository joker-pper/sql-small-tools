package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import com.joker17.sql.small.tools.utils.SqlAnalysisUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;


public class ExecuteSqlHelperTest {

    private JdbcTemplate jdbcTemplate;

    @Before
    public void before() throws IOException {
        String dataSourceProperties = ClassPathHelper.getFilePath("db.properties");
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);
    }


    @Test
    public void test1() throws IOException {
        String sqlText = "DROP TABLE IF EXISTS `xx01`;\n" +
                "CREATE TABLE `xx01` (\n" +
                "          `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "          `name` varchar(128) DEFAULT NULL,\n" +
                "          PRIMARY KEY (`id`)\n" +
                "        ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";

        String[] sqlResults = SqlAnalysisUtils.getSqlResults(sqlText);
        ExecuteSqlHelper.execute(jdbcTemplate, sqlResults, 1);
    }

    @Test
    public void test2() throws IOException {

        String sqlText = "---sql\n" +
                "DROP TABLE IF EXISTS `xx01`;\n" +
                "CREATE TABLE `xx01` (\n" +
                "          `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "          `name` varchar(128) DEFAULT NULL,\n" +
                "          PRIMARY KEY (`id`)\n" +
                "        ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                "---\n" +
                "\n" +
                "---sql\n" +
                "DROP TABLE IF EXISTS `xx02`;\n" +
                "CREATE TABLE `xx02` (\n" +
                "          `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "          `name` varchar(128) DEFAULT NULL,\n" +
                "          PRIMARY KEY (`id`)\n" +
                "        ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                "---\n" +
                "\n" +
                "---sql\n" +
                "DROP TABLE IF EXISTS `xx03`;\n" +
                "CREATE TABLE `xx03` (\n" +
                "          `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "          `name` varchar(128) DEFAULT NULL,\n" +
                "          PRIMARY KEY (`id`)\n" +
                "        ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                "---";

        String[] sqlResults = SqlAnalysisUtils.getSqlResults(sqlText);

        ExecuteSqlHelper.execute(jdbcTemplate, sqlResults, 6);
    }

}