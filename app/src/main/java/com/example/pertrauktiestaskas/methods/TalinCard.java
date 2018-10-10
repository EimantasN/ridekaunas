package com.example.pertrauktiestaskas.methods;

import java.io.UnsupportedEncodingException;

public class TalinCard {

    private final ClassicCard card;

    public TalinCard(ClassicCard card) {
        this.card = card;
    }

    public String getId() {
       DataClassicSector sector = (DataClassicSector) card.getSector(2);
        byte[] blocks = sector.readBlocks(0,2);
        byte[] new_block = ByteUtils.byteArraySlice(blocks, 15, blocks.length - 15); // Remove start
        byte[] newer_block = ByteUtils.byteArraySlice(new_block, 0, new_block.length - 6); // Remove end
        try {
            return new String(newer_block, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(newer_block);
        }
    }
}
