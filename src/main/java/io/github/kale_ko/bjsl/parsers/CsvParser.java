package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvFactoryBuilder;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser for interfacing with CSV
 * <p>
 * Uses the Jackson-DataFormat csv parser
 *
 * @version 1.1.0
 * @since 1.0.0
 */
public class CsvParser extends Parser<CsvFactory, CsvMapper> {
    /**
     * Create a new Parser using certain factories
     *
     * @param factory       The factory used for converting to/from trees/strings
     * @param mapper        The mapper used for converting to/from trees/strings
     * @param prettyPrinter The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected CsvParser(@NotNull CsvFactory factory, @NotNull CsvMapper mapper, @Nullable PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link CsvParser}s
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Create a new {@link CsvParser} builder
         *
         * @since 1.0.0
         */
        public Builder() {
        }

        /**
         * Uses the current settings to build a new {@link CsvParser}
         *
         * @return A new {@link CsvParser} instance
         *
         * @since 1.0.0
         */
        public @NotNull CsvParser build() {
            CsvFactoryBuilder factoryBuilder = CsvFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            factoryBuilder = factoryBuilder.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
            factoryBuilder = factoryBuilder.configure(CsvGenerator.Feature.ALWAYS_QUOTE_EMPTY_STRINGS, true);
            factoryBuilder = factoryBuilder.configure(Feature.TRIM_SPACES, true);

            CsvFactory factory = factoryBuilder.build();

            return new CsvParser(factory, new CsvMapper(factory), null);
        }
    }
}