package io.github.kale_ko.bjsl;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class BJSLLogger {
    public enum Level {
        INFO(""), WARNING(""), ERROR("");

        private String format;

        private Level(String format) {
            this.format = format;
        }
    }

    protected PrintWriter printWriter;

    protected String format;
    protected String prefix;

    protected boolean enabled = true;

    public BJSLLogger(String prefix) {
        this(prefix, "[{time} {LEVEL}]: [{prefix}] {message}");
    }

    public BJSLLogger(String prefix, String format) {
        this(prefix, format, System.out);
    }

    public BJSLLogger(String prefix, PrintStream printWriter) {
        this(prefix, "[{time} {LEVEL}]: [{prefix}] {message}", printWriter);
    }

    public BJSLLogger(String prefix, PrintWriter printWriter) {
        this(prefix, "[{time} {LEVEL}]: [{prefix}] {message}", printWriter);
    }

    public BJSLLogger(String prefix, String format, PrintStream printWriter) {
        this(prefix, format, new PrintWriter(printWriter));
    }

    public BJSLLogger(String prefix, String format, PrintWriter printWriter) {
        this.printWriter = printWriter;

        this.format = format;
        this.prefix = prefix;
    }

    public void log(Level level, String message) {
        if (this.printWriter == null) {
            return;
        }

        this.printWriter.println(this.format.replace("{message}", level.format + message).replace("{prefix}", this.prefix).replace("{PREFIX}", this.prefix.toUpperCase()).replace("{level}", level.name().toLowerCase()).replace("{LEVEL}", level.name().toUpperCase()).replace("{time}", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)).replace("{time12}", Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)).replace("{time24}", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND)));
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

    public void setPrintWriter(PrintStream value) {
        this.setPrintWriter(new PrintWriter(value));
    }

    public void setPrintWriter(PrintWriter value) {
        this.printWriter = value;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String value) {
        this.format = value;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String value) {
        this.prefix = value;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }
}