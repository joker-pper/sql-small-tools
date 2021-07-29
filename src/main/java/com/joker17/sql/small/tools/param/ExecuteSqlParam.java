package com.joker17.sql.small.tools.param;

import com.beust.jcommander.Parameter;
import com.joker17.sql.small.tools.executor.BaseExecutorParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ExecuteSqlParam extends BaseExecutorParam {

    /**
     * 数据源配置文件
     */
    @Parameter(names = {"-data-source"}, required = true, description = "data source properties")
    private String dataSourceProperties;

    /**
     * sql配置文件
     */
    @Parameter(names = {"-sql-text"}, required = true, description = "sql text file")
    private String sqlText;

    /**
     * 最大线程数
     */
    @Parameter(names = {"-max-threads"}, required = true, description = "max threads")
    private int maxThreads;

}
