package org.qlrm.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;

public class RecordGenerator extends AbstractGenerator {

    /**
     * Generates a Java source file from a database table
     *
     * @param path        Path
     * @param packageName Package
     * @param suffix      Suffix
     * @param schema      Schema
     * @param connection  Connection
     * @param tables      Tables
     */
    public void generateFromTables(final String path,
                                   final String packageName,
                                   final String suffix,
                                   final String schema,
                                   final Connection connection,
                                   final String... tables) throws SQLException, FileNotFoundException {
        DatabaseMetaData metadata = connection.getMetaData();
        for (String table : tables) {
            String className = generateClassName(table, suffix);
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, packageName, className)));

            createClassHeader(outputStream, packageName, className);

            ResultSet colResults = metadata.getColumns(null, schema, table, null);
            createClassBody(colResults, outputStream);

            outputStream.close();
            colResults.close();
        }
    }

    /**
     * Generates a Java source file from a result set
     *
     * @param path        Path
     * @param packageName Package
     * @param className   Class name
     * @param resultSet   {@link ResultSet}
     */
    public void generateFromResultSet(String path, String packageName, String className, ResultSet resultSet)
            throws SQLException, FileNotFoundException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        try (PrintWriter outputStream = new PrintWriter(new FileOutputStream(createFileName(path, packageName, className)))) {
            createClassHeader(outputStream, packageName, className);

            createClassBody(metaData, outputStream);
        }
    }

    private void createClassHeader(PrintWriter outputStream, String packageName, String className) {
        if (packageName != null) {
            outputStream.println("package " + packageName + ";\n");
        }

        outputStream.println("import java.sql.Date;");
        outputStream.println("import java.sql.Time;");
        outputStream.println("import java.sql.Timestamp;");
        outputStream.println("import java.math.BigDecimal;");
        outputStream.println("import java.math.BigInteger;");
        outputStream.println("import java.sql.Blob;");
        outputStream.println("");
        outputStream.print("public record " + className + "(");
    }

    private String createFileName(String path, String pkg, String className) {
        if (pkg == null) {
            return path + "/" + className + ".java";
        } else {
            return path + "/" + pkg.replaceAll("\\.", "/") + "/" + className + ".java";
        }
    }

    private void createClassBody(ResultSet resultSet, PrintWriter printWriter)
            throws SQLException {
        StringBuilder ctrArgs = new StringBuilder();
        boolean first = true;
        while (resultSet.next()) {
            if (!first) {
                ctrArgs.append(", ");
            }
            String name = resultSet.getString("COLUMN_NAME").toLowerCase();
            short colType = resultSet.getShort("DATA_TYPE");
            generateCtrAndGetters(colType, name, ctrArgs);
            first = false;
        }
        writeCtrAndGetters(printWriter, ctrArgs);
    }

    private void createClassBody(ResultSetMetaData metaData, PrintWriter outputStream)
            throws SQLException {
        StringBuilder ctrArgs = new StringBuilder();
        boolean first = true;
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (!first) {
                ctrArgs.append(", ");
            }
            String name = metaData.getColumnName(i).toLowerCase();
            int colType = metaData.getColumnType(i);
            generateCtrAndGetters(colType, name, ctrArgs);
            first = false;
        }
        writeCtrAndGetters(outputStream, ctrArgs);
    }

    private String generateClassName(String table, String suffix) {
        if (suffix == null) {
            suffix = "";
        }
        return table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + suffix;
    }

    private void generateCtrAndGetters(int colType, String name, StringBuilder constructorArguments) {
        String type = sqlTypeToJavaTypeString(colType);
        constructorArguments.append(type).append(" ").append(name);
    }

    private void writeCtrAndGetters(PrintWriter outputStream, StringBuilder constructorArguments) {
        outputStream.println(constructorArguments.toString() + ") {}");
    }
}
