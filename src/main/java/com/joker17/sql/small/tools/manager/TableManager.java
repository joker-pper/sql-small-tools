package com.joker17.sql.small.tools.manager;

import org.springframework.jdbc.core.JdbcTemplate;

public interface TableManager {

    /**
     * 执行更新语句并返回影响行数
     *
     * @param jdbcTemplate
     * @param sql
     * @return
     */
    int update(JdbcTemplate jdbcTemplate, String sql);


    /**
     * 执行删除语句并返回影响行数
     *
     * @param jdbcTemplate
     * @param sql
     * @return
     */
    int delete(JdbcTemplate jdbcTemplate, String sql);

    void execute(JdbcTemplate jdbcTemplate, String sql);

    void batchUpdate(JdbcTemplate jdbcTemplate, String... sql);

}
