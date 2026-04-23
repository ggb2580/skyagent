package com.hrbu.aidemo.security;

import com.hrbu.aidemo.exception.UnsafeSqlException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Say my name
 */
@Component
public class SqlSecurityValidator {
    @Value("${query.security.max-rows:100}")
    private int maxRows;

    //如果配置为空的话允许所有表
    @Value("${security.allowed-tables:}")
    private Set<String> allowedTables;

    @Value("${security.forbidden-keywords:}")
    private Set<String> forbiddenKeywords;


    public String validateAndEnhance(String originSql){
        //1.黑名单关键字检查
        String upperCaseSql = originSql.toUpperCase();
        for (String kw : forbiddenKeywords){
            if (upperCaseSql.contains(kw)){
                throw new UnsafeSqlException("禁止使用关键字："+kw);
            }
        }

        //2.解析SQL  必须是SELECT语句
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(originSql);
        } catch (JSQLParserException e) {
            throw new UnsafeSqlException("SQL 语法错误："+e.getMessage());
        }
        //不是SELECT语句
        if (!(statement instanceof Select)){
            throw new UnsafeSqlException("仅允许 SElECT 查询语句");
        }

        //3.提取表名并校验白名单
        Set<String> tableNames = extractTableNames((Select) statement);
        if (!allowedTables.isEmpty()){
            for (String table : tableNames){
                if (!allowedTables.contains(table.toLowerCase())){
                    throw new UnsafeSqlException("不允许查询表："+table);
                }
            }
        }

        //强制添加LIMIT
        return ensureLimit(originSql,(Select) statement);

    }


    /**
     * 递归提取 SELECT 语句中的所有表名
     */
    private Set<String> extractTableNames(Select select) {
        Set<String> tables = new HashSet<>();
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            PlainSelect plain = (PlainSelect) selectBody;
            // FROM 子句
            if (plain.getFromItem() != null) {
                addTableFromItem(plain.getFromItem(), tables);
            }
            // JOIN 子句
            if (plain.getJoins() != null) {
                plain.getJoins().forEach(join -> addTableFromItem(join.getRightItem(), tables));
            }
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOp = (SetOperationList) selectBody;
            setOp.getSelects().forEach(subSelect ->
                    tables.addAll(extractTableNames((Select) subSelect))
            );
        } else if (selectBody instanceof WithItem) {
            // 处理 WITH 子句（CTE），简化版，实际需要递归
            // 这里略，生产可完善
        }
        return tables;
    }

    private void addTableFromItem(net.sf.jsqlparser.statement.select.FromItem fromItem, Set<String> tables) {
        if (fromItem instanceof Table) {
            tables.add(((Table) fromItem).getName());
        } else if (fromItem instanceof SubSelect) {
            tables.addAll(extractTableNames((Select) ((SubSelect) fromItem).getSelectBody()));
        }
        // 其他类型如 Lateral, TableFunction 忽略
    }

    /**
     * 如果 SELECT 没有 LIMIT 且没有 TOP，则在末尾追加 LIMIT
     */
    private String ensureLimit(String originalSql, Select select) {
        SelectBody selectBody = select.getSelectBody();
        boolean hasLimit = false;
        if (selectBody instanceof PlainSelect) {
            PlainSelect plain = (PlainSelect) selectBody;
            if (plain.getLimit() != null || plain.getTop() != null) {
                hasLimit = true;
            }
        }
        // 简单实现：如果没有 limit，就在原 SQL 后拼接 " LIMIT " + maxRows
        // 注意：需要考虑原 SQL 末尾可能有分号，需要去掉
        if (!hasLimit) {
            String trimmed = originalSql.trim();
            if (trimmed.endsWith(";")) {
                trimmed = trimmed.substring(0, trimmed.length() - 1);
            }
            return trimmed + " LIMIT " + maxRows;
        }
        return originalSql;
    }

}
