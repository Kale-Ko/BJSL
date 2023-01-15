package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonParser extends Parser {
    protected JsonParser(JsonFactory factory, JsonMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    public static class Builder {
        protected boolean prettyPrint = false;
        protected int indentLevel = 2;

        protected boolean crlf = false;

        public Builder() {}

        public boolean getPrettyPrint() {
            return this.prettyPrint;
        }

        public Builder setPrettyPrint(boolean value) {
            this.prettyPrint = value;

            return this;
        }

        public int getIndentLevel() {
            return this.indentLevel;
        }

        public Builder setIndentLevel(int value) {
            this.indentLevel = value;

            return this;
        }

        public boolean getCrlf() {
            return this.crlf;
        }

        public Builder setCrlf(boolean value) {
            this.crlf = value;

            return this;
        }

        public JsonParser build() {
            JsonFactoryBuilder factoryBuilder = (JsonFactoryBuilder) JsonFactory.builder();
            factoryBuilder = factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder = factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_TRAILING_COMMA, true);
            factoryBuilder = factoryBuilder.configure(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS, true);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.ESCAPE_NON_ASCII, true);
            factoryBuilder = factoryBuilder.configure(JsonWriteFeature.WRITE_HEX_UPPER_CASE, true);

            JsonFactory factory = factoryBuilder.build();

            DefaultPrettyPrinter prettyPrinter = null;

            if (prettyPrint) {
                prettyPrinter = new DefaultPrettyPrinter();

                DefaultIndenter indenter = new DefaultIndenter();
                indenter = indenter.withIndent(" ".repeat(this.indentLevel));
                indenter = indenter.withLinefeed(this.crlf ? "\r\n" : "\n");

                prettyPrinter = prettyPrinter.withObjectIndenter(indenter).withArrayIndenter(indenter).withSpacesInObjectEntries();
            }

            return new JsonParser(factory, new JsonMapper(factory), prettyPrinter);
        }
    }
}