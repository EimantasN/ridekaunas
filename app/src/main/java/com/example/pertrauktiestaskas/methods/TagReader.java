package com.example.pertrauktiestaskas.methods;

import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public C readTag() throws Exception {
        T tech = getTech(mTag);
        try {
            tech.connect();
            return readTag(mTagId, mTag, tech, mCardKeys);
        } finally {
            if (tech.isConnected()) {
                IOUtils.closeQuietly(tech);
            }
        }
    }

    @NonNull
    protected abstract C readTag(
            @NonNull byte[] tagId,
            @NonNull Tag tag,
            @NonNull T tech,
            @Nullable K cardKeys)
            throws Exception;

    @NonNull
    protected abstract T getTech(@NonNull Tag tag);
}
