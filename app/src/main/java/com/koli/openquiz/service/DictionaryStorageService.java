package com.koli.openquiz.service;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class DictionaryStorageService {

    private Context context;
    private File directory;

    public DictionaryStorageService(Context context) {
        this.context = context;
        initDirectory();
    }

    private void initDirectory() {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "dictionary";
        directory = new File(path);
        if(!directory.exists())
            directory.mkdirs();
    }

    public void store(String name, String content) {
        File file = new File(directory, name);
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String name) {

        File file = new File(directory, name);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public String readFromStorage(Uri path) {
        byte[] bytes = new byte[0];

        try (InputStream stream = context.getContentResolver().openInputStream(path)) {
            bytes = new byte[stream.available()];
            stream.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    public boolean contains(String filename) {
        return list().contains(filename);
    }

    public List<String> list() {
        return Arrays.asList(directory.list());
    }
}
