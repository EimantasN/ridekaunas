package com.example.pertrauktiestaskas.methods;

public class ClassicUtils {

    private ClassicUtils() { }

    public static int convertBytePointerToBlock(int index) {
        int block;

        if (index >= 2048) { // Sector 32 (0x800)
            block = 128;
            index -= 2048;
            block += index / 16;
        } else {
            block = index / 16;
        }

        return block;
    }

    public static int sectorToBlock(int sectorIndex) {
        if (sectorIndex < 32) {
            return sectorIndex * 4;
        } else {
            return 32 * 4 + (sectorIndex - 32) * 16;
        }
    }

    public static int blockToSector(int blockIndex) {
        if (blockIndex < 32 * 4) {
            return blockIndex / 4;
        } else {
            return 32 + (blockIndex - 32 * 4) / 16;
        }
    }
}
