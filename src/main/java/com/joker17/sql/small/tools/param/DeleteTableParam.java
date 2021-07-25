package com.joker17.sql.small.tools.param;

import com.beust.jcommander.Parameter;
import com.joker17.sql.small.tools.executor.BaseExecutorParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class DeleteTableParam extends BaseExecutorParam {

    /**
     * 数据源配置文件
     */
    @Parameter(names = {"-data-source"}, required = true, description = "data source properties")
    private String dataSourceProperties;

    /**
     * 删除table配置文件
     */
    @Parameter(names = {"-delete-table"}, required = true, description = "delete table properties")
    private String deleteTableProperties;

    /**
     * 最大线程数
     */
    @Parameter(names = {"-max-threads"}, required = true, description = "max threads")
    private int maxThreads;

}
