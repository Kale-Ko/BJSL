package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactoryBuilder;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

/**
 * A parser for interfacing with Java properties
 * <p>
 * Uses the Jackson-DataFormat java properties parser
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class PropertiesParser extends Parser<JavaPropsFactory, JavaPropsMapper> {
    /**
     * Create a new Parser using certain factories
     *
     * @param factory
     *        The factory used for converting to/from trees/strings
     * @param mapper
     *        The mapper used for converting to/from trees/strings
     * @param prettyPrinter
     *        The prettyPrinter used for converting to strings
     * @since 1.0.0
     */
    protected PropertiesParser(JavaPropsFactory factory, JavaPropsMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link PropertiesParser}s
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Create a new {@link PropertiesParser} builder
         *
         * @since 1.0.0
         */
        public Builder() {}

        /**
         * Uses the current settings to build a new {@link PropertiesParser}
         *
         * @return A new {@link PropertiesParser} instance
         * @since 1.0.0
         */
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