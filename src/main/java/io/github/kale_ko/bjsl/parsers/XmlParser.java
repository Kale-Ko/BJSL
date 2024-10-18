package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactoryBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlNameProcessors;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A parser for interfacing with XML
 * <p>
 * Uses the default Jackson xml parser
 *
 * @version 1.1.0
 * @since 1.0.0
 */
public class XmlParser extends Parser<XmlFactory, XmlMapper> {
    /**
     * Create a new Parser using certain factories
     *
     * @param factory       The factory used for converting to/from trees/strings
     * @param mapper        The mapper used for converting to/from trees/strings
     * @param prettyPrinter The prettyPrinter used for converting to strings
     *
     * @since 1.0.0
     */
    protected XmlParser(@NotNull XmlFactory factory, @NotNull XmlMapper mapper, @Nullable PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    /**
     * A builder class for creating new {@link XmlParser}s
     *
     * @version 1.0.0
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
         * Uses the current settings to build a new {@link XmlParser}
         *
         * @return A new {@link XmlParser} instance
         *
         * @since 1.0.0
         */
        public @NotNull XmlParser build() {
            XmlFactoryBuilder factoryBuilder = XmlFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.STRICT_DUPLICATE_DETECTION, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);
            factoryBuilder = factoryBuilder.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            factoryBuilder = factoryBuilder.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
            factoryBuilder = factoryBuilder.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true);
            factoryBuilder = factoryBuilder.configure(ToXmlGenerator.Feature.WRITE_XML_SCHEMA_CONFORMING_FLOATS, true);
            factoryBuilder = factoryBuilder.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);

            factoryBuilder.xmlNameProcessor(XmlNameProcessors.newBase64Processor("base64_"));

            XmlFactory factory = factoryBuilder.build();

            factory.setCharacterEscapes(new CharacterEscapes() {
                @Override
                public int[] getEscapeCodesForAscii() {
                    return CharacterEscapes.standardAsciiEscapesForJSON();
                }

                @Override
                public SerializableString getEscapeSequence(int ch) {
                    return null;
                }
            });

            DefaultXmlPrettyPrinter prettyPrinter = new DefaultXmlPrettyPrinter();

            prettyPrinter.withCustomNewLine(this.crlf ? "\r\n" : "\n");

            if (!prettyPrint) {
                prettyPrinter.indentObjectsWith(null);
                prettyPrinter.indentArraysWith(null);
            }

            return new XmlParser(factory, new XmlMapper(factory), prettyPrinter);
        }
    }
}