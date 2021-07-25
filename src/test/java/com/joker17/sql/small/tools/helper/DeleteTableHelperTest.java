package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import com.joker17.sql.small.tools.support.TableDatabaseHelper;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;

public class DeleteTableHelperTest {

    private JdbcTemplate jdbcTemplate;

    private final String[] tables = TableDatabaseHelper.getTables();

    @Before
    public void before() throws IOException {
        String dataSourceProperties = ClassPathHelper.getFilePath("db.properties");
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);
        TableDatabaseHelper.initTable(jdbcTemplate);
    }

    @Test
    public void deleteByTables() {
        DeleteTableHelper.delete(jdbcTemplate, tables, "WHERE 1=1", 10);
    }

    @Test
    public void deleteByTablesQuerySql() {
        String tablesQuerySql = "SELECT table_name FROM information_schema.tables WHERE table_schema = (SELECT database());";
        DeleteTableHelper.delete(jdbcTemplate, tablesQuerySql, "WHERE 1=1", 10);
    }
}