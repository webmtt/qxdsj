package org.pentaho.di.core.database;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.util.Utils;

public class GbaseDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private static final int VARCHAR_LIMIT = 65_535;

    /**
     * 获取数据库的可能访问类型列表
     *
     * @param
     * @return int[]    通过JDBC驱动程序本地连接到数据库
     **/
    @Override
    public int[] getAccessTypeList() {
        return new int[]{DatabaseMeta.TYPE_ACCESS_NATIVE};
    }

    /**
     * 默认的数据库端口号
     *
     * @param
     * @return int
     **/
    @Override
    public int getDefaultDatabasePort() {
        if (getAccessType() == DatabaseMeta.TYPE_ACCESS_NATIVE) {
            return 5258;
        }
        return -1;
    }

    /**
     * 要限制查询结果的行数
     *
     * @param nrRows
     * @return java.lang.String
     **/
    @Override
    public String getLimitClause(int nrRows) {
        return " LIMIT " + nrRows;
    }

    /**
     * Returns the minimal SQL to launch in order to determine the layout of the resultset for a given database table
     * 要启动的最小SQL，以确定给定数据库表的resultset布局 用于确定布局的表的名称
     *
     * @param tableName The name of the table to determine the layout for
     * @return The SQL to launch.
     */
    @Override
    public String getSQLQueryFields(String tableName) {
        return "SELECT * FROM " + tableName + " LIMIT 0";
    }

    /**
     * 验证表是否存在
     *
     * @param tablename
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:26
     **/
    @Override
    public String getSQLTableExists(String tablename) {
        return getSQLQueryFields(tablename);
    }

    /**
     * 验证 列 是否存在
     *
     * @param columnname
     * @param tablename
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:27
     **/
    @Override
    public String getSQLColumnExists(String columnname, String tablename) {
        return getSQLQueryColumnFields(columnname, tablename);
    }

    public String getSQLQueryColumnFields(String columnname, String tableName) {
        return "SELECT " + columnname + " FROM " + tableName + " LIMIT 0";
    }

    /**
     * @see DatabaseInterface#getNotFoundTK(boolean)
     */
    @Override
    public int getNotFoundTK(boolean use_autoinc) {
        if (supportsAutoInc() && use_autoinc) {
            return 1;
        }
        return super.getNotFoundTK(use_autoinc);
    }

    /**
     * 获取我们需要使用的JDBC驱动程序类的名称
     *
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:28
     **/
    @Override
    public String getDriverClass() {
        if (getAccessType() == DatabaseMeta.TYPE_ACCESS_ODBC) {
            return "com.gbase.jdbc.Driver";
        } else {
            return "com.gbase.jdbc.Driver";
        }
    }

    /**
     * 用于连接到数据库的URL
     *
     * @param hostname
     * @param port
     * @param databaseName
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:29
     **/
    @Override
    public String getURL(String hostname, String port, String databaseName) {
        if (getAccessType() == DatabaseMeta.TYPE_ACCESS_ODBC) {
            return "jdbc:gbase:" + databaseName;
        } else {
            // jdbc:gbase://[host:port],[host:port].../[database][?propertyName
            // 1][=propertyValue1][&propertyName2][=propertyValue2]..
            if (Utils.isEmpty(port)) {
                return "jdbc:gbase://" + hostname + "/" + databaseName;
            } else {
                return "jdbc:gbase://" + hostname + ":" + port + "/" + databaseName;
            }
        }
    }

    /**
     * 此平台的数据库URL中的额外选项分隔符
     *
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:31
     **/
    @Override
    public String getExtraOptionSeparator() {
        return "&";
    }

    /**
     * url参数分隔
     *
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 11:31
     **/
    @Override
    public String getExtraOptionIndicator() {
        return "?";
    }

    /**
     * 是否支持事物
     */
    @Override
    public boolean supportsTransactions() {
        return false;
    }

    /**
     * 是否支持位图索引
     */
    @Override
    public boolean supportsBitmapIndex() {
        return false;
    }

    /**
     * 支持视图
     */
    @Override
    public boolean supportsViews() {
        return true;
    }

    /**
     * @return true if the database supports synonyms
     */
    @Override
    public boolean supportsSynonyms() {
        return true;
    }

    /**
     * Generates the SQL statement to add a column to the specified table
     * 生成向指定表添加列的SQL语句
     *
     * @param tablename   The table to add
     * @param v           The column defined as a value
     * @param tk          the name of the technical key field
     * @param use_autoinc whether or not this field uses auto increment
     * @param pk          the name of the primary key field
     * @param semicolon   whether or not to add a semi-colon behind the statement.
     * @return the SQL statement to add a column to the specified table
     */
    @Override
    public String getAddColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc,
                                        String pk, boolean semicolon) {
        return "ALTER TABLE " + tablename + " ADD " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
    }

    /**
     * Generates the SQL statement to modify a column in the specified table
     * 生成用于修改指定表中的列结构的SQL语句
     *
     * @param tablename   The table to add
     * @param v           The column defined as a value
     * @param tk          the name of the technical key field
     * @param use_autoinc whether or not this field uses auto increment
     * @param pk          the name of the primary key field
     * @param semicolon   whether or not to add a semi-colon behind the statement.
     * @return the SQL statement to modify a column in the specified table
     */
    @Override
    public String getModifyColumnStatement(String tablename, ValueMetaInterface v, String tk,
                                           boolean use_autoinc, String pk, boolean semicolon) {
        return "ALTER TABLE " + tablename + " MODIFY " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
    }

    /**
     * 将一个值 描述为 数据库中的一个字段
     *
     * @param v             定义为值的列
     * @param tk
     * @param pk            主键
     * @param use_autoinc   是否使用自动递增
     * @param add_fieldname 是否将字段名添加到定义中
     * @param add_cr        是否在定义的末尾添加一个
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/7/1 14:31
     **/
    @Override
    public String getFieldDefinition(ValueMetaInterface v, String tk, String pk, boolean use_autoinc,
                                     boolean add_fieldname, boolean add_cr) {
        String retval = "";

        String fieldname = v.getName();
        if (v.getLength() == DatabaseMeta.CLOB_LENGTH) {
            v.setLength(getMaxTextFieldLength());
        }
        int length = v.getLength();
        int precision = v.getPrecision();

        if (add_fieldname) {
            retval += fieldname + " ";
        }

        int type = v.getType();
        switch (type) {
            case ValueMetaInterface.TYPE_TIMESTAMP:
            case ValueMetaInterface.TYPE_DATE:
                retval += "DATETIME";
                break;
            case ValueMetaInterface.TYPE_BOOLEAN:
                if (supportsBooleanDataType()) {
                    retval += "BOOLEAN";
                } else {
                    retval += "CHAR(1)";
                }
                break;

            case ValueMetaInterface.TYPE_NUMBER:
            case ValueMetaInterface.TYPE_INTEGER:
            case ValueMetaInterface.TYPE_BIGNUMBER:
                if (fieldname.equalsIgnoreCase(tk) || // Technical key
                        fieldname.equalsIgnoreCase(pk) // Primary key
                ) {
                    if (use_autoinc) {
                        retval += "BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY";
                    } else {
                        retval += "BIGINT NOT NULL PRIMARY KEY";
                    }
                } else {
                    // Integer values...
                    if (precision == 0) {
                        if (length > 9) {
                            if (length < 19) {
                                // can hold signed values between -9223372036854775808 and 9223372036854775807
                                // 18 significant digits
                                retval += "BIGINT";
                            } else {
                                retval += "DECIMAL(" + length + ")";
                            }
                        } else {
                            retval += "INT";
                        }
                    } else {
                        // Floating point values...
                        if (length > 15) {
                            retval += "DECIMAL(" + length;
                            if (precision > 0) {
                                retval += ", " + precision;
                            }
                            retval += ")";
                        } else {
                            // A double-precision floating-point number is accurate to approximately 15 decimal places.
                            // http://mysql.mirrors-r-us.net/doc/refman/5.1/en/numeric-type-overview.html
                            retval += "DOUBLE";
                        }
                    }
                }
                break;
            case ValueMetaInterface.TYPE_STRING:
                if (length > 0) {
                    if (length == 1) {
                        retval += "CHAR(1)";
                    } else if (length < 256) {
                        retval += "VARCHAR(" + length + ")";
                    } else if (length < 65536) {
                        retval += "TEXT";
                    } else if (length < 16777216) {
                        retval += "MEDIUMTEXT";
                    } else {
                        retval += "LONGTEXT";
                    }
                } else {
                    retval += "TINYTEXT";
                }
                break;
            case ValueMetaInterface.TYPE_BINARY:
                retval += "LONGBLOB";
                break;
            default:
                retval += " UNKNOWN";
                break;
        }

        if (add_cr) {
            retval += Const.CR;
        }

        return retval;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pentaho.di.core.database.DatabaseInterface#getReservedWords()
     */
    @Override
    public String[] getReservedWords() {
        return new String[]{
                "ACCESSIBLE", "ADD", "ALL",
                "ALTER", "ANALYZE", "AND",
                "AS", "ASC", "ASENSITIVE",
                "BEFORE", "BETWEEN", "BIGINT",
                "BINARY", "BIT_AND", "BIT_OR",
                "BIT_XOR", "BLOB", "BOTH",
                "BY",
                "CALL", "CASCADE", "CASE",
                "CAST", "CHANGE", "CHAR",
                "CHARACTER", "CHECK", "CLUSTER",
                "COLLATE", "COLUMN", "COMPRESS",
                "CONDITION", "CONNECT", "CONSTRAINT",
                "CONTINUE", "CONVERT", "COUNT",
                "CREATE", "CROSS", "CURDATE",
                "CURDATETIME", "CURRENT_DATE", "CURRENT_DATETIME",
                "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER",
                "CURSOR", "CURTIME",
                "DATABASE", "DATABASES", "DATE_ADD",
                "DATE_SUB", "DAY_HOUR", "DAY_MICROSECOND",
                "DAY_MINUTE", "DAY_SECOND", "DEC",
                "DECIMAL", "DECLARE", "DEFAULT",
                "DELAYED", "DELETE", "DENSE_RANK",
                "DESC", "DESCRIBE", "DETERMINISTIC",
                "DISTINCT", "DISTINCTROW", "DISTRIBUTE",
                "DISTRIBUTED", "DIV", "DOUBLE",
                "DROP", "DUAL",
                "EACH", "ELSE", "ELSEIF",
                "ENCLOSED", "ESCAPED", "EXISTS",
                "EXIT", "EXPLAIN", "EXTRACT",
                "EXCEPT",
                "FALSE", "FETCH", "FLOAT",
                "FLOAT4", "FLOAT8", "FOR",
                "FORCE", "FOREIGN", "FROM",
                "FULL", "FULLTEXT",
                "GCEXPORT", "GCIMPORT", "GCLOCAL",
                "GCLUSTER", "GCLUSTER_LOCAL", "GET",
                "GRANT", "GROUP", "GROUPED",
                "GROUP_CONCAT",
                "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND",
                "HOUR_MINUTE", "HOUR_SECOND",
                "IF", "IGNORE", "IN",
                "INDEX", "INFILE", "INITNODEDATAMAP",
                "INNER", "INOUT", "INPATH",
                "INSENSITIVE", "INSERT", "INT",
                "INT1", "INT2", "INT3",
                "INT4", "INT8", "INTEGER",
                "INTERSECT", "INTERVAL", "INTO",
                "IS", "ITERATE",
                "JOIN",
                "KEY", "KEYS", "KILL",
                "LAG", "LEAD", "LEADING",
                "LEAVE", "LEFT", "LEVEL",
                "LIKE", "LIMIT", "LIMIT_STORAGE_SIZE",
                "LINEAR", "LINES", "LINK",
                "LOAD", "LOCALTIME", "LOCALTIMESTAMP",
                "LOCK", "LONG", "LONGBLOB",
                "LONGTEXT", "LOOP", "LOW_PRIORITY",
                "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAX",
                "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT",
                "MERGE", "MID", "MIDDLEINT",
                "MIN", "MINUS", "MINUTE_MICROSECOND",
                "MINUTE_SECOND", "MOD", "MODIFIES",
                "NATURAL", "NOCOPIES", "NOCYCLE",
                "NOT", "NOW", "NO_WRITE_TO_BINLOG",
                "NULL", "NUMERIC",
                "ON", "OPTIMIZE", "OPTION", "OPTIONALLY",
                "OR", "ORDER", "ORDERED", "OUT",
                "OUTER", "OUTFILE", "OVER",
                "POSITION", "PRECEDING", "PRIMARY",
                "PRIOR", "PROCEDURE", "PURGE",
                "RANGE", "RANK", "READ",
                "READS", "READ_WRITE", "REAL",
                "REFERENCES", "REFRESH", "REFRESHNODEDATAMAP",
                "REGEXP", "RELEASE", "RENAME",
                "REPEAT", "REPLACE", "REQUIRE",
                "RESTRICT", "RETURN", "REVERT",
                "REVOKE", "RIGHT", "RLIKE",
                "SCHEMA", "SCHEMAS", "SCN_NUMBER",
                "SECOND_MICROSECOND", "SELECT", "SELF",
                "SENSITIVE", "SEPARATOR", "SET",
                "SHOW", "SMALLINT", "SORT",
                "SPATIAL", "SPECIFIC", "SQL",
                "SQLEXCEPTION", "SQLSTATE", "SQLWARNING",
                "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT",
                "SSL", "START", "STARTING",
                "STD", "STDDEV", "STDDEV_POP",
                "STDDEV_SAMP", "STRAIGHT_JOIN",
                "TABLE", "TARGET", "TERMINATED",
                "THEN", "TINYBLOB", "TINYINT",
                "TINYTEXT", "TO", "TRAILING",
                "TRIGGER", "TRIM", "TRUE",
                "UNDO", "UNION", "UNIQUE",
                "UNLOCK", "UNSIGNED", "UPDATE",
                "USAGE", "USE", "USING",
                "UTC_DATE", "UTC_DATETIME", "UTC_TIME",
                "UTC_TIMESTAMP",
                "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER",
                "VARCHARACTER", "VARYING", "VARYING", "VAR_SAMP",
                "WHEN", "WHERE", "WHILE",
                "WITH", "WRITE",
                "XOR",
                "YEAR_MONTH",
                "ZEROFILL"};
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pentaho.di.core.database.DatabaseInterface#getStartQuote()
     */
    @Override
    public String getStartQuote() {
        return "`";
    }

    /**
     * Simply add an underscore in the case of MySQL!
     *
     * @see DatabaseInterface#getEndQuote()
     */
    @Override
    public String getEndQuote() {
        return "`";
    }

    /**
     * @param tableNames The names of the tables to lock
     * @return The SQL command to lock database tables for write purposes.
     */
    @Override
    public String getSQLLockTables(String[] tableNames) {
        String sql = "LOCK TABLES ";
        for (int i = 0; i < tableNames.length; i++) {
            if (i > 0) {
                sql += ", ";
            }
            sql += tableNames[i] + " WRITE";
        }
        sql += ";" + Const.CR;

        return sql;
    }

    /**
     * @param tableName The name of the table to unlock
     * @return The SQL command to unlock a database table.
     */
    @Override
    public String getSQLUnlockTables(String[] tableName) {
        return "UNLOCK TABLES"; // This unlocks all tables
    }

    @Override
    public boolean needsToLockAllTables() {
        return true;
    }

    /**
     * @return extra help text on the supported options on the selected database platform.
     */
    @Override
    public String getExtraOptionsHelpText() {
        return "http://www.gbase.cn";
    }

    /**
     * 此数据库连接所需的库(在lib中)
     *
     * @return java.lang.String[]
     * @author YuWenjie
     * @date 2020/7/1 14:33
     **/
    @Override
    public String[] getUsedLibraries() {
        return new String[]{"gbase-connector-java-8.3.81.53-build54.1-bin.jar"};
    }

    /**
     * @param tableName
     * @return true if the specified table is a system table
     */
    @Override
    public boolean isSystemTable(String tableName) {
        if (tableName.startsWith("sys")) {
            return true;
        }
        if (tableName.equals("dtproperties")) {
            return true;
        }
        return false;
    }

    /**
     * Get the SQL to insert a new empty unknown record in a dimension.
     *
     * @param schemaTable  the schema-table name to insert into
     * @param keyField     The key field
     * @param versionField the version field
     * @return the SQL to insert the unknown record into the SCD.
     */
    @Override
    public String getSQLInsertAutoIncUnknownDimensionRow(String schemaTable, String keyField,
                                                         String versionField) {
        return "insert into " + schemaTable + "(" + keyField + ", " + versionField + ") values (1, 1)";
    }

    /**
     * 根据数据库方言在字符串周围添加引号，并转义特殊字符，如CR、LF和引用字符本身。
     *
     * @param string
     * @return A string that is properly quoted for use in a SQL statement (insert, update, delete, etc)
     */
    @Override
    public String quoteSQLString(String string) {
        string = string.replaceAll("'", "\\\\'");
        string = string.replaceAll("\\n", "\\\\n");
        string = string.replaceAll("\\r", "\\\\r");
        return "'" + string + "'";
    }

    /**
     * Returns a false as Oracle does not allow for the releasing of savepoints.
     */
    @Override
    public boolean releaseSavepoint() {
        return false;
    }

    /**
     * 如果数据库支持批量更新时的错误处理(故障恢复)
     *
     * @return boolean
     * @author YuWenjie
     * @date 2020/7/1 14:41
     **/
    @Override
    public boolean supportsErrorHandlingOnBatchUpdates() {
        return true;
    }

    /**
     * 如果这个数据库需要一个事务来执行一个查询(自动提交关闭)
     *
     * @return boolean
     * @author YuWenjie
     * @date 2020/7/1 14:40
     **/
    @Override
    public boolean isRequiringTransactionsOnQueries() {
        return false;
    }

    /**
     * Kettle可以在这种类型的数据库上创建存储库
     *
     * @return true if Kettle can create a repository on this type of database.
     */
    @Override
    public boolean supportsRepository() {
        return false;
    }

    /**
      * 设置数据库默认选项
      *
      * @return void
      * @author YuWenjie
      * @date 2020/7/1 14:42
      **/
    @Override
    public void addDefaultOptions() {
        addExtraOption(getPluginId(), "defaultFetchSize", "500");
        addExtraOption(getPluginId(), "useCursorFetch", "true");
    }

    @Override
    public int getMaxVARCHARLength() {
        return VARCHAR_LIMIT;
    }

    @Override
    public int getMaxTextFieldLength() {
        return Integer.MAX_VALUE;
    }
}
