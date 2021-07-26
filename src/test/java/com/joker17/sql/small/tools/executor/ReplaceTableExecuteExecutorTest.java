package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import com.joker17.sql.small.tools.support.TableDatabaseHelper;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ReplaceTableExecuteExecutorTest {

    @Before
    public void before() throws IOException {
        String dataSourceProperties = ClassPathHelper.getFilePath("db.properties");
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        TableDatabaseHelper.initTable(JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile));
    }

    @Test
    public void testByTables() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String replaceTableExecute = ClassPathHelper.getFilePath("replace-table-execute/replace-table-execute1.text");
        String sqlText = ClassPathHelper.getFilePath("replace-table-execute/replace-table-execute-sql.text");

        String maxThreads = "3";

        String line = String.format("-data-source %s -replace-table-execute %s -sql-text %s -max-threads %s",
                dataSource, replaceTableExecute, sqlText, maxThreads);

        String[] args = line.split(" ");
        ReplaceTableExecuteExecutor.INSTANCE.execute(args);
    }

    @Test
    public void testByTablesQuerySql() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String replaceTableExecute = ClassPathHelper.getFilePath("replace-table-execute/replace-table-execute2.text");
        String sqlText = ClassPathHelper.getFilePath("replace-table-execute/replace-table-execute-sql.text");

        String maxThreads = "3";
        String line = String.format("-data-source %s -replace-table-execute %s -sql-text %s -max-threads %s",
                dataSource, replaceTableExecute, sqlText, maxThreads);

        String[] args = line.split(" ");
        ReplaceTableExecuteExecutor.INSTANCE.execute(args);
    }
}