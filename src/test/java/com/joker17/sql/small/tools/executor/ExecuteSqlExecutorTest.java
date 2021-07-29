package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.support.ClassPathHelper;
import org.junit.Test;

import java.io.IOException;

public class ExecuteSqlExecutorTest {


    @Test
    public void test1() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String sqlText = ClassPathHelper.getFilePath("execute-sql/execute-sql1.text");

        String maxThreads = "1";

        String line = String.format("-data-source %s -sql-text %s -max-threads %s",
                dataSource, sqlText, maxThreads);

        String[] args = line.split(" ");
        ExecuteSqlExecutor.INSTANCE.execute(args);
    }

    @Test
    public void test2() throws IOException {

        String dataSource = ClassPathHelper.getFilePath("db.properties");
        String sqlText = ClassPathHelper.getFilePath("execute-sql/execute-sql2.text");

        String maxThreads = "1";

        String line = String.format("-data-source %s -sql-text %s -max-threads %s",
                dataSource, sqlText, maxThreads);

        String[] args = line.split(" ");
        ExecuteSqlExecutor.INSTANCE.execute(args);
    }

}