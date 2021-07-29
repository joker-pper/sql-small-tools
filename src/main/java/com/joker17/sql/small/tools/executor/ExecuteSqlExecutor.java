package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.constants.ToolNames;
import com.joker17.sql.small.tools.helper.ExecuteSqlHelper;
import com.joker17.sql.small.tools.param.ExecuteSqlParam;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import com.joker17.sql.small.tools.utils.FileUtils;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import com.joker17.sql.small.tools.utils.SqlAnalysisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExecuteSqlExecutor extends AbstractExecutor<ExecuteSqlParam> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteSqlExecutor.class);

    public static final ExecuteSqlExecutor INSTANCE = new ExecuteSqlExecutor();

    private ExecuteSqlExecutor() {
    }

    @Override
    public String name() {
        return ToolNames.EXECUTE_SQL;
    }

    @Override
    protected void commandParseError(RuntimeException ex) {
        LOGGER.error("parse command args has error: ", ex);
    }

    @Override
    protected void doWork(ExecuteSqlParam param) throws IOException {
        LOGGER.info("execute sql begin: ");
        TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> {
            try {
                executeDoWork(param);
            } catch (Exception e) {
                LOGGER.error("execute sql has error: ", e);
            }
            return null;
        });
        LOGGER.info("execute sql end, take time: {}s", takeTimeTools.toExactSeconds());
    }

    private void executeDoWork(ExecuteSqlParam param) throws IOException {
        String dataSourceProperties = param.getDataSourceProperties();
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        if (!FileUtils.isFileAndExists(dataSourcePropertiesFile)) {
            throw new FileNotFoundException(String.format("data source properties %s not found", dataSourceProperties));
        }

        File sqlTextFile = new File(param.getSqlText());
        if (!FileUtils.isFileAndExists(sqlTextFile)) {
            throw new FileNotFoundException(String.format("sql text file %s not found", dataSourceProperties));
        }

        //获取sql模板内容
        String sqlText = FileUtils.getAsString(sqlTextFile);
        JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);

        //获取多组sql结果
        String[] sqlResults = SqlAnalysisUtils.getSqlResults(sqlText);
        ExecuteSqlHelper.execute(jdbcTemplate, sqlResults, param.getMaxThreads());
    }

    @Override
    protected ExecuteSqlParam getParamInstance() {
        return new ExecuteSqlParam();
    }
}
