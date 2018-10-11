package com.example.pertrauktiestaskas.nfcPackage;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ClassicBlock {

    public static final String TYPE_DATA = "data";
    public static final String TYPE_MANUFACTURER = "manufacturer";
    public static final String TYPE_TRAILER = "trailer";
    public static final String TYPE_VALUE = "value";

    @NonNull
    public static ClassicBlock create(@NonNull String type, int index, @NonNull ByteArray data) {
        return new AutoValue_ClassicBlock(type, index, data);
    }

    @NonNull
    public abstract String getType();

    public abstract int getIndex();

    @NonNull
    public abstract ByteArray getData();
}
