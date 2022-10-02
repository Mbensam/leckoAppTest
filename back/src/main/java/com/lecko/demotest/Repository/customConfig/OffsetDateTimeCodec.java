package com.lecko.demotest.Repository.customConfig;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.OffsetDateTime;

public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {

    @Override
    public void encode(final BsonWriter writer, final OffsetDateTime value, final EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }

    @Override
    public OffsetDateTime decode(final BsonReader reader, final DecoderContext decoderContext) {
        return OffsetDateTime.parse(reader.readString());
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
