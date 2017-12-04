
package com.google.refine.extension.database;

public class DatabaseUtils {

    public static DatabaseColumnType getDbColumnType(int dbType) {

        switch (dbType) {
        case java.sql.Types.BIGINT:
            return DatabaseColumnType.NUMBER;
        case java.sql.Types.FLOAT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.REAL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DOUBLE:
            return DatabaseColumnType.STRING;
        case java.sql.Types.NUMERIC:
            return DatabaseColumnType.NUMBER;
        case java.sql.Types.DECIMAL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.CHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.VARCHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.LONGVARCHAR:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DATE:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.TIME:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.TIMESTAMP:
            return DatabaseColumnType.DATETIME;
        case java.sql.Types.BINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.VARBINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.LONGVARBINARY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.NULL:
            return DatabaseColumnType.STRING;
        case java.sql.Types.OTHER:
            return DatabaseColumnType.STRING;
        case java.sql.Types.JAVA_OBJECT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.DISTINCT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.STRUCT:
            return DatabaseColumnType.STRING;
        case java.sql.Types.ARRAY:
            return DatabaseColumnType.STRING;
        case java.sql.Types.BLOB:
            return DatabaseColumnType.STRING;
        case java.sql.Types.CLOB:
            return DatabaseColumnType.STRING;
        case java.sql.Types.REF:
            return DatabaseColumnType.STRING;
        default:
            return DatabaseColumnType.STRING;
        }

    }

}
