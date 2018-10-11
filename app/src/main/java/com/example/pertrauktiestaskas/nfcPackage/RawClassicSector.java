package com.example.pertrauktiestaskas.nfcPackage;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.google.auto.value.AutoValue;
import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

@AutoValue
public abstract class RawClassicSector {

    public static final String TYPE_DATA = "data";
    public static final String TYPE_INVALID = "invalid";
    public static final String TYPE_UNAUTHORIZED = "unauthorized";

    @NonNull
    public static TypeAdapter<RawClassicSector> typeAdapter(@NonNull Gson gson) {
        return new AutoValue_RawClassicSector.GsonTypeAdapter(gson);
    }

    @NonNull
    public static RawClassicSector createData(int index, @NonNull List<RawClassicBlock> blocks) {
        return new AutoValue_RawClassicSector(TYPE_DATA, index, blocks, null);
    }

    @NonNull
    public static RawClassicSector createInvalid(int index, @NonNull String errorMessage) {
        return new AutoValue_RawClassicSector(TYPE_INVALID, index, null, errorMessage);
    }

    @NonNull
    public static RawClassicSector createUnauthorized(int index) {
        return new AutoValue_RawClassicSector(TYPE_UNAUTHORIZED, index, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    public ClassicSector parse() {
        switch (type()) {
            case TYPE_DATA:
                List<ClassicBlock> blocks = newArrayList(transform(blocks(),
                        new Function<RawClassicBlock, ClassicBlock>() {
                            @Override
                            public ClassicBlock apply(RawClassicBlock rawClassicBlock) {
                                return rawClassicBlock.parse();
                            }
                        }));
                return DataClassicSector.create(index(), blocks);
            case TYPE_INVALID:
                return InvalidClassicSector.create(index(), errorMessage());
            case TYPE_UNAUTHORIZED:
                return UnauthorizedClassicSector.create(index());
        }
        throw new RuntimeException("Unknown type");
    }

    @NonNull
    public abstract String type();

    public abstract int index();

    @Nullable
    public abstract List<RawClassicBlock> blocks();

    @Nullable
    public abstract String errorMessage();
}
