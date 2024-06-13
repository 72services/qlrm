package org.qlrm.generator;

import java.sql.Types;

public class AbstractGenerator {

    protected String sqlTypeToJavaTypeString(int dataType) {
        return switch (dataType) {
            case Types.TINYINT -> "byte";
            case Types.BIGINT -> "BigInteger";
            case Types.INTEGER -> "Integer";
            case Types.SMALLINT -> "Short";
            case Types.CHAR -> "Character";
            case Types.VARCHAR, Types.NVARCHAR, Types.LONGVARCHAR -> "String";
            case Types.DOUBLE, Types.FLOAT -> "Double";
            case Types.REAL -> "Float";
            case Types.NUMERIC, Types.DECIMAL -> "BigDecimal";
            case Types.DATE -> "Date";
            case Types.BIT -> "boolean";
            case Types.TIMESTAMP -> "Timestamp";
            case Types.TIME -> "Time";
            case Types.BLOB -> "Blob";
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> "byte[]";
            default -> "Object";
        };
    }
}
