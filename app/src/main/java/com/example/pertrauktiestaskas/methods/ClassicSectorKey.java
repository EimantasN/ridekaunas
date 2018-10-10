package com.example.pertrauktiestaskas.methods;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class ClassicSectorKey {

    @NonNull
    public static ClassicSectorKey create(@NonNull byte[] keyA, @NonNull byte[] keyB) {
        return new AutoValue_ClassicSectorKey(ByteArray.create(keyA), ByteArray.create(keyB));
    }

    @NonNull
    public static TypeAdapter<ClassicSectorKey> typeAdapter(@NonNull Gson gson) {
        return new AutoValue_ClassicSectorKey.GsonTypeAdapter(gson);
    }

    @NonNull
    public abstract ByteArray getKeyA();

    @NonNull
    public abstract ByteArray getKeyB();
}
