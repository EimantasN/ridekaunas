package com.example.pertrauktiestaskas.nfcPackage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoValue
public abstract class ClassicCardKeys implements CardKeys {

    /**
     * Mifare Classic uses 48-bit keys.
     */
    private static final int KEY_LEN = 6;

    /**
     * Reads keys from a binary bin dump created by proxmark3.
     */
    @NonNull
    public static ClassicCardKeys fromProxmark3(byte[] keysDump) {
        List<ClassicSectorKey> keys = new ArrayList<>();
        int numSectors = keysDump.length / KEY_LEN / 2;
        for (int i = 0; i < numSectors; i++) {
            int keyAOffset = (i * KEY_LEN);
            int keyBOffset = (i * KEY_LEN) + (KEY_LEN * numSectors);
            keys.add(ClassicSectorKey.create(readKey(keysDump, keyAOffset), readKey(keysDump, keyBOffset)));
        }
        return ClassicCardKeys.create(keys);
    }

    @NonNull
    public static ClassicCardKeys fromUserInput(byte[] A, byte[] B){
        List<ClassicSectorKey> keys = new ArrayList<>();
        keys.add(ClassicSectorKey.create(A, B));
        return ClassicCardKeys.create(keys);
    }

    @NonNull
    public static TypeAdapter<ClassicCardKeys> typeAdapter(@NonNull Gson gson) {
        return new AutoValue_ClassicCardKeys.GsonTypeAdapter(gson);
    }

    @NonNull
    private static ClassicCardKeys create(@NonNull List<ClassicSectorKey> keys) {
        return new AutoValue_ClassicCardKeys(CardType.MifareClassic, keys);
    }

    /**
     * Gets the key for a particular sector on the card.
     *
     * @param sectorNumber The sector number to retrieve the key for
     * @return A ClassicSectorKey for that sector, or null if there is no known key or the value is
     * out of range.
     */
    @Nullable
    public ClassicSectorKey keyForSector(int sectorNumber) {
        List<ClassicSectorKey> keys = keys();
        if (sectorNumber >= keys.size()) {
            return null;
        }
        return keys.get(sectorNumber);
    }

    public abstract List<ClassicSectorKey> keys();

    private static byte[] readKey(byte[] data, int offset) {
        return Arrays.copyOfRange(data, offset, offset + KEY_LEN);
    }
}
