package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactoryBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;

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
    protected XmlParser(XmlFactory factory, XmlMapper mapper, PrettyPrinter prettyPrinter) {
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
         * Create a new {@link XmlParser} builder
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
        public Builder setPrettyPrint(boolean value) {
            this.prettyPrint = value;

            return this;
        }

        /**
         * Uses the current settings to build a new {@link XmlParser}
         *
         * @return A new {@link XmlParser} instance
         *
         * @since 1.0.0
         */
        public XmlParser build() {
            XmlFactoryBuilder factoryBuilder = XmlFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            factoryBuilder = factoryBuilder.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

            XmlFactory factory = factoryBuilder.build();

            DefaultXmlPrettyPrinter prettyPrinter;

            if (prettyPrint) {
                prettyPrinter = new DefaultXmlPrettyPrinter();
            } else {
                prettyPrinter = new DefaultXmlPrettyPrinter();
                prettyPrinter.indentObjectsWith(null);
                prettyPrinter.indentArraysWith(null);
            }

            return new XmlParser(factory, new XmlMapper(factory), prettyPrinter);
        }
    }
}