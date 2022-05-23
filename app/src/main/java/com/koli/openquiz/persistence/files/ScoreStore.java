package com.koli.openquiz.persistence.files;

import android.content.Context;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ScoreStore {

    private final Logger log = Logger.getLogger(ScoreStore.class.getName());
    private final Context context;
    private final Path location;

    public ScoreStore(Context context) {
        this.context = context;
        this.location = Paths.get(context.getFilesDir().getAbsolutePath(), "score");
        if(location.toFile().mkdirs()) log.info("Created scores directory");
    }

    public void store(String key, int score) {
        try {
            Files.write(location.resolve(key), ByteBuffer.allocate(4).putInt(score).array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int read(String key) {
        try {
            byte[] bytes = Files.readAllBytes(location.resolve(key));
            return ByteBuffer.wrap(bytes).getInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
