package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.enums.SqlTypeEnum;
import com.joker17.sql.small.tools.factory.SqlManagerFactory;
import com.joker17.sql.small.tools.manager.TableManager;
import com.joker17.sql.small.tools.support.AllocationTaskSupport;
import com.joker17.sql.small.tools.support.MultipleThreadRunner;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExecuteSqlHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteSqlHelper.class);
    private final static TableManager TABLE_MANAGER = SqlManagerFactory.getTableManagerInstance();

    private static final ExecuteSqlHelperManager HELPER_MANAGER = new ExecuteSqlHelperManagerImpl();

    private ExecuteSqlHelper() {
    }

    public static void execute(JdbcTemplate jdbcTemplate, String[] sqls, int maxThreads) {
        HELPER_MANAGER.execute(jdbcTemplate, sqls, maxThreads);
    }


    public static void execute(JdbcTemplate jdbcTemplate, String sql, Logger logger) {
        SqlTypeEnum sqlTypeEnum = SqlTypeEnum.getBySql(sql);
        switch (sqlTypeEnum) {
            case UPDATE:
                TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.update(jdbcTemplate, sql));
                logger.info("{} ==> update rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                break;
            case DELETE:
                takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.delete(jdbcTemplate, sql));
                logger.info("{} ==> delete rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                break;
            case INSERT:
                takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.update(jdbcTemplate, sql));
                logger.info("{} ==> insert rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                break;
            case SELECT:
                takeTimeTools = TakeTimeTools.of(() -> {
                    TABLE_MANAGER.execute(jdbcTemplate, sql);
                    return null;
                });
                logger.info("{} ==> select take time: {}s", sql, takeTimeTools.toExactSeconds());
                break;
            case OTHER:
                takeTimeTools = TakeTimeTools.of(() -> {
                    TABLE_MANAGER.execute(jdbcTemplate, sql);
                    return null;
                });
                logger.info("{} ==> other take time: {}s", sql, takeTimeTools.toExactSeconds());
                break;
            default:
                throw new UnsupportedOperationException("not support sqlTypeEnum: " + sqlTypeEnum.name());
        }

    }

    interface ExecuteSqlHelperManager {
        void execute(JdbcTemplate jdbcTemplate, String[] sqls, int maxThreads);
    }


    static class ExecuteSqlHelperManagerImpl implements ExecuteSqlHelperManager {

        @Override
        public void execute(JdbcTemplate jdbcTemplate, String[] sqls, int maxThreads) {
            if (sqls == null || sqls.length == 0) {
                throw new IllegalArgumentException("sqls must be not empty");
            }

            if (maxThreads <= 0) {
                throw new IllegalArgumentException("maxThreads must be gt 0");
            }

            int sqlSize = sqls.length;
            if (maxThreads == 1 || sqlSize == 1) {
                for (int i = 0; i < sqlSize; i++) {
                    final String sql = sqls[i];
                    execute(sql, jdbcTemplate);
                }
            } else {
                MultipleThreadRunner multipleThreadRunner = new MultipleThreadRunner();
                if (sqlSize <= maxThreads) {
                    for (int i = 0; i < sqlSize; i++) {
                        final String sql = sqls[i];
                        multipleThreadRunner.addTask(() -> execute(sql, jdbcTemplate));
                    }
                } else {
                    AllocationTaskSupport allocationTaskSupport = AllocationTaskSupport.of(sqlSize, maxThreads);

                    for (int i = 0; i < maxThreads; i++) {
                        final int threadIndex = i;
                        int[] betweenIndexResults = allocationTaskSupport.getStartIndexAndEndIndex(threadIndex);
                        multipleThreadRunner.addTask(() -> {
                            //当前线程要处理的start index -> end index
                            for (int j = betweenIndexResults[0]; j <= betweenIndexResults[1]; j++) {
                                execute(sqls[j], jdbcTemplate);
                            }

                            //当前线程要处理的surplus index
                            int surplusIndex = allocationTaskSupport.getSurplusIndex(threadIndex);
                            if (surplusIndex != -1) {
                                execute(sqls[surplusIndex], jdbcTemplate);
                            }
                        });
                    }
                }
                multipleThreadRunner.executeTask();
            }
        }


        private void execute(String sql, JdbcTemplate jdbcTemplate) {
            ExecuteSqlHelper.execute(jdbcTemplate, sql, LOGGER);
        }

    }

}
