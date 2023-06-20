package com.koli.openvocab.util;

import android.content.Context;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class StorageUtil {

    private final Logger log = Logger.getLogger(StorageUtil.class.getName());

    private final Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public void write(Uri path, String content) {
        try (OutputStream stream = context.getContentResolver().openOutputStream(path)) {
            stream.write(content.getBytes());
            log.info(String.format("Written file with name %s: %s", path.getPath(), content));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String read(Uri path) {
        try (InputStream stream = context.getContentResolver().openInputStream(path)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(stream.available());
            stream.read(byteBuffer.array());
            String content = new String(byteBuffer.array());
            log.info(String.format("Read file with name %s: %s", path.getPath(), content));
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
