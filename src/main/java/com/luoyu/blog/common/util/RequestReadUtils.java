package com.luoyu.blog.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class RequestReadUtils {

    private static final int BUFFER_SIZE = 1024 * 8;

    public static String read(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = request.getReader();
        StringWriter writer = new StringWriter();
        write(bufferedReader, writer);
        return writer.getBuffer().toString();
    }

    public static long write(Reader reader, Writer writer) throws IOException {
        return write(reader, writer, BUFFER_SIZE);
    }

    public static long write(Reader reader, Writer writer, int bufferSize) throws IOException
    {
        int read;
        long total = 0;
        char[] buf = new char[bufferSize];
        while( ( read = reader.read(buf) ) != -1 ) {
            writer.write(buf, 0, read);
            total += read;
        }
        return total;
    }
}
