package com.zaoan.zaoan_news.zaoan_news_server.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * SQL 工具
 *
 * @author Jingzhen
 */
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        // 允许的字段列表，可以根据需求添加更多合法字段
        List<String> validFields = Arrays.asList("updated_at", "created_at", "id", "title");
        return validFields.contains(sortField) && !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
