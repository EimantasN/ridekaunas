package com.example.pertrauktiestaskas.nfcPackage;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ClassicCard extends Card {

    @NonNull
    public static ClassicCard create(
            @NonNull ByteArray tagId,
            @NonNull Date scannedAt,
            @NonNull List<ClassicSector> sectors) {
        return new AutoValue_ClassicCard(tagId, scannedAt, sectors);
    }

    @NonNull
    public CardType getCardType() {
        return CardType.MifareClassic;
    }

    @NonNull
    public abstract List<ClassicSector> getSectors();

    public ClassicSector getSector(int index) {
        return getSectors().get(index);
    }

}
