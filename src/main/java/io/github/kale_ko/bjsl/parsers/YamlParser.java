package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * A parser for interfacing with YAML
 * <p>
 * Uses the Jackson-DataFormat yaml parser
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class YamlParser extends Parser<YAMLFactory, YAMLMapper> {
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
    protected YamlParser(YAMLFactory factory, YAMLMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link YamlParser}s
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * The indent level (in spaces) to use when pretty printing
         * <p>
         * Default is 2
         *
         * @since 1.0.0
         */
        protected int indentLevel = 2;

        /**
         * Weather to use crlf or lf line endings
         * <p>
         * Default is lf
         *
         * @since 1.0.0
         */
        protected boolean crlf = false;

        /**
         * Create a new {@link YamlParser} builder
         *
         * @since 1.0.0
         */
        public Builder() {}

        /**
         * Get the indent level (in spaces) to use when pretty printing
         * <p>
         * Default is 2
         *
         * @return The indent level (in spaces) to use when pretty printing
         * @since 1.0.0
         */
        public int getIndentLevel() {
            return this.indentLevel;
        }

        /**
         * Set the indent level (in spaces) to use when pretty printing
         * <p>
         * Default is 2
         *
         * @param value
         *        The indent level (in spaces) to use when pretty printing
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setIndentLevel(int value) {
            this.indentLevel = value;

            return this;
        }

        /**
         * Get weather to use crlf or lf line endings
         * <p>
         * Default is lf
         *
         * @return Weather to use crlf or lf line endings
         * @since 1.0.0
         */
        public boolean getCrlf() {
            return this.crlf;
        }

        /**
         * Set weather to use crlf or lf line endings
         * <p>
         * Default is lf
         *
         * @param value
         *        Weather to use crlf or lf line endings
         * @return Self for chaining
         * @since 1.0.0
         */
        public Builder setCrlf(boolean value) {
            this.crlf = value;

            return this;
        }

        /**
         * Uses the current settings to build a new {@link YamlParser}
         *
         * @return A new {@link YamlParser} instance
         * @since 1.0.0
         */
        public YamlParser build() {
            YAMLFactoryBuilder factoryBuilder = (YAMLFactoryBuilder) YAMLFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            factoryBuilder = factoryBuilder.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
            factoryBuilder = factoryBuilder.configure(YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS, this.crlf);

            YAMLFactory factory = factoryBuilder.build();

            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

            DefaultIndenter indenter = new DefaultIndenter();
            indenter = indenter.withIndent(" ".repeat(this.indentLevel));
            indenter = indenter.withLinefeed(this.crlf ? "\r\n" : "\n");

            prettyPrinter = prettyPrinter.withObjectIndenter(indenter).withArrayIndenter(indenter).withSpacesInObjectEntries();

            return new YamlParser(factory, new YAMLMapper(factory), prettyPrinter);
        }
    }
}