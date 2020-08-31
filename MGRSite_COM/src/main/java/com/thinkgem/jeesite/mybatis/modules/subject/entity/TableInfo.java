package com.thinkgem.jeesite.mybatis.modules.subject.entity;

/**
 * 图片存储表信息
 * @author yangkq
 * @version 1.0
 * @date 2020/3/2
 */
public class TableInfo {
    private String columnName;
    private String dataType;
    private String characterMaximumLength;
    private String columnComment;

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public String getColumnComment() {
        return columnComment;
    }
}
