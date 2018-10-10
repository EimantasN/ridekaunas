package com.example.pertrauktiestaskas.methods;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassicTagReader extends TagReader<MifareClassic, RawClassicCard, ClassicCardKeys> {

    private static final byte[] PREAMBLE_KEY = {
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00
    };

    public ClassicTagReader(@NonNull byte[] tagId, @NonNull Tag tag, @Nullable ClassicCardKeys cardKeys) {
        super(tagId, tag, cardKeys);
    }

    @NonNull
    @Override
    public MifareClassic getTech(@NonNull Tag tag) {
        return MifareClassic.get(tag);
    }

    @NonNull
    @Override
    public RawClassicCard readTag(
            @NonNull byte[] tagId,
            @NonNull Tag tag,
            @NonNull MifareClassic tech,
            @Nullable ClassicCardKeys keys) throws Exception {
        List<RawClassicSector> sectors = new ArrayList<>();

        for (int sectorIndex = 0; sectorIndex < tech.getSectorCount(); sectorIndex++) {
            try {
                boolean authSuccess = false;

                // Try the default keys first
                if (!authSuccess && sectorIndex == 0) {
                    if(!tech.isConnected())
                        tech.connect();
                    authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, PREAMBLE_KEY);
                }

                if (!authSuccess) {
                    if(!tech.isConnected())
                        tech.connect();
                    authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, MifareClassic.KEY_DEFAULT);
                }

                if (keys != null) {
                    // Try with a 1:1 sector mapping on our key list first
                    if (!authSuccess) {
                        ClassicSectorKey sectorKey = keys.keyForSector(sectorIndex);
                        if (sectorKey != null) {
                            authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, sectorKey.getKeyA().bytes());
                            if (!authSuccess) {
                                authSuccess = tech.authenticateSectorWithKeyB(sectorIndex, sectorKey.getKeyB().bytes());
                            }
                        }
                    }

                    if (!authSuccess) {
                        // Be a little more forgiving on the key list.  Lets try all the keys!
                        //
                        // This takes longer, of course, but means that users aren't scratching
                        // their heads when we don't get the right key straight away.
                        List<ClassicSectorKey> cardKeys = keys.keys();

                        for (int keyIndex = 0; keyIndex < cardKeys.size(); keyIndex++) {
                            if (keyIndex == sectorIndex) {
                                // We tried this before
                                continue;
                            }

                            authSuccess = tech.authenticateSectorWithKeyA(sectorIndex,
                                    cardKeys.get(keyIndex).getKeyA().bytes());

                            if (!authSuccess) {
                                authSuccess = tech.authenticateSectorWithKeyB(sectorIndex,
                                        cardKeys.get(keyIndex).getKeyB().bytes());
                            }

                            if (authSuccess) {
                                // Jump out if we have the key
                                break;
                            }
                        }
                    }
                }

                if (authSuccess) {
                    List<RawClassicBlock> blocks = new ArrayList<>();
                    // FIXME: First read trailer block to get type of other blocks.
                    int firstBlockIndex = tech.sectorToBlock(sectorIndex);
                    for (int blockIndex = 0; blockIndex < tech.getBlockCountInSector(sectorIndex); blockIndex++) {
                        byte[] data = tech.readBlock(firstBlockIndex + blockIndex);
                        blocks.add(RawClassicBlock.create(blockIndex, data));
                    }
                    sectors.add(RawClassicSector.createData(sectorIndex, blocks));
                } else {
                    sectors.add(RawClassicSector.createUnauthorized(sectorIndex));
                }
            } catch (IOException ex) {
                throw ex;
            } catch (Exception ex) {
                sectors.add(RawClassicSector.createInvalid(sectorIndex, ex.getMessage()));
            }
        }

        return RawClassicCard.create(tagId, new Date(), sectors);
    }
}
