package com.joker17.sql.small.tools.helper;

import com.joker17.sql.small.tools.enums.SqlTypeEnum;
import com.joker17.sql.small.tools.factory.SqlManagerFactory;
import com.joker17.sql.small.tools.manager.TableManager;
import com.joker17.sql.small.tools.support.MultipleThreadRunner;
import com.joker17.sql.small.tools.support.TakeTimeTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExecuteSqlHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteSqlHelper.class);

    private static final ExecuteSqlHelperManager HELPER_MANAGER = new ExecuteSqlHelperManagerImpl();

    private ExecuteSqlHelper() {
    }

    public static void execute(JdbcTemplate jdbcTemplate, String[] sqls, int maxThreads) {
        HELPER_MANAGER.execute(jdbcTemplate, sqls, maxThreads);
    }

    interface ExecuteSqlHelperManager {
        void execute(JdbcTemplate jdbcTemplate, String[] sqls, int maxThreads);
    }


    static class ExecuteSqlHelperManagerImpl implements ExecuteSqlHelperManager {

        private final static TableManager TABLE_MANAGER = SqlManagerFactory.getTableManagerInstance();

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
                    int avgNum = sqlSize / maxThreads;
                    int surplusNum = sqlSize % maxThreads;
                    for (int i = 0; i < maxThreads; i++) {
                        final int threadIndex = i;
                        multipleThreadRunner.addTask(() -> {
                            //每个线程平均要处理的table
                            for (int j = 0; j < avgNum; j++) {
                                //计算索引位置
                                int sqlIndex = threadIndex * avgNum + j;
                                execute(sqls[sqlIndex], jdbcTemplate);
                            }

                            //剩余要处理的table
                            if (threadIndex < surplusNum) {
                                //计算索引位置
                                int sqlIndex = maxThreads * avgNum + threadIndex;
                                execute(sqls[sqlIndex], jdbcTemplate);
                            }
                        });
                    }
                }
                multipleThreadRunner.executeTask();
            }
        }


        private void execute(String sql, JdbcTemplate jdbcTemplate) {
            SqlTypeEnum sqlTypeEnum = SqlTypeEnum.getBySql(sql);
            switch (sqlTypeEnum) {
                case UPDATE:
                    TakeTimeTools takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.update(jdbcTemplate, sql));
                    LOGGER.info("{} ==> update rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                    break;
                case DELETE:
                    takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.delete(jdbcTemplate, sql));
                    LOGGER.info("{} ==> delete rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                    break;
                case INSERT:
                    takeTimeTools = TakeTimeTools.of(() -> TABLE_MANAGER.update(jdbcTemplate, sql));
                    LOGGER.info("{} ==> insert rows: {}, take time: {}s", sql, takeTimeTools.getResult(), takeTimeTools.toExactSeconds());
                    break;
                case SELECT:
                    takeTimeTools = TakeTimeTools.of(() -> {
                        TABLE_MANAGER.execute(jdbcTemplate, sql);
                        return null;
                    });
                    LOGGER.info("{} ==> select take time: {}s", sql, takeTimeTools.toExactSeconds());
                    break;
                case OTHER:
                    takeTimeTools = TakeTimeTools.of(() -> {
                        TABLE_MANAGER.execute(jdbcTemplate, sql);
                        return null;
                    });
                    LOGGER.info("{} ==> other take time: {}s", sql, takeTimeTools.toExactSeconds());
                    break;
                default:
                    throw new UnsupportedOperationException("not support sqlTypeEnum: " + sqlTypeEnum.name());
            }

        }


    }

}
