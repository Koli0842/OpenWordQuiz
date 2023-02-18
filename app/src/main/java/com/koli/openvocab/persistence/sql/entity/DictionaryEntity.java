package com.koli.openvocab.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class DictionaryEntity {

    @NonNull
    @PrimaryKey
    private final UUID id;
    private final String name;
    private final Integer ordinal;

    public DictionaryEntity(@NonNull UUID id, String name, Integer ordinal) {
        this.id = id;
        this.name = name;
        this.ordinal = ordinal;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }
}
