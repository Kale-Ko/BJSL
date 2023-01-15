package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactoryBuilder;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

public class PropertiesParser extends Parser {
    protected PropertiesParser(JavaPropsFactory factory, JavaPropsMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    public static class Builder {
        public Builder() {}

        public PropertiesParser build() {
            JavaPropsFactoryBuilder factoryBuilder = (JavaPropsFactoryBuilder) JavaPropsFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

            JavaPropsFactory factory = factoryBuilder.build();

            return new PropertiesParser(factory, new JavaPropsMapper(factory), null);
        }
    }
}