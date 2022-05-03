package com.koli.openquiz.sync.service;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionService {

    public static final String SYNC_PREFERENCES = "SyncPreferences";

    private static final String LOGIN_POSTFIX = "/login";
    private static final Logger LOGGER = Logger.getLogger("SessionService");

    private Context context;
    private String sessionId;

    public SessionService(Context context) {
        this.context = context;
        this.sessionId = readSessionId();
        LOGGER.log(Level.INFO, sessionId == null ? "No session found" : "Found session: " + sessionId);
    }

    public void login(String email, String password) throws IOException {
        URL url = new URL(SyncService.URL + LOGIN_POSTFIX);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", RequestHelper.CONTENT_TYPE);
        connection.connect();

        JSONObject loginObject = createLoginObject(email, password);
        RequestHelper.writeToStream(connection.getOutputStream(), loginObject.toString());

        String cookieString = connection.getHeaderField("Set-Cookie");
        List<String> cookies = Arrays.asList(cookieString.split(";"));

        sessionId = parseSessionId(cookies);
        if (sessionId != null) {
            LOGGER.log(Level.INFO, "Saving new session");
            saveSessionId();
        }

        if (connection.getResponseCode() == 200) {
            LOGGER.log(Level.INFO, sessionId);
        } else {
            LOGGER.log(Level.INFO, String.valueOf(connection.getResponseCode()));
        }

        connection.disconnect();
    }

    private JSONObject createLoginObject(String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String parseSessionId(List<String> cookies) {
        String id = null;
        for (String cookie : cookies) {
            cookie = cookie.trim();
            if (cookie.contains(RequestHelper.SESSION_COOKIE)) {
                id = cookie.split("=")[1];
            }
        }
        return id;
    }

    public boolean hasSession() {
        return readSessionId() != null;
    }

    private void saveSessionId() {
        SharedPreferences.Editor editor = context.getSharedPreferences(SYNC_PREFERENCES, 0).edit();
        editor.putString(RequestHelper.SESSION_COOKIE, sessionId);
        editor.apply();
    }

    private String readSessionId() {
        SharedPreferences settings = context.getSharedPreferences(SYNC_PREFERENCES, 0);
        return settings.getString(RequestHelper.SESSION_COOKIE, null);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
