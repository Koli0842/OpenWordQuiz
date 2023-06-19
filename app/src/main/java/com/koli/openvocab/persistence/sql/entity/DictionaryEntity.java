package com.koli.openvocab.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

@Entity
public class DictionaryEntity {

    @NonNull
    @PrimaryKey
    private UUID id;
    private String name;
    private Integer ordinal;

    public DictionaryEntity(@NonNull UUID id, String name, Integer ordinal) {
        this.id = id;
        this.name = name;
        this.ordinal = ordinal;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryEntity that = (DictionaryEntity) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(ordinal, that.ordinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ordinal);
    }
}
