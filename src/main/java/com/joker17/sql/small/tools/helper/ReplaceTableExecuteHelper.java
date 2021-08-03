package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.factory.SqlManagerFactory;
import com.joker17.sql.small.tools.manager.TableManager;
import com.joker17.sql.small.tools.support.AllocationTaskSupport;
import com.joker17.sql.small.tools.support.MultipleThreadRunner;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import com.joker17.sql.small.tools.utils.SqlAnalysisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ReplaceTableExecuteHelper {

    private static final ReplaceTableHelperManager HELPER_MANAGER = new ReplaceTableHelperManagerImpl();

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceTableExecuteHelper.class);

    private ReplaceTableExecuteHelper() {
    }

    /**
     * 通过指定的table查询列表sql、要执行的sql模板及最大线程数进行执行语句
     *
     * @param jdbcTemplate
     * @param tablesQuerySql
     * @param sqlText
     * @param maxThreads
     */
    public static void execute(JdbcTemplate jdbcTemplate, String tablesQuerySql, String sqlText, int maxThreads) {
        List<String> tableList = jdbcTemplate.queryForList(tablesQuerySql, String.class);
        String[] tables;
        if (tableList != null && !tableList.isEmpty()) {
            tables = tableList.toArray(new String[tableList.size()]);
        } else {
            throw new IllegalArgumentException("table query sql not found tables: " + tablesQuerySql);
        }
        HELPER_MANAGER.execute(jdbcTemplate, tables, sqlText, maxThreads);
    }


    /**
     * 通过指定的table列表、要执行的sql模板及最大线程数进行执行语句
     *
     * @param jdbcTemplate
     * @param tables
     * @param sqlText
     * @param maxThreads
     */
    public static void execute(JdbcTemplate jdbcTemplate, String[] tables, String sqlText, int maxThreads) {
        HELPER_MANAGER.execute(jdbcTemplate, tables, sqlText, maxThreads);
    }

    interface ReplaceTableHelperManager {

        /**
         * 通过指定的table列表、要执行的sql模板及最大线程数进行执行语句
         *
         * @param jdbcTemplate
         * @param tables
         * @param sqlText
         * @param maxThreads
         */
        void execute(JdbcTemplate jdbcTemplate, String[] tables, String sqlText, int maxThreads);

    }

    static class ReplaceTableHelperManagerImpl implements ReplaceTableHelperManager {

        private final static TableManager TABLE_MANAGER = SqlManagerFactory.getTableManagerInstance();

        @Override
        public void execute(JdbcTemplate jdbcTemplate, String[] tables, String sqlText, int maxThreads) {
            if (tables == null || tables.length == 0) {
                throw new IllegalArgumentException("tables must be not empty");
            }

            if (maxThreads <= 0) {
                throw new IllegalArgumentException("maxThreads must be gt 0");
            }

            if (StringUtils.isEmpty(sqlText)) {
                throw new IllegalArgumentException("sql must be not empty");
            }

            int tableSize = tables.length;
            if (maxThreads == 1 || tableSize == 1) {
                for (int i = 0; i < tableSize; i++) {
                    final String table = tables[i];
                    execute(table, sqlText, jdbcTemplate);
                }
            } else {
                MultipleThreadRunner multipleThreadRunner = new MultipleThreadRunner();
                if (tableSize <= maxThreads) {
                    for (int i = 0; i < tableSize; i++) {
                        final String table = tables[i];
                        multipleThreadRunner.addTask(() -> execute(table, sqlText, jdbcTemplate));
                    }
                } else {
                    AllocationTaskSupport allocationTaskSupport = AllocationTaskSupport.of(tableSize, maxThreads);

                    for (int i = 0; i < maxThreads; i++) {
                        final int threadIndex = i;
                        int[] betweenIndexResults = allocationTaskSupport.getStartIndexAndEndIndex(threadIndex);
                        multipleThreadRunner.addTask(() -> {
                            //当前线程要处理的start index -> end index
                            for (int j = betweenIndexResults[0]; j <= betweenIndexResults[1]; j++) {
                                execute(tables[j], sqlText, jdbcTemplate);
                            }

                            //当前线程要处理的surplus index
                            int surplusIndex = allocationTaskSupport.getSurplusIndex(threadIndex);
                            if (surplusIndex != -1) {
                                execute(tables[surplusIndex], sqlText, jdbcTemplate);
                            }
                        });
                    }
                }
                multipleThreadRunner.executeTask();
            }
        }

        private void execute(String table, String sqlText, JdbcTemplate jdbcTemplate) {
            //替换sql模板中的table占位符为实际值
            sqlText = StringUtils.replace(sqlText, "#{table}", table);
            String[] toExecuteSqlResults = SqlAnalysisUtils.getSqlResults(sqlText);
            String toExecuteSql;
            if (toExecuteSqlResults.length == 1) {
                toExecuteSql = toExecuteSqlResults[0];
            } else {
                toExecuteSql = SqlAnalysisUtils.getMergeSqlText(toExecuteSqlResults);
            }
            TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> {
                TABLE_MANAGER.update(jdbcTemplate, toExecuteSql);
                return null;
            });
            LOGGER.info("{} ==> execute take time: {}s", toExecuteSql, takeTimeTools.toExactSeconds());
        }
    }

}
