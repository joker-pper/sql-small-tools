package com.joker17.sql.small.tools.manager.impl;

import com.joker17.sql.small.tools.manager.TableManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableManagerImpl implements TableManager {

    @Override
    public int update(JdbcTemplate jdbcTemplate, String sql) {
        return jdbcTemplate.update(sql);
    }

    @Override
    public int delete(JdbcTemplate jdbcTemplate, String sql) {
        return jdbcTemplate.update(sql);
    }

    @Override
    public void execute(JdbcTemplate jdbcTemplate, String sql) {
        jdbcTemplate.execute(sql);
    }

    @Override
    public void batchUpdate(JdbcTemplate jdbcTemplate, String... sql) {
        jdbcTemplate.batchUpdate(sql);
    }
}
