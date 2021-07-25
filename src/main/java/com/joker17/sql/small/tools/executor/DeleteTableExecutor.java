package com.joker17.sql.small.tools.executor;

import com.joker17.sql.small.tools.constants.ToolNames;
import com.joker17.sql.small.tools.param.DeleteTableParam;
import com.joker17.sql.small.tools.helper.DeleteTableHelper;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DeleteTableExecutor extends AbstractExecutor<DeleteTableParam> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTableExecutor.class);

    public static final DeleteTableExecutor INSTANCE = new DeleteTableExecutor();

    private DeleteTableExecutor() {
    }

    @Override
    public String name() {
        return ToolNames.DELETE_TABLE;
    }

    @Override
    protected void commandParseError(RuntimeException ex) {
        LOGGER.error("parse command args has error: ", ex);
    }

    @Override
    protected void doWork(DeleteTableParam param) throws IOException {
        LOGGER.info("delete table begin: ");
        TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> {
            try {
                executeDoWork(param);
            } catch (Exception e) {
                LOGGER.error("delete table has error: ", e);
            }
            return null;
        });
        LOGGER.info("delete table end, take time: {}s", takeTimeTools.toExactSeconds());
    }

    private void executeDoWork(DeleteTableParam param) throws IOException {
        String dataSourceProperties = param.getDataSourceProperties();
        File dataSourcePropertiesFile = new File(dataSourceProperties);
        if (!FileUtils.isFileAndExists(dataSourcePropertiesFile)) {
            throw new FileNotFoundException(String.format("data source properties %s not found", dataSourceProperties));
        }

        String deleteTableProperties = param.getDeleteTableProperties();
        File deleteTablePropertiesFile = new File(deleteTableProperties);
        if (!FileUtils.isFileAndExists(deleteTablePropertiesFile)) {
            throw new FileNotFoundException(String.format("delete table properties %s not found", dataSourceProperties));
        }

        DeleteTableConfig deleteTableConfig = convertDeleteTableConfig(deleteTablePropertiesFile);
        DeleteTableConfigTypeEnum deleteTableConfigTypeEnum = DeleteTableConfigTypeEnum.getByValue(deleteTableConfig.getType());
        if (deleteTableConfigTypeEnum == null) {
            throw new IllegalArgumentException("not support deleteTableConfigType value: " + deleteTableConfig.getType());
        }

        JdbcTemplate jdbcTemplate = JdbcUtils.getJdbcTemplate(dataSourcePropertiesFile);
        switch (deleteTableConfigTypeEnum) {
            case QUERY_VALUES:
                DeleteTableHelper.delete(jdbcTemplate, deleteTableConfig.getTables(), deleteTableConfig.getDeleteCondition(), param.getMaxThreads());
                break;
            case VALUES:
                String tables[] = StringUtils.split(deleteTableConfig.getTables(), ",");
                DeleteTableHelper.delete(jdbcTemplate, tables, deleteTableConfig.getDeleteCondition(), param.getMaxThreads());
                break;
            default:
                throw new UnsupportedOperationException("not support deleteTableConfigTypeEnum: " + deleteTableConfigTypeEnum.name());
        }
    }


    private DeleteTableConfig convertDeleteTableConfig(File deleteTableConfigFile) throws IOException {
        Properties properties = PropertiesUtils.loadProperties(new FileInputStream(deleteTableConfigFile));
        DeleteTableConfig deleteTableConfig = new DeleteTableConfig();
        deleteTableConfig.setTables(properties.getProperty("tables"));
        deleteTableConfig.setDeleteCondition(properties.getProperty("delete-condition"));
        deleteTableConfig.setType(Integer.valueOf(properties.getProperty("type")));
        return deleteTableConfig;
    }

    @Getter
    @Setter
    @ToString
    static class DeleteTableConfig {
        private Integer type;
        private String tables;
        private String deleteCondition;
    }

    enum DeleteTableConfigTypeEnum {

        VALUES(1),

        QUERY_VALUES(2),

        ;
        private final int value;

        DeleteTableConfigTypeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DeleteTableConfigTypeEnum getByValue(int value) {
            for (DeleteTableConfigTypeEnum typeEnum : DeleteTableConfigTypeEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }

    }

    @Override
    protected DeleteTableParam getParamInstance() {
        return new DeleteTableParam();
    }
}
