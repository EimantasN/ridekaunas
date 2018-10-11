package com.example.pertrauktiestaskas.nfcPackage;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class DataClassicSector implements ClassicSector {

    @NonNull
    public static ClassicSector create(int sectorIndex, List<ClassicBlock> classicBlocks) {
        return new AutoValue_DataClassicSector(sectorIndex, classicBlocks);
    }

    @NonNull
    public abstract List<ClassicBlock> getBlocks();

    @NonNull
    public ClassicBlock getBlock(int index) {
        return getBlocks().get(index);
    }

    @NonNull
    public byte[] readBlocks(int startBlock, int blockCount) {
        int readBlocks = 0;
        byte[] data = new byte[blockCount * 16];
        for (int index = startBlock; index < (startBlock + blockCount); index++) {
            byte[] blockData = getBlock(index).getData().bytes();
            System.arraycopy(blockData, 0, data, readBlocks * 16, blockData.length);
            readBlocks++;
        }
        return data;
    }
}
