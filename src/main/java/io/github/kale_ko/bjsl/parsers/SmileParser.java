package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactoryBuilder;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

public class SmileParser extends Parser {
    protected SmileParser(SmileFactory factory, SmileMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    public static class Builder {
        public Builder() {}

        public SmileParser build() {
            SmileFactoryBuilder factoryBuilder = (SmileFactoryBuilder) SmileFactory.builder();
            factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

            SmileFactory factory = factoryBuilder.build();

            return new SmileParser(factory, new SmileMapper(factory), null);
        }
    }
}