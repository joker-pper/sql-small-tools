package com.joker17.sql.small.tools.enums;

import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;

public enum SqlTypeEnum {

    SELECT,

    INSERT,

    UPDATE,

    DELETE,

    OTHER;

    public static SqlTypeEnum getBySql(String text) {
        String sql = StringUtils.trimToEmpty(text);
        int sqlLength = sql.length();
        if (sqlLength == 0) {
            return OTHER;
        }
        String sqlPrefix = sql.substring(0, sqlLength >= 10 ? 10 : sqlLength);
        String sqlPrefixUpperCase = StringUtils.upperCase(sqlPrefix);
        for (SqlTypeEnum sqlTypeEnum : Arrays.asList(UPDATE, DELETE, INSERT, SELECT)) {
            if (sqlPrefixUpperCase.startsWith(sqlTypeEnum.name())) {
                return sqlTypeEnum;
            }
        }
        return OTHER;
    }

}
