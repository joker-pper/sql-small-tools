package com.joker17.sql.small.tools.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlAnalysisUtils {

    private static final String START_TAG = "---sql";

    private static final String END_TAG = "---";

    private SqlAnalysisUtils() {
    }

    /**
     * 根据sql内容解析为多个sql
     *
     * @param sqlText
     * @return
     */
    public static String[] getSqlResults(String sqlText) {
        String trimSql = StringUtils.trimToNull(sqlText);
        if (trimSql == null) {
            return null;
        }
        String[] results;
        if (StringUtils.countMatches(trimSql, START_TAG) > 0) {
            List<String> resultList = new ArrayList<>(16);
            String beforeStartTagSql = StringUtils.trimToNull(StringUtils.substringBefore(trimSql, START_TAG));
            if (beforeStartTagSql != null) {
                resultList.add(beforeStartTagSql);
            }
            String[] betweenResults = StringUtils.substringsBetween(trimSql, START_TAG, END_TAG);
            if (betweenResults != null && betweenResults.length > 0) {
                resultList.addAll(Arrays.asList(betweenResults));
            }
            results = resultList.toArray(new String[resultList.size()]);
        } else {
            results = new String[]{trimSql};
        }
        return results;
    }

    /**
     * 获取合并后的sql内容
     *
     * @param results
     * @return
     */
    public static String getMergeSqlText(String[] results) {
        if (results == null || results.length == 0) {
            return "";
        }
        if (results.length == 1) {
            return results[0];
        }
        return StringUtils.join(results, "\r\n");
    }
}
