/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
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

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.row.value.*;

import static org.junit.Assert.*;

public class XuGuDatabaseMetaTest {
    XuGuDatabaseMeta nativeMeta;

    @Before
    public void setupBefore() {
        nativeMeta = new XuGuDatabaseMeta();
        nativeMeta.setAccessType(DatabaseMeta.TYPE_ACCESS_NATIVE);

    }

    @Test
    public void testSettings() throws Exception {
        assertArrayEquals(new int[]{DatabaseMeta.TYPE_ACCESS_NATIVE},
                nativeMeta.getAccessTypeList());
        assertEquals(5138, nativeMeta.getDefaultDatabasePort());
        assertTrue(nativeMeta.supportsAutoInc());
        assertEquals(1, nativeMeta.getNotFoundTK(true));
        assertEquals(0, nativeMeta.getNotFoundTK(false));
        assertEquals("com.xugu.cloudjdbc.Driver", nativeMeta.getDriverClass());
        assertEquals("jdbc:xugu://FOO:BAR/WIBBLE", nativeMeta.getURL("FOO", "BAR", "WIBBLE"));
        assertEquals("jdbc:xugu://FOO/WIBBLE", nativeMeta.getURL("FOO", "", "WIBBLE"));
        assertEquals("&", nativeMeta.getExtraOptionSeparator());
        assertEquals("?", nativeMeta.getExtraOptionIndicator());
        assertTrue(nativeMeta.supportsTransactions());
        assertTrue(nativeMeta.supportsBitmapIndex());
        assertTrue(nativeMeta.supportsViews());
        assertTrue(nativeMeta.supportsSynonyms());
        assertArrayEquals(new String[]{"ABORT", "ABOVE", "ABSOLUTE", "ACCESS", "ACCOUNT", "ACTION", "ADD", "AFTER",
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
                "ZONE"}, nativeMeta.getReservedWords());

        assertEquals("\"", nativeMeta.getStartQuote());
        assertEquals("\"", nativeMeta.getEndQuote());
        assertFalse(nativeMeta.needsToLockAllTables());
        assertEquals("http://www.xugucn.com/Single_index_id_28.shtml", nativeMeta.getExtraOptionsHelpText());
        assertArrayEquals(new String[]{"cloudjdbc.jar"}, nativeMeta.getUsedLibraries()); // this is way wrong
        assertFalse(nativeMeta.releaseSavepoint());
        assertFalse(nativeMeta.supportsErrorHandlingOnBatchUpdates());
        assertTrue(nativeMeta.isRequiringTransactionsOnQueries());
        assertTrue(nativeMeta.supportsRepository());
    }

    @Test
    public void testSQLStatements() {
        assertEquals(" LIMIT 15", nativeMeta.getLimitClause(15));
        assertEquals("SELECT * FROM FOO LIMIT 0", nativeMeta.getSQLQueryFields("FOO"));
        assertEquals("SELECT * FROM FOO LIMIT 0", nativeMeta.getSQLTableExists("FOO"));
        assertEquals("SELECT FOO FROM BAR LIMIT 0", nativeMeta.getSQLQueryColumnFields("FOO", "BAR"));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DATETIME",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaDate("BAR"), "", false, "", false));
        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DATETIME",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaTimestamp("BAR"), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR CHAR(1)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBoolean("BAR"), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 10, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 10, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaInteger("BAR", 10, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR SMALLINT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 0, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR INTEGER",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 5, 0), "", false, "", false));


        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DOUBLE(10,3)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 10, 3), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DOUBLE(10,3)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 10, 3), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DOUBLE(21,4)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 21, 4), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR CLOB",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaString("BAR", nativeMeta.getMaxVARCHARLength() + 2, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR VARCHAR(15)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaString("BAR", 15, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR FLOAT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 10, -7), "", false, "", false)); // Bug here - invalid SQL

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DOUBLE(22,7)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 22, 7), "", false, "", false));
        assertEquals("ALTER TABLE FOO ADD COLUMN BAR FLOAT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", -10, 7), "", false, "", false));
        assertEquals("ALTER TABLE FOO ADD COLUMN BAR DOUBLE(5,7)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 5, 7), "", false, "", false));
        assertEquals("ALTER TABLE FOO ADD COLUMN BAR  UNKNOWN",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaInternetAddress("BAR"), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaInteger("BAR"), "BAR", true, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaNumber("BAR", 26, 8), "BAR", true, "", false));

        String lineSep = System.getProperty("line.separator");
        assertEquals("ALTER TABLE FOO DROP COLUMN BAR " + lineSep,
                nativeMeta.getDropColumnStatement("FOO", new ValueMetaString("BAR", 15, 0), "", false, "", true));

        assertEquals("ALTER TABLE FOO ALTER COLUMN BAR VARCHAR(15)",
                nativeMeta.getModifyColumnStatement("FOO", new ValueMetaString("BAR", 15, 0), "", false, "", true));

        assertEquals("ALTER TABLE FOO ALTER COLUMN BAR CHAR",
                nativeMeta.getModifyColumnStatement("FOO", new ValueMetaString("BAR"), "", false, "", true));

//        // do a boolean check
//        nativeMeta.setSupportsBooleanDataType(true);
//        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BOOLEAN",
//                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBoolean("BAR"), "", false, "", false));
//        nativeMeta.setSupportsBooleanDataType(false);

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT NOT NULL PRIMARY KEY",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaInteger("BAR"), "BAR", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BIGINT",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 10, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR NUMERIC(22,0)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBigNumber("BAR", 22, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR CHAR(1)",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaString("BAR", 1, 0), "", false, "", false));

        assertEquals("ALTER TABLE FOO ADD COLUMN BAR CLOB",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaString("BAR", 16777250, 0), "", false, "", false));
        assertEquals("ALTER TABLE FOO ADD COLUMN BAR BLOB",
                nativeMeta.getAddColumnStatement("FOO", new ValueMetaBinary("BAR", 16777250, 0), "", false, "", false));

        assertEquals("LOCK TABLES FOO WRITE, BAR WRITE;" + lineSep,
                nativeMeta.getSQLLockTables(new String[]{"FOO", "BAR"}));

        assertEquals(null, nativeMeta.getSQLUnlockTables(new String[]{}));

        assertEquals("insert into FOO(FOOKEY, FOOVERSION) values (1, 1)", nativeMeta.getSQLInsertAutoIncUnknownDimensionRow("FOO", "FOOKEY", "FOOVERSION"));
    }

}
