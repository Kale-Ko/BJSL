package io.github.kale_ko.bjsl.parsers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class JsonParser extends Parser {
    public JsonParser() {
        this(true);
    }

    public JsonParser(boolean prettyPrint) {
        this(prettyPrint, 2);
    }

    public JsonParser(boolean prettyPrint, int indent) {
        super(JsonFactory.builder().build(), (prettyPrint ? new DefaultPrettyPrinter().withObjectIndenter(new DefaultIndenter().withIndent(" ".repeat(indent)).withLinefeed("\n")).withArrayIndenter(new DefaultIndenter().withIndent(" ".repeat(indent)).withLinefeed("\n")).withSpacesInObjectEntries() : null));
    }
}