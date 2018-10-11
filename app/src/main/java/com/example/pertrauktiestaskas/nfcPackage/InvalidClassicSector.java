package com.example.pertrauktiestaskas.nfcPackage;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class InvalidClassicSector implements ClassicSector {

    @NonNull
    public static InvalidClassicSector create(int index, String error) {
        return new AutoValue_InvalidClassicSector(index, error);
    }

    @NonNull
    public abstract String getError();
}
