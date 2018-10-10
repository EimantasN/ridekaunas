package com.example.pertrauktiestaskas.methods;

import android.support.annotation.NonNull;

import java.util.Date;

public interface RawCard<T extends Card> {

    @NonNull
    CardType cardType();

    @NonNull
    ByteArray tagId();

    @NonNull
    Date scannedAt();

    boolean isUnauthorized();

    @NonNull
    T parse();
}