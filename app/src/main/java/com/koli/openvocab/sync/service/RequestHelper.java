package com.koli.openvocab.sync.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RequestHelper {

    public static final String SESSION_COOKIE = "JSESSIONID";
    public static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    public static void writeToStream(OutputStream stream, String content) {
        try {
            OutputStream out = new BufferedOutputStream(stream);
            out.write(content.getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
