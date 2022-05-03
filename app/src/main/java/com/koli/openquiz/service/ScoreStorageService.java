package com.koli.openquiz.service;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScoreStorageService {

    private Context context;
    private File directory;

    public ScoreStorageService(Context context) {
        this.context = context;
        initDirectory();
    }

    private void initDirectory() {
        String path = context.getFilesDir().getAbsolutePath() + File.separator + "score";
        directory = new File(path);
        if(!directory.exists())
            directory.mkdirs();
    }

    public void store(String key, int score) {
        File file = new File(directory, key);
        try(FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(ByteBuffer.allocate(4).putInt(score).array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int read(String key) {

        File file = new File(directory, key);
        int result = 0;

        try (FileInputStream stream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            stream.read(bytes);
            result = ByteBuffer.wrap(bytes).getInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
