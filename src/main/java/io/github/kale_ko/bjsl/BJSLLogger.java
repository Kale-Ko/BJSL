package io.github.kale_ko.bjsl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.temporal.ChronoField;

public class BJSLLogger {
    public enum Level {
        INFO(""), WARNING(""), ERROR("");

        private String format;

        private Level(String format) {
            this.format = format;
        }
    }

    protected String format;
    protected String prefix;

    protected boolean enabled = true;

    public BJSLLogger(String prefix) {
        this(prefix, "[{time} {LEVEL}]: [{prefix}] {message}");
    }

    public BJSLLogger(String prefix, String format) {
        this.format = format;
        this.prefix = prefix;
    }

    public void log(Level level, String message) {
        System.console().writer().println(this.format.replace("{message}", level.format + message).replace("{prefix}", this.prefix).replace("{PREFIX}", this.prefix.toUpperCase()).replace("{level}", level.name().toLowerCase()).replace("{LEVEL}", level.name().toUpperCase()).replace("{time}", Instant.now().get(ChronoField.HOUR_OF_DAY) + ":" + Instant.now().get(ChronoField.MINUTE_OF_HOUR) + ":" + Instant.now().get(ChronoField.SECOND_OF_MINUTE)).replace("{time12}", Instant.now().get(ChronoField.HOUR_OF_AMPM) + ":" + Instant.now().get(ChronoField.MINUTE_OF_HOUR) + ":" + Instant.now().get(ChronoField.SECOND_OF_MINUTE)).replace("{time24}", Instant.now().get(ChronoField.HOUR_OF_DAY) + ":" + Instant.now().get(ChronoField.MINUTE_OF_HOUR) + ":" + Instant.now().get(ChronoField.SECOND_OF_MINUTE)));
    }

    public void log(Level level, Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        this.log(level, writer.toString());
    }

    public void info(String message) {
        this.log(Level.INFO, message);
    }

    public void info(Exception exception) {
        this.log(Level.INFO, exception);
    }

    public void warning(String message) {
        this.log(Level.WARNING, message);
    }

    public void warning(Exception exception) {
        this.log(Level.WARNING, exception);
    }

    public void error(String message) {
        this.log(Level.ERROR, message);
    }

    public void error(Exception exception) {
        this.log(Level.ERROR, exception);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }
}