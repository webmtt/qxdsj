/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.core.database;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.util.Utils;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contains XuGu specific information through static final members
 */

public class XuGuDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

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
            return 5138;
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
     * @date 2020/3/13 21:42
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
     * @date 2020/3/13 21:41
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
    /**
     * 获取未找到的技术密钥
     *
     * @param use_autoinc 是否要使用自动递增字段
     * @return int  在缓慢变化的维度中用作未找到行的最低技术键
     * @author YuWenjie
     * @date 2020/3/16 10:37
     **/
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
     * @param
     * @return java.lang.String
     **/
    @Override
    public String getDriverClass() {
        if (getAccessType() == DatabaseMeta.TYPE_ACCESS_ODBC) {
            return "com.xugu.cloudjdbc.Driver";
        } else {
            return "com.xugu.cloudjdbc.Driver";
        }
    }

    /**
     * 用于连接到数据库的URL
     *
     * @param hostname
     * @param port
     * @param databaseName
     * @return java.lang.String
     **/
    @Override
    public String getURL(String hostname, String port, String databaseName) {
        if (getAccessType() == DatabaseMeta.TYPE_ACCESS_ODBC) {
            return "jdbc:odbc:" + databaseName;
        } else {
            if (Utils.isEmpty(port)) {
                return "jdbc:xugu://" + hostname + "/" + databaseName;
            } else {
                return "jdbc:xugu://" + hostname + ":" + port + "/" + databaseName;
            }
        }
    }

    /**
     * @return The extra option separator in database URL for this platform (usually this is semicolon ; )
     * 此平台的数据库URL中的额外选项分隔符
     */
    @Override
    public String getExtraOptionSeparator() {
        return "&";
    }

    /**
     * @return This indicator separates the normal URL from the options
     * url参数分隔
     */
    @Override
    public String getExtraOptionIndicator() {
        return "?";
    }

    /**
     * @return true if the database supports transactions.
     * 是否支持事物
     */
    @Override
    public boolean supportsTransactions() {
        return true;
    }

    /**
     * @return true if the database supports bitmap indexes
     */
    /*@Override
    public boolean supportsBitmapIndex() {
        return false;
    }*/

    /**
     * 支持视图
     *
     * @return true if the database supports views
     */
    @Override
    public boolean supportsViews() {
        return true;
    }

    /**
     * XuGu 的同义词,暂时只支持同一个库中实现同义词
     *
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
        return "ALTER TABLE " + tablename + " ADD COLUMN " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
    }

    /**
     * 生成从指定表中删除列的SQL语句
     *
     * @param tablename
     * @param v
     * @param tk
     * @param use_autoinc
     * @param pk
     * @param semicolon
     * @return java.lang.String
     **/
    @Override
    public String getDropColumnStatement(String tablename, ValueMetaInterface v, String tk, boolean use_autoinc,
                                         String pk, boolean semicolon) {
        return "ALTER TABLE " + tablename + " DROP COLUMN " + v.getName() + " " + Const.CR;
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
        return "ALTER TABLE " + tablename + " ALTER COLUMN " + getFieldDefinition(v, tk, pk, use_autoinc, true, false);
    }

    /**
     * 将一个值 描述为 数据库中的一个字段
     *
     * @param v             定义为值的列
     * @param tk
     * @param pk            主键
     * @param use_autoinc   是否使用自动递增
     * @param add_fieldname 是否将字段名添加到定义中
     * @param add_cr        是否在定义的末尾添加一个 \r\n
     * @return java.lang.String
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
                // true: 如果数据库支持布尔、位、逻辑
                // false: 数据类型（默认），映射到字符串。
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
                        // 自增
                        retval += "BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY";
                    } else {
                        retval += "BIGINT NOT NULL PRIMARY KEY";
                    }
                } else {
                    // Integer values...
                    if (precision == 0) {
                        if (length > 18) {
                            retval += "NUMERIC(" + length + ",0)";
                        } else {
                            // can hold signed values between -9223372036854775808 and 9223372036854775807
                            // 18 significant digits
                            if (length > 9) {
                                retval += "BIGINT";
                            } else {
                                if (length < 5) {
                                    retval += "SMALLINT";
                                } else {
                                    retval += "INTEGER";
                                }
                            }
                        }
                    } else {
                        // Floating point values...
                        if (precision > 0 && length > 0) {
                            retval += "DOUBLE(" + length + "," + precision + ")";
//                            retval += "NUMERIC(" + length + "," + precision + ")";
                        } else {
                            retval += "FLOAT";
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
                    } else {
                        retval += "CLOB";
                    }
                } else {
                    retval += "CHAR";
                }
                break;
            case ValueMetaInterface.TYPE_BINARY:
//                if( length > getMaxVARCHARLength() || length >= DatabaseMeta.CLOB_LENGTH ){
                retval += "BLOB";
//                }else {
//                    retval += "BINARY";
//                }
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

    /**
     * 关键字
     *
     * @param
     * @return java.lang.String[]
     * @author YuWenjie
     * @date 2020/3/16 13:43
     **/
    @Override
    public String[] getReservedWords() {
        return new String[]{"ABORT", "ABOVE", "ABSOLUTE", "ACCESS", "ACCOUNT", "ACTION", "ADD", "AFTER",
                "AGGREGATE", "ALL", "ALTER", "ANALYSE", "ANALYZE", "AND", "ANY", "AOVERLAPS",
                "APPEND", "ARCHIVELOG", "ARE", "ARRAY", "AS", "ASC", "AT", "AUDIT", "AUDITOR",
                "AUTHID", "AUTHORIZATION", "AUTO",
                "BACKUP", "BACKWARD", "BADFILE", "BCONTAINS", "BEFORE", "BEGIN",
                "BETWEEN", "BINARY", "BINTERSECTS", "BIT", "BLOCK", "BLOCKS", "BODY",
                "BOTH", "BOUND", "BOVERLAPS", "BREAK", "BUFFER_POOL", "BUILD", "BULK",
                "BWITHIN", "BY",
                "CACHE", "CALL", "CASCADE", "CASE", "CAST", "CATCH", "CATEGORY",
                "CHAIN", "CHAR", "CHARACTER", "CHARACTERISTICS", "CHECK",
                "CHECKPOINT", "CHUNK", "CLOSE", "CLUSTER", "COALESCE", "COLLATE",
                "COLLECT", "COLUMN", "COMMENT", "COMMIT", "COMMITTED", "COMPLETE",
                "COMPRESS", "COMPUTE", "CONNECT", "CONSTANT", "CONSTRAINT",
                "CONSTRAINTS", "CONSTRUCTOR", "CONTAINS", "CONTEXT", "CONTINUE",
                "COPY", "CORRESPONDING", "CREATE", "CREATEDB", "CREATEUSER", "CROSS",
                "CROSSES", "CUBE", "CURRENT", "CURSOR", "CYCLE",
                "DATABASE", "DATAFILE", "DATE", "DATETIME", "DAY", "DBA",
                "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DECODE", "DECRYPT",
                "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DELIMITED",
                "DELIMITERS", "DEMAND", "DESC", "DESCRIBE", "DETERMINISTIC", "DIR",
                "DISABLE", "DISASSEMBLE", "DISCORDFILE", "DISJOINT", "DISTINCT", "DO",
                "DOMAIN", "DOUBLE", "DRIVEN", "DROP",
                "EACH", "ELEMENT", "ELSE", "ELSEIF", "ELSIF", "ENABLE", "ENCODING",
                "ENCRYPT", "ENCRYPTOR", "END", "ENDCASE", "ENDFOR", "ENDIF", "ENDLOOP",
                "EQUALS", "ESCAPE", "EVERY", "EXCEPT", "EXCEPTION", "EXCEPTIONS",
                "EXCLUSIVE", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXPIRE", "EXPLAIN",
                "EXPORT", "EXTEND", "EXTERNAL", "EXTRACT",
                "FALSE", "FAST", "FETCH", "FIELD", "FIELDS", "FILTER", "FINAL", "FINALLY",
                "FIRST", "FLOAT", "FOLLOWING", "FOR", "FORALL", "FORCE", "FOREIGN",
                "FORWARD", "FOUND", "FREELIST", "FREELISTS", "FROM", "FULL", "FUNCTION",
                "GENERATED", "GET", "GLOBAL", "GOTO", "GRANT", "GREATEST", "GROUP",
                "GROUPING", "GROUPS",
                "HANDLER", "HASH", "HAVING", "HEAP", "HIDE", "HOTSPOT", "HOUR",
                "IDENTIFIED", "IDENTIFIER", "IDENTITY", "IF", "ILIKE", "IMMEDIATE",
                "IMPORT", "IN", "INCLUDE", "INCREMENT", "INDEX", "INDEXTYPE", "INDICATOR",
                "INDICES", "INHERITS", "INIT", "INITIAL", "INITIALLY", "INITRANS", "INNER",
                "INOUT", "INSENSITIVE", "INSERT", "INSTANTIABLE", "INSTEAD", "INTERSECT",
                "INTERSECTS", "INTERVAL", "INTO", "IO", "IS", "ISNULL", "ISOLATION", "ISOPEN",
                "JOB", "JOIN",
                "K", "KEEP", "KEY", "KEYSET",
                "LABEL", "LANGUAGE", "LAST", "LEADING", "LEAST", "LEAVE", "LEFT",
                "LEFTOF", "LENGTH", "LESS", "LEVEL", "LEVELS", "LEXER", "LIBRARY", "LIKE",
                "LIMIT", "LINK", "LIST", "LISTEN", "LOAD", "LOB", "LOCAL", "LOCATION",
                "LOCATOR", "LOCK", "LOGFILE", "LOGGING", "LOGIN", "LOGOUT", "LOOP",
                "LOVERLAPS",
                "M", "MATCH", "MATERIALIZED", "MAX", "MAXEXTENTS", "MAXSIZE",
                "MAXTRANS", "MAXVALUE", "MAXVALUES", "MEMBER", "MEMORY", "MERGE",
                "MINEXTENTS", "MINUS", "MINUTE", "MINVALUE", "MISSING", "MODE",
                "MODIFY", "MONTH", "MOVEMENT",
                "NAME", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NESTED", "NEW",
                "NEWLINE", "NEXT", "NO", "NOARCHIVELOG", "NOAUDIT", "NOCACHE",
                "NOCOMPRESS", "NOCREATEDB", "NOCREATEUSER", "NOCYCLE", "NODE",
                "NOFORCE", "NOFOUND", "NOLOGGING", "NONE", "NOORDER", "NOPARALLEL",
                "NOT", "NOTFOUND", "NOTHING", "NOTIFY", "NOTNULL", "NOVALIDATE",
                "NOWAIT", "NULL", "NULLIF", "NULLS", "NUMBER", "NUMERIC", "NVARCHAR",
                "NVARCHAR2", "NVL", "NVL2",
                "OBJECT", "OF", "OFF", "OFFLINE", "OFFSET", "OIDINDEX", "OIDS", "OLD", "ON",
                "ONLINE", "ONLY", "OPEN", "OPERATOR", "OPTION", "OR", "ORDER",
                "ORGANIZATION", "OTHERVALUES", "OUT", "OUTER", "OVER", "OVERLAPS",
                "OWNER",
                "PACKAGE", "PARALLEL", "PARAMETERS", "PARTIAL", "PARTITION",
                "PARTITIONS", "PASSWORD", "PCTFREE", "PCTINCREASE", "PCTUSED",
                "PCTVERSION", "PERIOD", "POLICY", "PRAGMA", "PREBUILT", "PRECEDING",
                "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIORITY",
                "PRIVILEGES", "PROCEDURAL", "PROCEDURE", "PROTECTED", "PUBLIC",
                "QUERY", "QUOTA",
                "RAISE", "RANGE", "RAW", "READ", "READS", "REBUILD", "RECOMPILE",
                "RECORD", "RECORDS", "RECYCLE", "REDUCED", "REF", "REFERENCES",
                "REFERENCING", "REFRESH", "REINDEX", "RELATIVE", "RENAME",
                "REPEATABLE", "REPLACE", "REPLICATION", "RESOURCE", "RESTART",
                "RESTORE", "RESTRICT", "RESULT", "RETURN", "RETURNING", "REVERSE",
                "REVOKE", "REWRITE", "RIGHT", "RIGHTOF", "ROLE", "ROLLBACK", "ROLLUP",
                "ROVERLAPS", "ROW", "ROWCOUNT", "ROWID", "ROWS", "ROWTYPE", "RULE",
                "RUN",
                "SAVEPOINT", "SCHEMA", "SCROLL", "SECOND", "SEGMENT", "SELECT",
                "SELF", "SEQUENCE", "SERIALIZABLE", "SESSION", "SET", "SETOF", "SETS",
                "SHARE", "SHOW", "SHUTDOWN", "SIBLINGS", "SIZE", "SLOW", "SNAPSHOT",
                "SOME", "SPATIAL", "SPLIT", "SSO", "STANDBY", "START", "STATEMENT", "STATIC",
                "STATISTICS", "STEP", "STOP", "STORAGE", "STORE", "STREAM", "SUBPARTITION",
                "SUBPARTITIONS", "SUBTYPE", "SUCCESSFUL", "SYNONYM", "SYSTEM",
                "TABLE", "TABLESPACE", "TEMP", "TEMPLATE", "TEMPORARY",
                "TERMINATED", "THAN", "THEN", "THROW", "TIME", "TIMESTAMP", "TO", "TOP",
                "TOPOVERLAPS", "TOUCHES", "TRACE", "TRAILING", "TRAN", "TRANSACTION",
                "TRIGGER", "TRUE", "TRUNCATE", "TRUSTED", "TRY", "TYPE",
                "UNBOUNDED", "UNDER", "UNDO", "UNIFORM", "UNION", "UNIQUE",
                "UNLIMITED", "UNLISTEN", "UNLOCK", "UNPROTECTED", "UNTIL",
                "UOVERLAPS", "UPDATE", "USE", "USER", "USING",
                "VACUUM", "VALID", "VALIDATE", "VALUE", "VALUES", "VARCHAR",
                "VARCHAR2", "VARRAY ", "VARYING", "VERBOSE", "VERSION", "VIEW",
                "VOCABLE",
                "WAIT", "WHEN", "WHENEVER", "WHERE", "WHILE", "WITH", "WITHIN",
                "WITHOUT", "WORK", "WRITE",
                "XML",
                "YEAR",
                "ZONE"};
    }

    /**
     * @return 获取存储过程 列表数据
     */
    @Override
    public String getSQLListOfProcedures() {
        return "select p.proc_id, p.user_id, p.proc_name, comments, p.create_time " +
                "from Dba_procedures  p, Dba_users u " +
                "where p.user_id = u.user_id " +
                "order by p.proc_name ";
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
        return null; // SQL命令解锁数据库表。如果目标上不支持锁定，则返回null
    }

    @Override
    public boolean needsToLockAllTables() {
        return false;
    }

    /**
     * @return 所选数据库平台上支持的选项上的额外帮助文本
     */
    @Override
    public String getExtraOptionsHelpText() {
        return "http://www.xugucn.com/Single_index_id_28.shtml";
    }

    /**
     * 此数据库连接所需的库(在lib中)
     *
     * @param
     * @return java.lang.String[]
     **/
    @Override
    public String[] getUsedLibraries() {
        return new String[]{"cloudjdbc.jar"};
    }

//    /**
//     * 检索模式列表
//     *
//     * @param
//     * @return java.lang.String
//     **/
//    @Override
//    public String getSQLListOfSchemas() {
//        return "select schema_name from Dba_schemas";
//    }

    /**
     * @return true if we need to supply the schema-name to getTables in order to get a correct list of items.
     * 如果我们需要向getTables提供模式名以获得正确的项列表，则为true
     */
    @Override
    public boolean useSchemaNameForTableList() {
        return true;
    }

    @Override
    public boolean supportsSchemas() {
        return true;
    }

    /**
     * Get the SQL to insert a new empty unknown record in a dimension.
     * 让SQL在维度中插入一个新的空未知记录
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
     * 在SQL语句中正确引用的字符串
     */
    @Override
    public String quoteSQLString(String string) {
        string = string.replaceAll("'", "\\\\'");
        string = string.replaceAll("\\n", "\\\\n");
        string = string.replaceAll("\\r", "\\\\r");
        return "'" + string + "'";
    }

    /**
     * 保存点（SAVEPOINT）是事务处理过程中的一个标志，与回滚命令（ROLLBACK）结合使用。
     * 其主要用途是允许用户将某一段处理进行回滚而不必回滚整个事务。
     * Returns a false as Oracle does not allow for the releasing of savepoints.
     */
    @Override
    public boolean releaseSavepoint() {
        return false;
    }

    /**
     * 如果数据库支持批量更新时的错误处理(故障恢复)
     *
     * @param
     * @return boolean
     * @author YuWenjie
     * @date 2020/3/16 14:17
     **/
    @Override
    public boolean supportsErrorHandlingOnBatchUpdates() {
        return false;
    }

    /**
     * 如果这个数据库需要一个事务来执行一个查询(自动提交关闭)
     *
     * @param
     * @return boolean
     **/
    @Override
    public boolean isRequiringTransactionsOnQueries() {
        return true;
    }

    /**
     * Kettle可以在这种类型的数据库上创建存储库
     *
     * @return true if Kettle can create a repository on this type of database.
     */
    @Override
    public boolean supportsRepository() {
        return true;
    }

    /**
     * 设置数据库默认选项
     *
     * @param
     * @return void
     **/
    /*@Override
    public void addDefaultOptions() {
        addExtraOption(getPluginId(), "defaultFetchSize", "500");
        addExtraOption(getPluginId(), "useCursorFetch", "true");
    }*/

    @Override
    public int getMaxVARCHARLength() {
        return VARCHAR_LIMIT;
    }

    @Override
    public int getMaxTextFieldLength() {
        return Integer.MAX_VALUE;
    }

    /**
     * 此方法允许数据库方言将数据库特定的数据类型转换为Kettle数据类型。
     *
     * @param rs  要使用的结果集
     * @param val 要检索的值的描述
     * @param i   我们需要检索值的索引
     * @return 与valueMeta描述对应的正确转换的Kettle数据类型。
     **/
    @Override
    public Object getValueFromResultSet(ResultSet rs, ValueMetaInterface val, int i) throws KettleDatabaseException {
        Object data = null;

        try {
            switch (val.getType()) {
                case ValueMetaInterface.TYPE_BOOLEAN:
                    data = Boolean.valueOf(rs.getBoolean(i + 1));
                    break;
                case ValueMetaInterface.TYPE_NUMBER:
                    data = new Double(rs.getDouble(i + 1));
                    break;
                case ValueMetaInterface.TYPE_BIGNUMBER:
                    data = rs.getBigDecimal(i + 1);
                    break;
                case ValueMetaInterface.TYPE_INTEGER:
                    data = Long.valueOf(rs.getLong(i + 1));
                    break;
                case ValueMetaInterface.TYPE_STRING:
                    if (val.isStorageBinaryString()) {
                        data = rs.getBytes(i + 1);
                    } else {
                        data = rs.getString(i + 1);
                    }
                    break;
                case ValueMetaInterface.TYPE_BINARY:
                    if (supportsGetBlob()) {
                        Blob blob = rs.getBlob(i + 1);
                        if (blob != null) {
                            data = blob.getBytes(1L, (int) blob.length());
                        } else {
                            data = null;
                        }
                    } else {
                        data = rs.getBytes(i + 1);
                    }
                    break;
                case ValueMetaInterface.TYPE_TIMESTAMP:
                case ValueMetaInterface.TYPE_DATE:
                    if (val.getOriginalColumnType() == java.sql.Types.TIME) {
                        // Neoview can not handle getDate / getTimestamp for a Time column
                        data = rs.getTime(i + 1);
                        break; // Time is a subclass of java.util.Date, the default date
                        // will be 1970-01-01
                    } else if (val.getPrecision() != 1 && supportsTimeStampToDateConversion()) {
                        data = rs.getTimestamp(i + 1);
                        break; // Timestamp extends java.util.Date
                    } else {
                        data = rs.getDate(i + 1);
                        break;
                    }
                default:
                    break;
            }
            if (rs.wasNull()) {
                data = null;
            }
        } catch (SQLException e) {
            throw new KettleDatabaseException("Unable to get value '"
                    + val.toStringMeta() + "' from database resultset, index " + i, e);
        }

        return data;
    }
}
