package com.example.pertrauktiestaskas.methods;

import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.pertrauktiestaskas.nfcPackage.CardKeys;
import com.example.pertrauktiestaskas.nfcPackage.RawCard;

public abstract class TagReader<
        T extends TagTechnology,
        C extends RawCard,
        K extends CardKeys> {

    @NonNull
    private final byte[] mTagId;
    @NonNull private final Tag mTag;
    @Nullable
    private final K mCardKeys;

    public TagReader(@NonNull byte[] tagId, @NonNull Tag tag, @Nullable K cardKeys) {
        mTagId = tagId;
        mTag = tag;
        mCardKeys = cardKeys;
    }


    @NonNull
    protected abstract T getTech(@NonNull Tag tag);
}
