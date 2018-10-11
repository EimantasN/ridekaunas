package com.example.pertrauktiestaskas.methods;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.pertrauktiestaskas.nfcPackage.ClassicBlock;
import com.example.pertrauktiestaskas.nfcPackage.ClassicCard;
import com.example.pertrauktiestaskas.nfcPackage.ClassicCardKeys;
import com.example.pertrauktiestaskas.nfcPackage.ClassicSector;
import com.example.pertrauktiestaskas.nfcPackage.ClassicSectorKey;
import com.example.pertrauktiestaskas.nfcPackage.RawClassicBlock;
import com.example.pertrauktiestaskas.nfcPackage.RawClassicCard;
import com.example.pertrauktiestaskas.nfcPackage.RawClassicSector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassicTagRW extends TagReader<MifareClassic, RawClassicCard, ClassicCardKeys> {

    private final Tag tag;
    private final byte[] tagId;
    private final ClassicCardKeys cardKeys;
    private final MifareClassic tech;

    private static final byte[] PREAMBLE_KEY = {
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00
    };

    public ClassicTagRW(@NonNull byte[] tagId, @NonNull Tag tag, @Nullable ClassicCardKeys cardKeys) {
        super(tagId, tag, cardKeys);
        this.cardKeys = cardKeys;
        this.tag = tag;
        this.tagId = tagId;
        this.tech = getTech(tag);
    }

    @NonNull
    @Override
    public MifareClassic getTech(@NonNull Tag tag) {
        return MifareClassic.get(tag);
    }

    @SuppressLint("NewApi")
    @NonNull
    public ClassicSector readTagSector(@NonNull int sector) throws Exception {
        try {
            boolean authSuccess = false;

            // Try the default keys first
            if (!authSuccess && sector == 0) {
                if (!tech.isConnected())
                    tech.connect();
                authSuccess = tech.authenticateSectorWithKeyA(sector, PREAMBLE_KEY);
            }

            // Try other default key
            if (!authSuccess) {
                if (!tech.isConnected())
                    tech.connect();
                authSuccess = tech.authenticateSectorWithKeyA(sector, MifareClassic.KEY_DEFAULT);
            }

            // Authenticate with provided keys
            if (cardKeys != null) {
                // Try with a 1:1 sector mapping on our key list first
                if (!authSuccess) {
                    ClassicSectorKey sectorKey = cardKeys.keyForSector(sector);
                    if (sectorKey != null) {
                        authSuccess = tech.authenticateSectorWithKeyA(sector, sectorKey.getKeyA().bytes());
                        if (!authSuccess) {
                            authSuccess = tech.authenticateSectorWithKeyB(sector, sectorKey.getKeyB().bytes());
                        }
                    }
                }

                if (!authSuccess) {
                    // Be a little more forgiving on the key list.  Lets try all the keys!
                    //
                    // This takes longer, of course, but means that users aren't scratching
                    // their heads when we don't get the right key straight away.
                    List<ClassicSectorKey> cardKeys = this.cardKeys.keys();

                    for (int keyIndex = 0; keyIndex < cardKeys.size(); keyIndex++) {
                        if (keyIndex == sector) {
                            // We tried this before
                            continue;
                        }

                        authSuccess = tech.authenticateSectorWithKeyA(sector,
                                cardKeys.get(keyIndex).getKeyA().bytes());

                        if (!authSuccess) {
                            authSuccess = tech.authenticateSectorWithKeyB(sector,
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
                int firstBlockIndex = tech.sectorToBlock(sector);
                for (int blockIndex = 0; blockIndex < tech.getBlockCountInSector(sector); blockIndex++) {
                    byte[] data = tech.readBlock(firstBlockIndex + blockIndex);
                    blocks.add(RawClassicBlock.create(blockIndex, data));
                }

                IOUtils.closeQuietly(tech);
                return (ClassicSector) RawClassicSector.createData(sector, blocks);
            } else {
                IOUtils.closeQuietly(tech);
                return (ClassicSector) RawClassicSector.createUnauthorized(sector);
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            IOUtils.closeQuietly(tech);
            return (ClassicSector) RawClassicSector.createInvalid(sector, ex.getMessage());
        }
    }

    @SuppressLint("NewApi")
    @NonNull
    public ClassicCard readTagSectorRange(int sectorStart, int sectorEnd) throws Exception {
        List<RawClassicSector> sectors = new ArrayList<>();
        if (sectorStart < 0 || sectorEnd > tech.getSectorCount())
            return null;
        for (int sectorIndex = sectorStart; sectorIndex < sectorEnd; sectorIndex++) {
            try {
                boolean authSuccess = false;

                // Try the default keys first
                if (!authSuccess && sectorIndex == 0) {
                    if (!tech.isConnected())
                        tech.connect();
                    authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, PREAMBLE_KEY);
                }

                // Try other default key
                if (!authSuccess) {
                    if (!tech.isConnected())
                        tech.connect();
                    authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, MifareClassic.KEY_DEFAULT);
                }

                // Try authenticate with provided keys
                if (cardKeys != null) {
                    // Try with a 1:1 sector mapping on our key list first
                    if (!authSuccess) {
                        ClassicSectorKey sectorKey = cardKeys.keyForSector(sectorIndex);
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
                        List<ClassicSectorKey> cardKeys = this.cardKeys.keys();

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

        IOUtils.closeQuietly(tech);
        return RawClassicCard.create(tagId, new Date(), sectors).parse();
    }

    @SuppressLint("NewApi")
    @NonNull
    public ClassicCard readTag() throws Exception {
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

                // Try with other default key
                if (!authSuccess) {
                    if(!tech.isConnected())
                        tech.connect();
                    authSuccess = tech.authenticateSectorWithKeyA(sectorIndex, MifareClassic.KEY_DEFAULT);
                }

                // Try to authenticate with provided keys
                if (cardKeys != null) {
                    // Try with a 1:1 sector mapping on our key list first
                    if (!authSuccess) {
                        ClassicSectorKey sectorKey = cardKeys.keyForSector(sectorIndex);
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
                        List<ClassicSectorKey> cardKeys = this.cardKeys.keys();

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

        IOUtils.closeQuietly(tech);
        return RawClassicCard.create(tagId, new Date(), sectors).parse();
    }

    @NonNull
    public RawClassicCard writeTag(@NonNull ClassicBlock blockData,
                                   @NonNull int sector,
                                   @NonNull int block) {
        try {
            boolean authSuccess = false;
            // If tag isn't connected, connect
            if (!authSuccess && sector != 0 && cardKeys != null) {
                if (!tech.isConnected()) {
                    tech.connect();
                }
                // Authenticate with A key
                ClassicSectorKey sectorKey = cardKeys.keys().get(0);
                authSuccess = tech.authenticateSectorWithKeyA(sector, sectorKey.getKeyA().bytes());

                // If authentication failed, try B key
                if (!authSuccess)
                    authSuccess = tech.authenticateSectorWithKeyB(sector, sectorKey.getKeyB().bytes());
            }
            // If authentication succeed, write block
            if (authSuccess) {
                int pBlock = tech.sectorToBlock(sector);
                int blockIndex = pBlock + block;
                tech.writeBlock(blockIndex, blockData.getData().bytes());
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            IOUtils.closeQuietly(tech);
        }
    }
}
