package com.koli.openquiz.persistence;

import android.content.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class DictionaryStore {

    private final Logger log = Logger.getLogger(DictionaryStore.class.getName());
    private final Context context;
    private final Path location;

    public DictionaryStore(Context context) {
        this.context = context;
        this.location = Paths.get(context.getFilesDir().getAbsolutePath(), "dictionary");
        if(location.toFile().mkdirs()) log.info("Created dictionary directory");
    }

    public void store(String name, String content) {
        log.info(String.format("Saving file with name %s: %s", name, content));
        try {
            Files.write(location.resolve(name), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String name) {
        log.info(String.format("Reading file %s", name));
        try {
            byte[] bytes = Files.readAllBytes(location.resolve(name));
            String content = new String(bytes);
            log.info(String.format("Read file %s: %s", name, content));
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean contains(String filename) {
        return list().contains(filename);
    }

    public List<String> list() {
        String[] files = location.toFile().list();
        return files == null ? List.of() : List.of(files);
    }
}
