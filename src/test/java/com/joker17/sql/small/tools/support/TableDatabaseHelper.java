package com.joker17.sql.small.tools.support;

import org.springframework.jdbc.core.JdbcTemplate;

public class TableDatabaseHelper {

    private static final String[] tables = new String[]{"xx01", "xx02", "xx03"};

    public static String[] getTables() {
        return tables;
    }

    public static void initTable(JdbcTemplate jdbcTemplate) {
        String createTableSqlTemplate = "CREATE TABLE `%s` (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(128) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        for (int i = 0; i < tables.length; i++) {
            String table = tables[i];
            //删除table
            String dropTableSql = String.format("DROP TABLE IF EXISTS `%s`", table);
            jdbcTemplate.execute(dropTableSql);
            //创建table
            String createTableSql = String.format(createTableSqlTemplate, table);
            jdbcTemplate.execute(createTableSql);
        }
    }

}
