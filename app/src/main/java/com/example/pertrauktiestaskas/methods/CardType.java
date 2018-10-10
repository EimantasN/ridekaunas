package com.example.pertrauktiestaskas.methods;

public enum CardType {
    MifareClassic,
    MifareUltralight,
    MifareDesfire,
    CEPAS,
    FeliCa,
    Sample;

    public String toString() {
        switch (this) {
            case MifareClassic:
                return "MIFARE Classic";
            case MifareUltralight:
                return "MIFARE Ultralight";
            case MifareDesfire:
                return "MIFARE DESFire";
            case CEPAS:
                return "CEPAS";
            case FeliCa:
                return "FeliCa";
            case Sample:
                return "Sample Card";
            default:
                return "Unknown";
        }
    }
}
