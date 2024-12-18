package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.kale_ko.bjsl.BJSL;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser for interfacing with JSON
 * <p>
 * Uses the default Jackson json parser
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class JsonParser extends JacksonParser<JsonFactory, JsonMapper> {
    /**
     * Create a new JacksonParser using certain factories
     *
     * @param factory       The factory used for converting to/from trees/strings
     * @param mapper        The mapper used for converting to/from trees/strings
     * @param prettyPrinter The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected JsonParser(@NotNull JsonFactory factory, @NotNull JsonMapper mapper, @Nullable PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link JsonParser}s
     *
     * @version 2.0.0
     * @since 1.0.0
     */
    public static class Builder {
        /**
         * Weather pretty printing should be enabled or not
         * <p>
         * Default is false
         *
         * @since 1.0.0
         */
        protected boolean prettyPrint = false;

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
         * Create a new {@link JsonParser} builder
         *
         * @since 1.0.0
         */
        public Builder() {
        }

        /**
         * Get weather pretty printing should be enabled or not
         * <p>
         * Default is false
         *
         * @return Weather pretty printing should be enabled or not
         *
         * @since 1.0.0
         */
        public boolean getPrettyPrint() {
            return this.prettyPrint;
        }

        /**
         * Set weather pretty printing should be enabled or not
         * <p>
         * Default is false
         *
         * @param value Weather pretty printing should be enabled or not
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setPrettyPrint(boolean value) {
            this.prettyPrint = value;

            return this;
        }

        /**
         * Get the indent level (in spaces) to use when pretty printing
         * <p>
         * Default is 2
         *
         * @return The indent level (in spaces) to use when pretty printing
         *
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
         * @param value The indent level (in spaces) to use when pretty printing
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setIndentLevel(int value) {
            this.indentLevel = value;

            return this;
        }

        /**
         * Get weather to use crlf or lf line endings
         * <p>
         * Default is lf
         *
         * @return Weather to use crlf or lf line endings
         *
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
         * @param value Weather to use crlf or lf line endings
         *
         * @return Self for chaining
         *
         * @since 1.0.0
         */
        public @NotNull Builder setCrlf(boolean value) {
            this.crlf = value;

            return this;
        }

        /**
         * Uses the current settings to build a new {@link JsonParser}
         *
         * @return A new {@link JsonParser} instance
         *
         * @since 1.0.0
         */
        public @NotNull JsonParser build() {
            JsonFactoryBuilder factoryBuilder = (JsonFactoryBuilder) JsonFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.STRICT_DUPLICATE_DETECTION, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_JAVA_COMMENTS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_YAML_COMMENTS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_MISSING_VALUES, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS, false);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS, false);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.ESCAPE_NON_ASCII, true);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.WRITE_HEX_UPPER_CASE, true);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.WRITE_NAN_AS_STRINGS, false);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.COMBINE_UNICODE_SURROGATES_IN_UTF8, true);

            JsonFactory factory = factoryBuilder.build();

            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

            if (prettyPrint) {
                DefaultIndenter indenter = new DefaultIndenter();
                indenter = indenter.withIndent(" ".repeat(this.indentLevel));
                indenter = indenter.withLinefeed(this.indentLevel > 0 ? (this.crlf ? "\r\n" : "\n") : "");

                Separators separators = new Separators();
                separators = separators.withObjectFieldValueSpacing(Separators.Spacing.AFTER);
                separators = separators.withObjectEntrySpacing(this.indentLevel <= 0 ? Separators.Spacing.AFTER : Separators.Spacing.NONE);
                separators = separators.withArrayValueSpacing(this.indentLevel <= 0 ? Separators.Spacing.AFTER : Separators.Spacing.NONE);
                separators = separators.withObjectEmptySeparator(" ");
                separators = separators.withArrayEmptySeparator(" ");

                prettyPrinter = prettyPrinter.withObjectIndenter(indenter).withArrayIndenter(indenter).withSeparators(separators);

                try {
                    Field separatorField = prettyPrinter.getClass().getDeclaredField("_objectFieldValueSeparatorWithSpaces");
                    separatorField.setAccessible(true);
                    separatorField.set(prettyPrinter, ": ");
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                    if (BJSL.getLogger() != null) {
                        StringWriter writer = new StringWriter();
                        new RuntimeException("Error while parsing:", e).printStackTrace(new PrintWriter(writer));
                        BJSL.getLogger().severe(writer.toString());
                    }
                }
            } else {
                DefaultIndenter indenter = new DefaultIndenter();
                indenter = indenter.withIndent("");
                indenter = indenter.withLinefeed("");

                Separators separators = new Separators();
                separators = separators.withObjectFieldValueSpacing(Separators.Spacing.NONE);
                separators = separators.withObjectEntrySpacing(Separators.Spacing.NONE);
                separators = separators.withArrayValueSpacing(Separators.Spacing.NONE);

                prettyPrinter = prettyPrinter.withObjectIndenter(indenter).withArrayIndenter(indenter).withSeparators(separators);
            }

            return new JsonParser(factory, new JsonMapper(factory), prettyPrinter);
        }
    }
}