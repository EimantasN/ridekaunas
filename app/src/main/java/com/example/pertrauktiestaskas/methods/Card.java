package com.example.pertrauktiestaskas.methods;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;

public abstract class Card {

    @NonNull
    public abstract CardType getCardType();

    @NonNull
    public abstract ByteArray getTagId();

    @NonNull
    public abstract Date getScannedAt();

    //@NonNull
    //public abstract FareBotUiTree getAdvancedUi(Context context);

    @NonNull
    @SuppressWarnings("unchecked")
    public Class<? extends Card> getParentClass() {
        Class<? extends Card> aClass = getClass();
        while (aClass.getSuperclass() != Card.class) {
            aClass = (Class<? extends Card>) aClass.getSuperclass();
        }
        return aClass;
    }
}
