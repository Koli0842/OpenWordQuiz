package com.koli.openquiz.sync.service;

import android.content.Context;

import com.koli.openquiz.model.Dictionary;
import com.koli.openquiz.persistence.DictionaryStore;
import com.koli.openquiz.sync.domain.DictionaryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncService {

    public static final String URL = "http://kolihome.ddns.net:8091/openquiz";

    private static final String LIST_DICTIONARIES_POSTFIX = "/list-dictionary";
    private static final String GET_DICTIONARY_POSTFIX = "/dictionary";
    private static final String GET_DICTIONARY_PARAM = "?dictionaryId=";
    private static final String ADD_DICTIONARY_POSTFIX = "/add-dictionary";

    private static final Logger LOGGER = Logger.getLogger("SyncService");

    private SessionService sessionService;
    private DictionaryStore dictionaryStore;

    public SyncService(Context context) {
        this.sessionService = new SessionService(context);
        this.dictionaryStore = new DictionaryStore(context);
    }

    public void sync() {
        try {
            List<DictionaryInfo> dictionaryInfos = findDictionaries();
            for(String dictionaryName : dictionaryStore.list()) {
                Dictionary dictionary = null /*íDictionary.createDictionary(new JSONObject(dictionaryStore.read(dictionaryName)))*/;
                boolean found = dictionaryInfos.stream().anyMatch((d) -> dictionary.getName().equals(d.getName()));
                if(!found) {
                    LOGGER.log(Level.INFO, "No remote dictionary found with name: " + dictionary.getName());
                    uploadDictionary(dictionary);
                }
            }
            for(DictionaryInfo dictionaryInfo : dictionaryInfos) {
                if(!dictionaryStore.contains(dictionaryInfo.getName())) {
                    LOGGER.log(Level.INFO, "No local dictionary found with name: " + dictionaryInfo.getName());
                    saveDictionary(dictionaryInfo.getId());
                } else {
                    LOGGER.log(Level.INFO, "Local dictionary found with name: " + dictionaryInfo.getName());
                    String raw = dictionaryStore.read(dictionaryInfo.getName());
                    Dictionary localDictionary = null/* = Dictionary.createDictionary(new JSONObject(raw))*/;
                    if(localDictionary.getVersion() < dictionaryInfo.getVersion())  {
                        saveDictionary(dictionaryInfo.getId());
                    } else if(localDictionary.getVersion() > dictionaryInfo.getVersion()) {
                        uploadDictionary(localDictionary);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createJson(String content) {
        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            return null;
        }
    }

    private void saveDictionary(int id) throws IOException, JSONException {
        storeDictionary(downloadDictionary(id));
    }

    private void storeDictionary(Dictionary dictionary) {
        //dictionaryStore.store(dictionary.getName(), dictionary.toJson().toString());
    }

    public List<DictionaryInfo> findDictionaries() throws IOException, JSONException {
        LOGGER.log(Level.INFO, "Requested Dictionary list");
        List<DictionaryInfo> response = null;

        URL url = new URL(URL + LIST_DICTIONARIES_POSTFIX);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", RequestHelper.SESSION_COOKIE + "=" + sessionService.getSessionId());
        connection.connect();

        if (connection.getResponseCode() == 200) {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String responseString = RequestHelper.convertStreamToString(in);
            LOGGER.log(Level.INFO, responseString);
            response = downloadDictionaryInfo(new JSONArray(responseString));
        } else {
            LOGGER.log(Level.SEVERE, String.valueOf(connection.getResponseCode()));
        }

        connection.disconnect();
        return response;
    }

    public List<DictionaryInfo> downloadDictionaryInfo(JSONArray jsonArray) throws JSONException {
        List<DictionaryInfo> dictionaryInfos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject jsonObject = jsonArray.getJSONObject(i);
            dictionaryInfos.add(DictionaryInfo.createDictionary(jsonObject));
        }
        return dictionaryInfos;
    }

    public Dictionary downloadDictionary(int id) throws IOException, JSONException {
        LOGGER.log(Level.INFO, "Requested Dictionary with identifier: " + id);
        Dictionary dictionary = null;

        URL url = new URL(URL + GET_DICTIONARY_POSTFIX + GET_DICTIONARY_PARAM + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", RequestHelper.SESSION_COOKIE + "=" + sessionService.getSessionId());
        connection.connect();

        if (connection.getResponseCode() == 200) {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String responseString = RequestHelper.convertStreamToString(in);
            //dictionary = Dictionary.createDictionary(new JSONObject(responseString));
            LOGGER.log(Level.INFO, responseString);
        } else {
            LOGGER.log(Level.SEVERE, String.valueOf(connection.getResponseCode()));
        }

        connection.disconnect();
        return dictionary;
    }

    public void uploadDictionary(Dictionary dictionary) throws IOException {
        LOGGER.log(Level.INFO, "Uploading Dictionary" );

        URL url = new URL(URL + ADD_DICTIONARY_POSTFIX);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", RequestHelper.CONTENT_TYPE);
        connection.setRequestProperty("Cookie", RequestHelper.SESSION_COOKIE + "=" + sessionService.getSessionId());
        connection.connect();

        //RequestHelper.writeToStream(connection.getOutputStream(), dictionary.toJson().toString());

        if (connection.getResponseCode() == 200) {
            LOGGER.log(Level.INFO, "Dictionary added");
        } else {
            LOGGER.log(Level.SEVERE, String.valueOf(connection.getResponseCode()));
        }

        connection.disconnect();
    }

    public boolean hasSession() {
        return sessionService.hasSession();
    }

    public void login(String email, String password) {
        try {
            sessionService.login(email, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
