package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactoryBuilder;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

public class SmileParser extends Parser {
    protected SmileParser(SmileFactory factory, SmileMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    public static class Builder {
        public Builder() {}

        public SmileParser build() {
            SmileFactoryBuilder factoryBuilder = (SmileFactoryBuilder) SmileFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, true);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, true);

            SmileFactory factory = factoryBuilder.build();

            return new SmileParser(factory, new SmileMapper(factory), null);
        }
    }
}