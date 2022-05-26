package com.koli.openvocab.util.moshi;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.util.UUID;

public class UUIDAdapter extends JsonAdapter<UUID> {


    @FromJson
    @Override
    public UUID fromJson(JsonReader reader) throws IOException {
        return UUID.fromString(reader.readJsonValue().toString());
    }

    @ToJson
    @Override
    public void toJson(JsonWriter writer, UUID value) throws IOException {
        writer.jsonValue(value);
    }
}
