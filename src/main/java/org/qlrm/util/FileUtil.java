package org.qlrm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static String getFileAsString(String filename) {
        try {
            InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new IllegalArgumentException("File " + filename + " not found!");
            }

            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                for (; ; ) {
                    int rsz = reader.read(buffer, 0, buffer.length);
                    if (rsz < 0) {
                        break;
                    }
                    out.append(buffer, 0, rsz);
                }
            }
            return out.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
