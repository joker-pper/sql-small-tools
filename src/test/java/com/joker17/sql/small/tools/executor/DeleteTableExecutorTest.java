package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import com.joker17.sql.small.tools.support.TableDatabaseHelper;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class DeleteTableExecutorTest {

    @Before
    public void before() throws IOException {
        String dataSourceProperties = ClassPathHelper.getFilePath("db.properties");
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        TableDatabaseHelper.initTable(JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile));
    }

    @Test
    public void testByTables() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String deleteTable = ClassPathHelper.getFilePath("delete-table/delete-table1.text");

        String maxThreads = "3";

        String line = String.format("-data-source %s -delete-table %s -max-threads %s",
                dataSource, deleteTable, maxThreads);

        String[] args = line.split(" ");
        DeleteTableExecutor.INSTANCE.execute(args);
    }

    @Test
    public void testByTablesQuerySql() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String deleteTable = ClassPathHelper.getFilePath("delete-table/delete-table2.text");

        String maxThreads = "3";

        String line = String.format("-data-source %s -delete-table %s -max-threads %s",
                dataSource, deleteTable, maxThreads);

        String[] args = line.split(" ");
        DeleteTableExecutor.INSTANCE.execute(args);
    }
}