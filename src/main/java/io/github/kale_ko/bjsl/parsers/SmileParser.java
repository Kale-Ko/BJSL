package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileFactoryBuilder;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser for interfacing with Smile (Binary JSON)
 * <p>
 * Uses the Jackson-DataFormat smile parser
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class SmileParser extends JacksonParser<SmileFactory, SmileMapper> {
    /**
     * Create a new JacksonParser using certain factories
     *
     * @param factory       The factory used for converting to/from trees/strings
     * @param mapper        The mapper used for converting to/from trees/strings
     * @param prettyPrinter The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected SmileParser(@NotNull SmileFactory factory, @NotNull SmileMapper mapper, @Nullable PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link SmileParser}s
     *
     * @version 2.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Create a new {@link SmileParser} builder
         *
         * @since 1.0.0
         */
        public Builder() {
        }

        /**
         * Uses the current settings to build a new {@link SmileParser}
         *
         * @return A new {@link SmileParser} instance
         *
         * @since 1.0.0
         */
        public @NotNull SmileParser build() {
            SmileFactoryBuilder factoryBuilder = SmileFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.STRICT_DUPLICATE_DETECTION, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.WRITE_HEADER, true);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.WRITE_END_MARKER, true);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.CHECK_SHARED_NAMES, true);
            factoryBuilder = factoryBuilder.configure(SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES, false);

            SmileFactory factory = factoryBuilder.build();

            return new SmileParser(factory, new SmileMapper(factory), null);
        }
    }
}