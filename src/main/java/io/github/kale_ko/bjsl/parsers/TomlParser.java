package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.toml.TomlFactoryBuilder;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;

public class TomlParser extends Parser {
    protected TomlParser(TomlFactory factory, TomlMapper mapper, PrettyPrinter prettyPrinter) {
        super(factory, mapper, prettyPrinter);
    }

    public static class Builder {
        protected int indentLevel = 2;

        protected boolean crlf = false;

        public Builder() {}

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

        public TomlParser build() {
            TomlFactoryBuilder factoryBuilder = (TomlFactoryBuilder) TomlFactory.builder();
            factoryBuilder.configure(StreamReadFeature.USE_FAST_DOUBLE_PARSER, true);
            factoryBuilder.configure(StreamWriteFeature.USE_FAST_DOUBLE_WRITER, true);
            factoryBuilder.configure(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

            TomlFactory factory = factoryBuilder.build();

            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

            DefaultIndenter indenter = new DefaultIndenter();
            indenter.withIndent(" ".repeat(this.indentLevel));
            indenter.withLinefeed(this.crlf ? "\r\n" : "\n");

            prettyPrinter = prettyPrinter.withObjectIndenter(indenter).withArrayIndenter(indenter).withSpacesInObjectEntries();

            return new TomlParser(factory, new TomlMapper(factory), prettyPrinter);
        }
    }
}