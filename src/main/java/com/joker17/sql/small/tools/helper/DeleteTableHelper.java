package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.factory.SqlManagerFactory;
import com.joker17.sql.small.tools.manager.TableManager;
import com.joker17.sql.small.tools.support.AllocationTaskSupport;
import com.joker17.sql.small.tools.support.MultipleThreadRunner;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DeleteTableHelper {

    private static final DeleteTableHelperManager HELPER_MANAGER = new DeleteTableHelperManagerImpl();

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTableHelper.class);

    private DeleteTableHelper() {
    }

    /**
     * 通过指定的table查询列表sql、删除条件及最大线程数进行执行删除语句
     *
     * @param jdbcTemplate
     * @param tablesQuerySql
     * @param deleteCondition
     * @param maxThreads
     */
    public static void delete(JdbcTemplate jdbcTemplate, String tablesQuerySql, String deleteCondition, int maxThreads) {
        List<String> tableList = jdbcTemplate.queryForList(tablesQuerySql, String.class);
        String[] tables;
        if (tableList != null && !tableList.isEmpty()) {
            tables = tableList.toArray(new String[tableList.size()]);
        } else {
            throw new IllegalArgumentException("table query sql not found tables: " + tablesQuerySql);
        }
        HELPER_MANAGER.delete(jdbcTemplate, tables, deleteCondition, maxThreads);
    }


    /**
     * 通过指定的table列表、删除条件及最大线程数进行执行删除语句
     *
     * @param jdbcTemplate
     * @param tables
     * @param deleteCondition
     * @param maxThreads
     */
    public static void delete(JdbcTemplate jdbcTemplate, String[] tables, String deleteCondition, int maxThreads) {
        HELPER_MANAGER.delete(jdbcTemplate, tables, deleteCondition, maxThreads);
    }


    interface DeleteTableHelperManager {

        /**
         * 通过指定的table列表、删除条件及最大线程数进行执行删除语句
         *
         * @param jdbcTemplate
         * @param tables
         * @param deleteCondition
         * @param maxThreads
         */
        void delete(JdbcTemplate jdbcTemplate, String[] tables, String deleteCondition, int maxThreads);

    }

    static class DeleteTableHelperManagerImpl implements DeleteTableHelperManager {

        private final static TableManager TABLE_MANAGER = SqlManagerFactory.getTableManagerInstance();

        @Override
        public void delete(JdbcTemplate jdbcTemplate, String[] tables, String deleteCondition, int maxThreads) {
            if (tables == null || tables.length == 0) {
                throw new IllegalArgumentException("tables must be not empty");
            }

            if (maxThreads <= 0) {
                throw new IllegalArgumentException("maxThreads must be gt 0");
            }

            final String finalDeleteCondition = StringUtils.trimToEmpty(deleteCondition);
            int tableSize = tables.length;
            if (maxThreads == 1 || tableSize == 1) {
                for (int i = 0; i < tableSize; i++) {
                    final String table = tables[i];
                    executeDelete(table, finalDeleteCondition, jdbcTemplate);
                }
            } else {
                MultipleThreadRunner multipleThreadRunner = new MultipleThreadRunner();
                if (tableSize <= maxThreads) {
                    for (int i = 0; i < tableSize; i++) {
                        final String table = tables[i];
                        multipleThreadRunner.addTask(() -> executeDelete(table, finalDeleteCondition, jdbcTemplate));
                    }
                } else {
                    AllocationTaskSupport allocationTaskSupport = AllocationTaskSupport.of(tableSize, maxThreads);

                    for (int i = 0; i < maxThreads; i++) {
                        final int threadIndex = i;
                        int[] betweenIndexResults = allocationTaskSupport.getStartIndexAndEndIndex(threadIndex);
                        multipleThreadRunner.addTask(() -> {
                            //当前线程要处理的start index -> end index
                            for (int j = betweenIndexResults[0]; j <= betweenIndexResults[1]; j++) {
                                executeDelete(tables[j], finalDeleteCondition, jdbcTemplate);
                            }

                            //当前线程要处理的surplus index
                            int surplusIndex = allocationTaskSupport.getSurplusIndex(threadIndex);
                            if (surplusIndex != -1) {
                                executeDelete(tables[surplusIndex], finalDeleteCondition, jdbcTemplate);
                            }
                        });
                    }
                }
                multipleThreadRunner.executeTask();
            }
        }

        private void executeDelete(String table, String deleteCondition, JdbcTemplate jdbcTemplate) {
            String sql = "DELETE FROM " + table + " " + deleteCondition;
            executeDelete(sql, jdbcTemplate);
        }

        private void executeDelete(String sql, JdbcTemplate jdbcTemplate) {
            TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.delete(jdbcTemplate, sql));
            LOGGER.info("{} ==> delete rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
        }


    }
}
