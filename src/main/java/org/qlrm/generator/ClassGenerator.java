package org.qlrm.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;

public class ClassGenerator {

    @Deprecated
    public void generateFromTables(final String path,
                                   final String pkg,
                                   final String suffix,
                                   final boolean publicFields,
                                   final Connection con,
                                   final String... tables) throws SQLException, FileNotFoundException {
        generateFromTables(path, pkg, suffix, publicFields, null, con, tables);
    }

    public void generateFromTables(final String path,
                                   final String pkg,
                                   final String suffix,
                                   final boolean publicFields,
                                   final String schema,
                                   final Connection con,
                                   final String... tables) throws SQLException, FileNotFoundException {
        DatabaseMetaData metadata = con.getMetaData();
        for (String table : tables) {
            String className = generateClassName(table, suffix);
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, pkg, className)));

            createClassHeader(outputStream, pkg, className);

            ResultSet colResults = metadata.getColumns(null, schema, table, null);
            createClassBody(colResults, outputStream, className, publicFields);

            outputStream.close();
            colResults.close();
        }
    }

    public void generateFromResultSet(String path, String pkg, String className, boolean publicFields, ResultSet resultSet)
            throws SQLException, FileNotFoundException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        try (PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, pkg, className)))) {
            createClassHeader(outputStream, pkg, className);

            createClassBody(metaData, outputStream, className, publicFields);
        }
    }

    private void createClassHeader(PrintWriter outputStream, String pkg, String className) {
        if (pkg != null) {
            outputStream.println("package " + pkg + ";\n");
        }

        outputStream.println("import java.io.Serializable;");
        outputStream.println("import java.sql.Date;");
        outputStream.println("import java.sql.Time;");
        outputStream.println("import java.sql.Timestamp;");
        outputStream.println("import java.math.BigDecimal;");
        outputStream.println("import java.math.BigInteger;");
        outputStream.println("import java.sql.Blob;");
        outputStream.println("\n");
        outputStream.println("public class " + className + " implements Serializable {\n");

        outputStream.println("\n");
        outputStream.println("  private static final long serialVersionUID = 1L;");
    }

    private String createFileName(String path, String pkg, String className) {
        if (pkg == null) {
            return path + "/" + className + ".java";
        } else {
            return path + "/" + pkg.replaceAll("\\.", "/") + "/" + className + ".java";
        }
    }

    private void createClassBody(ResultSet colResults, PrintWriter outputStream,
                                 String className, boolean publicFields)
            throws SQLException {
        StringBuilder ctrArgs = new StringBuilder();
        StringBuilder ctrBody = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        boolean first = true;
        while (colResults.next()) {
            if (!first) {
                ctrArgs.append(", ");
            }
            String name = colResults.getString("COLUMN_NAME").toLowerCase();
            short colType = colResults.getShort("DATA_TYPE");
            generateCtrAndGetters(colType, outputStream, publicFields, name, ctrArgs, ctrBody, getters);
            first = false;
        }
        writeCtrAndGetters(outputStream, className, ctrArgs, ctrBody, getters);
    }

    private void createClassBody(ResultSetMetaData metaData, PrintWriter outputStream,
                                 String className, boolean publicFields)
            throws SQLException {
        StringBuilder ctrArgs = new StringBuilder();
        StringBuilder ctrBody = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        boolean first = true;
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (!first) {
                ctrArgs.append(", ");
            }
            String name = metaData.getColumnName(i).toLowerCase();
            int colType = metaData.getColumnType(i);
            generateCtrAndGetters(colType, outputStream, publicFields, name, ctrArgs, ctrBody, getters);
            first = false;
        }
        writeCtrAndGetters(outputStream, className, ctrArgs, ctrBody, getters);
    }

    private String sqlTypeToJavaTypeString(int dataType) {
        String typeString;
        switch (dataType) {
            case Types.TINYINT:
                typeString = "byte";
                break;
            case Types.BIGINT:
                typeString = "BigInteger";
                break;
            case Types.INTEGER:
                typeString = "Integer";
                break;
            case Types.SMALLINT:
                typeString = "Short";
                break;
            case Types.CHAR:
                typeString = "Character";
                break;
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
                typeString = "String";
                break;
            case Types.DOUBLE:
            case Types.FLOAT:
                typeString = "Double";
                break;
            case Types.REAL:
                typeString = "Float";
                break;
            case Types.NUMERIC:
            case Types.DECIMAL:
                typeString = "BigDecimal";
                break;
            case Types.DATE:
                typeString = "Date";
                break;
            case Types.BIT:
                typeString = "boolean";
                break;
            case Types.OTHER:
                typeString = "Object";
                break;
            case Types.TIMESTAMP:
                typeString = "Timestamp";
                break;
            case Types.TIME:
                typeString = "Time";
                break;
            case Types.BLOB:
                typeString = "Blob";
                break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                typeString = "byte[]";
                break;
            default:
                typeString = "Object";
                break;
        }
        return typeString;
    }

    private String generateClassName(String table, String suffix) {
        if (suffix == null) {
            suffix = "";
        }
        return table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + suffix;
    }

    private void generateCtrAndGetters(int colType, PrintWriter outputStream,
                                       boolean publicFields, String name, StringBuilder ctrArgs,
                                       StringBuilder ctrBody, StringBuilder getters) {
        String type = sqlTypeToJavaTypeString(colType);
        outputStream.println(publicFields ? "  public " : "  private " + type + " " + name + ";");
        ctrArgs.append(type).append(" ").append(name);
        ctrBody.append("    this.").append(name).append(" = ").append(name).append(";\n");
        if (!publicFields) {
            getters.append("  public ").append(type).append(" get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("() {\n");
            getters.append("    return ").append(name).append(";\n }\n");
        }
    }

    private void writeCtrAndGetters(PrintWriter outputStream, String className,
                                    StringBuilder ctrArgs, StringBuilder ctrBody, StringBuilder getters) {
        outputStream.println("\n");
        outputStream.println("  public " + className + " (" + ctrArgs.toString() + ") {\n");
        outputStream.println(ctrBody.toString());
        outputStream.println("  }\n");
        outputStream.println(getters.toString());
        outputStream.println("}");
    }
}
