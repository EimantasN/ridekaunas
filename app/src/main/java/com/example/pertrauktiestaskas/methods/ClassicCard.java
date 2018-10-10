package com.example.pertrauktiestaskas.methods;

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

//    @NonNull
//    @Override
//    public FareBotUiTree getAdvancedUi(Context context) {
//        FareBotUiTree.Builder cardUiBuilder = FareBotUiTree.builder(context);
//        for (ClassicSector sector : getSectors()) {
//            String sectorIndexString = Integer.toHexString(sector.getIndex());
//            FareBotUiTree.Item.Builder sectorUiBuilder = cardUiBuilder.item();
//            if (sector instanceof UnauthorizedClassicSector) {
//                sectorUiBuilder.title(context.getString(
//                        R.string.classic_unauthorized_sector_title_format, sectorIndexString));
//            } else if (sector instanceof InvalidClassicSector) {
//                InvalidClassicSector errorSector = (InvalidClassicSector) sector;
//                sectorUiBuilder.title(context.getString(
//                        R.string.classic_invalid_sector_title_format, sectorIndexString, errorSector.getError()));
//            } else {
//                DataClassicSector dataClassicSector = (DataClassicSector) sector;
//                sectorUiBuilder.title(context.getString(R.string.classic_sector_title_format, sectorIndexString));
//                for (ClassicBlock block : dataClassicSector.getBlocks()) {
//                    sectorUiBuilder.item()
//                            .title(context.getString(
//                                    R.string.classic_block_title_format,
//                                    String.valueOf(block.getIndex())))
//                            .value(block.getData());
//                }
//            }
//        }
//        return cardUiBuilder.build();
//    }
}
