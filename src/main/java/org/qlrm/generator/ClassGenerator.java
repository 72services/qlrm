package org.qlrm.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;

public class ClassGenerator {

    @Deprecated
    public void generateFromTables(final String path,
                                   final String packageName,
                                   final String suffix,
                                   final boolean publicFields,
                                   final Connection connection,
                                   final String... tables) throws SQLException, FileNotFoundException {
        generateFromTables(path, packageName, suffix, publicFields, null, connection, tables);
    }

    /**
     * Generates a Java source file from a database table
     *
     * @param path         Path
     * @param packageName  Package
     * @param suffix       Suffix
     * @param publicFields if fields are public
     * @param schema       Schema
     * @param connection   Connection
     * @param tables       Tables
     */
    public void generateFromTables(final String path,
                                   final String packageName,
                                   final String suffix,
                                   final boolean publicFields,
                                   final String schema,
                                   final Connection connection,
                                   final String... tables) throws SQLException, FileNotFoundException {
        DatabaseMetaData metadata = connection.getMetaData();
        for (String table : tables) {
            String className = generateClassName(table, suffix);
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, packageName, className)));

            createClassHeader(outputStream, packageName, className);

            ResultSet colResults = metadata.getColumns(null, schema, table, null);
            createClassBody(colResults, outputStream, className, publicFields);

            outputStream.close();
            colResults.close();
        }
    }

    /**
     * Generates a Java source file from a result set
     *
     * @param path         Path
     * @param packageName  Package
     * @param className    Class name
     * @param publicFields if fields are public
     * @param resultSet    {@link ResultSet}
     */
    public void generateFromResultSet(String path, String packageName, String className, boolean publicFields, ResultSet resultSet)
            throws SQLException, FileNotFoundException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        try (PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, packageName, className)))) {
            createClassHeader(outputStream, packageName, className);

            createClassBody(metaData, outputStream, className, publicFields);
        }
    }

    private void createClassHeader(PrintWriter outputStream, String packageName, String className) {
        if (packageName != null) {
            outputStream.println("package " + packageName + ";\n");
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

    private void createClassBody(ResultSet resultSet, PrintWriter outputStream, String className, boolean publicFields)
            throws SQLException {
        StringBuilder ctrArgs = new StringBuilder();
        StringBuilder ctrBody = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        boolean first = true;
        while (resultSet.next()) {
            if (!first) {
                ctrArgs.append(", ");
            }
            String name = resultSet.getString("COLUMN_NAME").toLowerCase();
            short colType = resultSet.getShort("DATA_TYPE");
            generateCtrAndGetters(colType, outputStream, publicFields, name, ctrArgs, ctrBody, getters);
            first = false;
        }
        writeCtrAndGetters(outputStream, className, ctrArgs, ctrBody, getters);
    }

    private void createClassBody(ResultSetMetaData metaData, PrintWriter outputStream, String className, boolean publicFields)
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

    private String generateClassName(String table, String suffix) {
        if (suffix == null) {
            suffix = "";
        }
        return table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + suffix;
    }

    private void generateCtrAndGetters(int colType, PrintWriter outputStream, boolean publicFields,
                                       String name, StringBuilder constructorArguments, StringBuilder constructorBody,
                                       StringBuilder getters) {
        String type = sqlTypeToJavaTypeString(colType);
        outputStream.println(publicFields ? "  public " : "  private " + type + " " + name + ";");
        constructorArguments.append(type).append(" ").append(name);
        constructorBody.append("    this.").append(name).append(" = ").append(name).append(";\n");
        if (!publicFields) {
            getters.append("  public ").append(type).append(" get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("() {\n");
            getters.append("    return ").append(name).append(";\n }\n");
        }
    }

    private void writeCtrAndGetters(PrintWriter outputStream, String className, StringBuilder constructorArguments,
                                    StringBuilder constructorBody, StringBuilder getters) {
        outputStream.println("\n");
        outputStream.println("  public " + className + " (" + constructorArguments.toString() + ") {\n");
        outputStream.println(constructorBody.toString());
        outputStream.println("  }\n");
        outputStream.println(getters.toString());
        outputStream.println("}");
    }
}
