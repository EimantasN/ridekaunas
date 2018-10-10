package com.example.pertrauktiestaskas.methods;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UnauthorizedClassicSector implements ClassicSector {

    @NonNull
    public static UnauthorizedClassicSector create(int sectorIndex) {
        return new AutoValue_UnauthorizedClassicSector(sectorIndex);
    }
}
