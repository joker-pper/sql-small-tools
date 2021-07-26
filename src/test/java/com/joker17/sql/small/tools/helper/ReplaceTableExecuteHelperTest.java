package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import com.joker17.sql.small.tools.support.TableDatabaseHelper;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;

public class ReplaceTableExecuteHelperTest {

    private JdbcTemplate jdbcTemplate;

    private final String[] tables = TableDatabaseHelper.getTables();

    @Before
    public void before() throws IOException {
        String dataSourceProperties = ClassPathHelper.getFilePath("db.properties");
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);
    }

    @Test
    public void executeByTables() {
        String sql = "DROP TABLE IF EXISTS `#{table}`;\nCREATE TABLE `#{table}` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(128) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tables, sql, 1);

        sql = "UPDATE `#{table}` SET name = '123' WHERE 1=1;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tables, sql, 1);

        sql = "DELETE FROM `#{table}` WHERE 1=1;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tables, sql, 1);
    }

    @Test
    public void executeByTablesQuerySql() {
        String tablesQuerySql = "SELECT 'xx01' UNION SELECT 'xx02' UNION SELECT 'xx03'";
        String sql = "DROP TABLE IF EXISTS `#{table}`;\nCREATE TABLE `#{table}` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(128) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tablesQuerySql, sql, 1);

        sql = "UPDATE `#{table}` SET name = '123' WHERE 1=1;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tablesQuerySql, sql, 1);

        sql = "DELETE FROM `#{table}` WHERE 1=1;";
        ReplaceTableExecuteHelper.execute(jdbcTemplate, tablesQuerySql, sql, 1);

    }
}