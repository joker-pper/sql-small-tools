package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.constants.ToolNames;
import com.joker17.sql.small.tools.helper.ReplaceTableExecuteHelper;
import com.joker17.sql.small.tools.param.ReplaceTableExecuteParam;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import com.joker17.sql.small.tools.utils.FileUtils;
import com.joker17.sql.small.tools.utils.JdbcUtils;
import com.joker17.sql.small.tools.utils.PropertiesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.util.Properties;

public class ReplaceTableExecuteExecutor extends AbstractExecutor<ReplaceTableExecuteParam> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceTableExecuteExecutor.class);

    public static final ReplaceTableExecuteExecutor INSTANCE = new ReplaceTableExecuteExecutor();

    private ReplaceTableExecuteExecutor() {
    }

    @Override
    public String name() {
        return ToolNames.REPLACE_TABLE_EXECUTE;
    }

    @Override
    protected void commandParseError(RuntimeException ex) {
        LOGGER.error("parse command args has error: ", ex);
    }

    @Override
    protected void doWork(ReplaceTableExecuteParam param) throws IOException {
        LOGGER.info("replace table execute begin: ");
        TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> {
            try {
                executeDoWork(param);
            } catch (Exception e) {
                LOGGER.error("replace table execute has error: ", e);
            }
            return null;
        });
        LOGGER.info("replace table execute end, take time: {}s", takeTimeTools.toExactSeconds());
    }

    private void executeDoWork(ReplaceTableExecuteParam param) throws IOException {
        String dataSourceProperties = param.getDataSourceProperties();
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        if (!FileUtils.isFileAndExists(dataSourcePropertiesFile)) {
            throw new FileNotFoundException(String.format("data source properties %s not found", dataSourceProperties));
        }

        String replaceTableExecuteProperties = param.getReplaceTableExecuteProperties();
        File replaceTableExecutePropertiesFile = new File(replaceTableExecuteProperties);
        if (!FileUtils.isFileAndExists(replaceTableExecutePropertiesFile)) {
            throw new FileNotFoundException(String.format("replace table execute properties %s not found", dataSourceProperties));
        }

        File sqlTextFile = new File(param.getSqlText());
        if (!FileUtils.isFileAndExists(sqlTextFile)) {
            throw new FileNotFoundException(String.format("sql text file %s not found", dataSourceProperties));
        }

        ReplaceTableExecuteConfig replaceTableExecuteConfig = convertReplaceTableExecuteConfig(replaceTableExecutePropertiesFile);
        ReplaceTableExecuteConfigTypeEnum replaceTableExecuteConfigTypeEnum = ReplaceTableExecuteConfigTypeEnum.getByValue(replaceTableExecuteConfig.getType());
        if (replaceTableExecuteConfigTypeEnum == null) {
            throw new IllegalArgumentException("not support replaceTableExecuteConfigType value: " + replaceTableExecuteConfig.getType());
        }

        //获取sql模板内容
        String sqlText = FileUtils.getAsString(sqlTextFile);

        JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);
        switch (replaceTableExecuteConfigTypeEnum) {
            case QUERY_VALUES:
                ReplaceTableExecuteHelper.execute(jdbcTemplate, replaceTableExecuteConfig.getTables(), sqlText, param.getMaxThreads());
                break;
            case VALUES:
                String tables[] = StringUtils.split(replaceTableExecuteConfig.getTables(), ",");
                ReplaceTableExecuteHelper.execute(jdbcTemplate, tables, sqlText, param.getMaxThreads());
                break;
            default:
                throw new UnsupportedOperationException("not support replaceTableExecuteConfigTypeEnum: " + replaceTableExecuteConfigTypeEnum.name());
        }
    }


    private ReplaceTableExecuteConfig convertReplaceTableExecuteConfig(File deleteTableConfigFile) throws IOException {
        Properties properties = PropertiesUtils.loadProperties(new FileInputStream(deleteTableConfigFile));
        ReplaceTableExecuteConfig replaceTableExecuteConfig = new ReplaceTableExecuteConfig();
        replaceTableExecuteConfig.setTables(properties.getProperty("tables"));
        replaceTableExecuteConfig.setType(Integer.valueOf(properties.getProperty("type")));
        return replaceTableExecuteConfig;
    }

    @Getter
    @Setter
    @ToString
    static class ReplaceTableExecuteConfig {
        private Integer type;
        private String tables;
    }

    enum ReplaceTableExecuteConfigTypeEnum {

        VALUES(1),

        QUERY_VALUES(2),

        ;
        private final int value;

        ReplaceTableExecuteConfigTypeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ReplaceTableExecuteConfigTypeEnum getByValue(int value) {
            for (ReplaceTableExecuteConfigTypeEnum typeEnum : ReplaceTableExecuteConfigTypeEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }

    }

    @Override
    protected ReplaceTableExecuteParam getParamInstance() {
        return new ReplaceTableExecuteParam();
    }
}
