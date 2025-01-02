import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import io.github.kale_ko.bjsl.processor.DefaultTypeProcessors;
import java.io.File;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(value=1000, unit=TimeUnit.MILLISECONDS)
public class DefaultTypeProcessorsTest {
    protected final DefaultTypeProcessors typeProcessorsMode1 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.STRING, DefaultTypeProcessors.Options.InetAddressMode.STRING, false, DefaultTypeProcessors.Options.DateMode.STRING, DateTimeFormatter.ofPattern("MMMM dd yyyy @ hh:mm:ss.SSS a XXX").withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));
    protected final DefaultTypeProcessors typeProcessorsMode2 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.BYTE_ARRAY, DefaultTypeProcessors.Options.InetAddressMode.STRING, true, DefaultTypeProcessors.Options.DateMode.STRING, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));
    protected final DefaultTypeProcessors typeProcessorsMode3 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.SHORT_ARRAY, DefaultTypeProcessors.Options.InetAddressMode.NUMBER, false, DefaultTypeProcessors.Options.DateMode.STRING, DateTimeFormatter.ISO_INSTANT.withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));
    protected final DefaultTypeProcessors typeProcessorsMode4 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.INT_ARRAY, DefaultTypeProcessors.Options.InetAddressMode.NUMBER, false, DefaultTypeProcessors.Options.DateMode.NUMBER, DateTimeFormatter.ISO_DATE_TIME.withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));
    protected final DefaultTypeProcessors typeProcessorsMode5 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.LONG_ARRAY, DefaultTypeProcessors.Options.InetAddressMode.NUMBER, false, DefaultTypeProcessors.Options.DateMode.NUMBER, DateTimeFormatter.ISO_DATE_TIME.withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));
    protected final DefaultTypeProcessors typeProcessorsMode6 = new DefaultTypeProcessors(new DefaultTypeProcessors.Options(DefaultTypeProcessors.Options.UUIDMode.NUMBER, DefaultTypeProcessors.Options.InetAddressMode.NUMBER, false, DefaultTypeProcessors.Options.DateMode.NUMBER, DateTimeFormatter.ISO_DATE_TIME.withLocale(Locale.US).withZone(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-5)))));

    protected static class StringBuilderProvider implements ArgumentsProvider {
        protected record StringBuilderHolder(String string, StringBuilder stringBuilder) {
        }

        public StringBuilderProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("test", new StringBuilderProvider.StringBuilderHolder("test", new StringBuilder("test"))), Arguments.argumentSet("<empty>", new StringBuilderProvider.StringBuilderHolder("", new StringBuilder())), Arguments.argumentSet("\"hello world\"", new StringBuilderProvider.StringBuilderHolder("\"hello world\"", new StringBuilder("\"hello world\""))));
        }
    }

    protected static class StringBufferProvider implements ArgumentsProvider {
        protected record StringBufferHolder(String string, StringBuffer stringBuffer) {
        }

        public StringBufferProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("test", new StringBufferProvider.StringBufferHolder("test", new StringBuffer("test"))), Arguments.argumentSet("<empty>", new StringBufferProvider.StringBufferHolder("", new StringBuffer())), Arguments.argumentSet("\"hello world\"", new StringBufferProvider.StringBufferHolder("\"hello world\"", new StringBuffer("\"hello world\""))));
        }
    }

    protected static class UUIDProvider implements ArgumentsProvider {
        protected static class UUIDHolder {
            protected final String string;

            protected final byte[] bytes;
            protected final ParsedArray bytesArray;

            protected final short[] shorts;
            protected final ParsedArray shortsArray;

            protected final int[] integers;
            protected final ParsedArray integersArray;

            protected final long[] longs;
            protected final ParsedArray longsArray;

            protected final BigInteger bigInteger;

            protected final UUID uuid;

            public UUIDHolder(String string, byte[] bytes, short[] shorts, int[] integers, long[] longs, BigInteger bigInteger, UUID uuid) {
                this.string = string;

                this.bytes = bytes;
                this.bytesArray = ParsedArray.create();
                for (byte b : bytes) {
                    this.bytesArray.add(ParsedPrimitive.fromByte(b));
                }

                this.shorts = shorts;
                this.shortsArray = ParsedArray.create();
                for (short s : shorts) {
                    this.shortsArray.add(ParsedPrimitive.fromShort(s));
                }

                this.integers = integers;
                this.integersArray = ParsedArray.create();
                for (int i : integers) {
                    this.integersArray.add(ParsedPrimitive.fromInteger(i));
                }

                this.longs = longs;
                this.longsArray = ParsedArray.create();
                for (long l : longs) {
                    this.longsArray.add(ParsedPrimitive.fromLong(l));
                }

                this.bigInteger = bigInteger;

                this.uuid = uuid;
            }

            public String getString() {
                return this.string;
            }

            public byte[] getBytes() {
                return this.bytes;
            }

            public ParsedArray getBytesArray() {
                return this.bytesArray;
            }

            public short[] getShorts() {
                return this.shorts;
            }

            public ParsedArray getShortsArray() {
                return this.shortsArray;
            }

            public int[] getIntegers() {
                return this.integers;
            }

            public ParsedArray getIntegersArray() {
                return this.integersArray;
            }

            public long[] getLongs() {
                return this.longs;
            }

            public ParsedArray getLongsArray() {
                return this.longsArray;
            }

            public BigInteger getBigInteger() {
                return this.bigInteger;
            }

            public UUID getUUID() {
                return this.uuid;
            }
        }

        public UUIDProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("e4ec171a-665d-43fa-946e-852cc67cc590", new UUIDProvider.UUIDHolder("e4ec171a-665d-43fa-946e-852cc67cc590", new byte[] { (byte) -28, (byte) -20, (byte) 23, (byte) 26, (byte) 102, (byte) 93, (byte) 67, (byte) -6, (byte) -108, (byte) 110, (byte) -123, (byte) 44, (byte) -58, (byte) 124, (byte) -59, (byte) -112 }, new short[] { (short) -6932, (short) 5914, (short) 26205, (short) 17402, (short) -27538, (short) -31444, (short) -14724, (short) -14960 }, new int[] { -454289638, 1717388282, -1804696276, -964901488 }, new long[] { -1951159136404290566L, -7751111481302923888L }, new BigInteger(new byte[] { (byte) -28, (byte) -20, (byte) 23, (byte) 26, (byte) 102, (byte) 93, (byte) 67, (byte) -6, (byte) -108, (byte) 110, (byte) -123, (byte) 44, (byte) -58, (byte) 124, (byte) -59, (byte) -112 }), UUID.fromString("e4ec171a-665d-43fa-946e-852cc67cc590"))), Arguments.argumentSet("00000000-0000-0000-0000-000000000000", new UUIDProvider.UUIDHolder("00000000-0000-0000-0000-000000000000", new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }, new short[] { (short) 0x00, (short) 0x00, (short) 0x00, (short) 0x00, (short) 0x00, (short) 0x00, (short) 0x00, (short) 0x00 }, new int[] { 0x00, 0x00, 0x00, 0x00 }, new long[] { (long) 0x00, (long) 0x00 }, new BigInteger(new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }), UUID.fromString("00000000-0000-0000-0000-000000000000"))), Arguments.argumentSet("ffffffff-ffff-ffff-ffff-ffffffffffff", new UUIDProvider.UUIDHolder("ffffffff-ffff-ffff-ffff-ffffffffffff", new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff }, new short[] { (short) 0xffff, (short) 0xffff, (short) 0xffff, (short) 0xffff, (short) 0xffff, (short) 0xffff, (short) 0xffff, (short) 0xffff }, new int[] { 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff }, new long[] { 0xffffffffffffffffL, 0xffffffffffffffffL }, new BigInteger(new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff }), UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"))));
        }
    }

    protected static class URIProvider implements ArgumentsProvider {
        protected record URIHolder(String string, URI uri) {
        }

        public URIProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws URISyntaxException {
            return Stream.of(Arguments.argumentSet("http://example.com", new URIProvider.URIHolder("http://example.com", new URI("http://example.com"))), Arguments.argumentSet("https://user:password@www.example.com/uri?query=string#element", new URIProvider.URIHolder("https://user:password@www.example.com/uri?query=string#element", new URI("https://user:password@www.example.com/uri?query=string#element"))));
        }
    }

    @SuppressWarnings("deprecation")
    protected static class URLProvider implements ArgumentsProvider {
        protected record URLHolder(String string, URL url) {
        }

        public URLProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws MalformedURLException {
            return Stream.of(Arguments.argumentSet("http://example.com", new URLProvider.URLHolder("http://example.com", new URL("http://example.com"))), Arguments.argumentSet("https://user:password@www.example.com/url?query=string#element", new URLProvider.URLHolder("https://user:password@www.example.com/url?query=string#element", new URL("https://user:password@www.example.com/url?query=string#element"))));
        }
    }

    protected static class PathProvider implements ArgumentsProvider {
        protected record PathHolder(String string, Path path) {
        }

        public PathProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("/a/silly/path", new PathHolder("/a/silly/path", Path.of("/a/silly/path"))), Arguments.argumentSet("/big/scary/path/with spaces", new PathHolder("/big/scary/path/with spaces", Path.of("/big/scary/path/with spaces"))), Arguments.argumentSet("C:\\a\\silly\\path\\", new PathHolder("C:\\a\\silly\\path\\", Path.of("C:\\a\\silly\\path\\"))), Arguments.argumentSet("C:\\big\\scary\\path\\with spaces\\", new PathHolder("C:\\big\\scary\\path\\with spaces\\", Path.of("C:\\big\\scary\\path\\with spaces\\"))));
        }
    }

    protected static class FileProvider implements ArgumentsProvider {
        protected record FileHolder(String string, File file) {
        }

        public FileProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("/a/silly/path", new FileHolder("/a/silly/path", new File("/a/silly/path"))), Arguments.argumentSet("/big/scary/path/with spaces", new FileHolder("/big/scary/path/with spaces", new File("/big/scary/path/with spaces"))), Arguments.argumentSet("C:\\a\\silly\\path\\", new FileHolder("C:\\a\\silly\\path\\", new File("C:\\a\\silly\\path\\"))), Arguments.argumentSet("C:\\big\\scary\\path\\with spaces\\", new FileHolder("C:\\big\\scary\\path\\with spaces\\", new File("C:\\big\\scary\\path\\with spaces\\"))));
        }
    }

    protected static class InetAddressProvider implements ArgumentsProvider {
        protected record InetAddressHolder(String smallString, String fullString, BigInteger bigInteger, InetAddress inetAddress) {
        }

        public InetAddressProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws UnknownHostException {
            return Stream.of(Arguments.argumentSet("example.com", new InetAddressHolder("example.com", "example.com", null, InetAddress.getByName("example.com"))), Arguments.argumentSet("192.168.0.1", new InetAddressHolder("192.168.0.1", "192.168.000.001", new BigInteger(new byte[] { (byte) 0x00, (byte) 0xc0, (byte) 0xa8, (byte) 0x00, (byte) 0x01 }), InetAddress.getByName("192.168.0.1"))), Arguments.argumentSet("2001:db8:35fa::ab38:ffff", new InetAddressHolder("2001:db8:35fa::ab38:ffff", "2001:0db8:35fa:0000:0000:0000:ab38:ffff", new BigInteger(new byte[] { (byte) 0x01, (byte) 0x20, (byte) 0x01, (byte) 0x0d, (byte) 0xb8, (byte) 0x35, (byte) 0xfa, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xab, (byte) 0x38, (byte) 0xff, (byte) 0xff }), InetAddress.getByName("2001:db8:35fa::ab38:ffff"))), Arguments.argumentSet("::1", new InetAddressHolder("::1", "0000:0000:0000:0000:0000:0000:0000:0001", new BigInteger(new byte[] { (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 }), InetAddress.getByName("::1"))), Arguments.argumentSet("ffff::", new InetAddressHolder("ffff::", "ffff:0000:0000:0000:0000:0000:0000:0000", new BigInteger(new byte[] { (byte) 0x01, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }), InetAddress.getByName("ffff::"))));
        }
    }

    protected static class InetSocketAddressProvider implements ArgumentsProvider {
        protected record InetSocketAddressHolder(String smallString, String fullString, BigInteger bigInteger, InetSocketAddress inetSocketAddress) {
        }

        public InetSocketAddressProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws UnknownHostException {
            return Stream.of(Arguments.argumentSet("example.com:8000", new InetSocketAddressHolder("example.com:8000", "example.com:8000", null, new InetSocketAddress(InetAddress.getByName("example.com"), 8000))), Arguments.argumentSet("192.168.0.1:8000", new InetSocketAddressHolder("192.168.0.1:8000", "192.168.000.001:8000", new BigInteger(new byte[] { (byte) 0x1f, (byte) 0x40, (byte) 0xc0, (byte) 0xa8, (byte) 0x00, (byte) 0x01 }), new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 8000))), Arguments.argumentSet("[2001:db8:35fa::ab38:ffff]:8000", new InetSocketAddressHolder("[2001:db8:35fa::ab38:ffff]:8000", "[2001:0db8:35fa:0000:0000:0000:ab38:ffff]:8000", new BigInteger(new byte[] { (byte) 0x1f, (byte) 0x40, (byte) 0x01, (byte) 0x20, (byte) 0x01, (byte) 0x0d, (byte) 0xb8, (byte) 0x35, (byte) 0xfa, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xab, (byte) 0x38, (byte) 0xff, (byte) 0xff }), new InetSocketAddress(InetAddress.getByName("2001:db8:35fa::ab38:ffff"), 8000))), Arguments.argumentSet("[::1]:8000", new InetSocketAddressHolder("[::1]:8000", "[0000:0000:0000:0000:0000:0000:0000:0001]:8000", new BigInteger(new byte[] { (byte) 0x1f, (byte) 0x40, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 }), new InetSocketAddress(InetAddress.getByName("::1"), 8000))), Arguments.argumentSet("[ffff::]:8000", new InetSocketAddressHolder("[ffff::]:8000", "[ffff:0000:0000:0000:0000:0000:0000:0000]:8000", new BigInteger(new byte[] { (byte) 0x1f, (byte) 0x40, (byte) 0x01, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 }), new InetSocketAddress(InetAddress.getByName("ffff::"), 8000))));
        }
    }

    protected static class CalendarProvider implements ArgumentsProvider {
        protected record CalendarHolder(String defaultString, String isoDateTimeString, String isoInstantString, long number, Calendar calendar) {
        }

        public CalendarProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("2024-07-04T12:00:00Z", new CalendarProvider.CalendarHolder("July 04 2024 @ 07:00:00.000 AM -05:00", "2024-07-04T07:00:00-05:00", "2024-07-04T12:00:00Z", 1720094400000L, getCalendar(1720094400000L))), Arguments.argumentSet("1970-01-01T00:00:00Z", new CalendarProvider.CalendarHolder("December 31 1969 @ 07:00:00.000 PM -05:00", "1969-12-31T19:00:00-05:00", "1970-01-01T00:00:00Z", 0L, getCalendar(0L))));
        }

        private static Calendar getCalendar(long instant) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(instant);
            return calendar;
        }
    }

    protected static class DateProvider implements ArgumentsProvider {
        protected record DateHolder(String defaultString, String isoDateTimeString, String isoInstantString, long number, Date date) {
        }

        public DateProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("2024-07-04T12:00:00Z", new DateProvider.DateHolder("July 04 2024 @ 07:00:00.000 AM -05:00", "2024-07-04T07:00:00-05:00", "2024-07-04T12:00:00Z", 1720094400000L, getDate(1720094400000L))), Arguments.argumentSet("1970-01-01T00:00:00Z", new DateProvider.DateHolder("December 31 1969 @ 07:00:00.000 PM -05:00", "1969-12-31T19:00:00-05:00", "1970-01-01T00:00:00Z", 0L, getDate(0L))));
        }

        private static Date getDate(long instant) {
            return new Date(instant);
        }
    }

    protected static class InstantProvider implements ArgumentsProvider {
        protected record InstantHolder(String defaultString, String isoInstantTimeString, String isoInstantString, long number, Instant instant) {
        }

        public InstantProvider() {
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(Arguments.argumentSet("2024-07-04T12:00:00Z", new InstantProvider.InstantHolder("July 04 2024 @ 07:00:00.000 AM -05:00", "2024-07-04T07:00:00-05:00", "2024-07-04T12:00:00Z", 1720094400000L, getInstant(1720094400000L))), Arguments.argumentSet("1970-01-01T00:00:00Z", new InstantProvider.InstantHolder("December 31 1969 @ 07:00:00.000 PM -05:00", "1969-12-31T19:00:00-05:00", "1970-01-01T00:00:00Z", 0L, getInstant(0L))));
        }

        private static Instant getInstant(long instant) {
            return Instant.ofEpochMilli(instant);
        }
    }

    public DefaultTypeProcessorsTest() {
    }

    @ParameterizedTest(name="toElement_StringBuilder_String - {argumentSetName}")
    @Tag("stringBuilder")
    @Tag("toElement")
    @ArgumentsSource(StringBuilderProvider.class)
    void toElement_StringBuilder_String(StringBuilderProvider.StringBuilderHolder holder) {
        ParsedElement element = typeProcessorsMode1.STRING_BUILDER_P.toElement(holder.stringBuilder());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_StringBuilder_Null")
    @Tag("stringBuilder")
    @Tag("toElement")
    void toElement_StringBuilder_Null() {
        ParsedElement element = typeProcessorsMode1.STRING_BUILDER_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_StringBuilder_String - {argumentSetName}")
    @Tag("stringBuilder")
    @Tag("toObject")
    @ArgumentsSource(StringBuilderProvider.class)
    void toObject_StringBuilder_String(StringBuilderProvider.StringBuilderHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.STRING_BUILDER_P.toObject(element);
        assertInstanceOf(StringBuilder.class, obj);
        assertEquals(holder.string(), obj.toString());
    }

    @Test
    @DisplayName("toObject_StringBuilder_Null")
    @Tag("stringBuilder")
    @Tag("toObject")
    void toObject_StringBuilder_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.STRING_BUILDER_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_StringBuffer_String - {argumentSetName}")
    @Tag("stringBuffer")
    @Tag("toElement")
    @ArgumentsSource(StringBufferProvider.class)
    void toElement_StringBuffer_String(StringBufferProvider.StringBufferHolder holder) {
        ParsedElement element = typeProcessorsMode1.STRING_BUFFER_P.toElement(holder.stringBuffer());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_StringBuffer_Null")
    @Tag("stringBuffer")
    @Tag("toElement")
    void toElement_StringBuffer_Null() {
        ParsedElement element = typeProcessorsMode1.STRING_BUFFER_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_StringBuffer_String - {argumentSetName}")
    @Tag("stringBuffer")
    @Tag("toObject")
    @ArgumentsSource(StringBufferProvider.class)
    void toObject_StringBuffer_String(StringBufferProvider.StringBufferHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.STRING_BUFFER_P.toObject(element);
        assertInstanceOf(StringBuffer.class, obj);
        assertEquals(holder.string(), obj.toString());
    }

    @Test
    @DisplayName("toObject_StringBuffer_Null")
    @Tag("stringBuffer")
    @Tag("toObject")
    void toObject_StringBuffer_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.STRING_BUFFER_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_UUID_String - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_String(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode1.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.getString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_UUID_ByteArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_ByteArray(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode2.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedArray.class, element);
        assertEquals(16, element.asArray().getSize());
        assertEquals(List.copyOf(holder.getBytesArray().getValues()), List.copyOf(element.asArray().getValues()));
    }

    @ParameterizedTest(name="toElement_UUID_ShortArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_ShortArray(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode3.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedArray.class, element);
        assertEquals(8, element.asArray().getSize());
        assertEquals(List.copyOf(holder.getShortsArray().getValues()), List.copyOf(element.asArray().getValues()));
    }

    @ParameterizedTest(name="toElement_UUID_IntegerArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_IntegerArray(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode4.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedArray.class, element);
        assertEquals(4, element.asArray().getSize());
        assertEquals(List.copyOf(holder.getIntegersArray().getValues()), List.copyOf(element.asArray().getValues()));
    }

    @ParameterizedTest(name="toElement_UUID_LongArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_LongArray(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode5.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedArray.class, element);
        assertEquals(2, element.asArray().getSize());
        assertEquals(List.copyOf(holder.getLongsArray().getValues()), List.copyOf(element.asArray().getValues()));
    }

    @ParameterizedTest(name="toElement_UUID_BigInt - {argumentSetName}")
    @Tag("uuid")
    @Tag("toElement")
    @ArgumentsSource(UUIDProvider.class)
    void toElement_UUID_BigInt(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = typeProcessorsMode6.UUID_P.toElement(holder.getUUID());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.BIGINTEGER, element.asPrimitive().getType());
        assertEquals(holder.getBigInteger(), element.asPrimitive().asBigInteger());
    }

    @Test
    @DisplayName("toElement_UUID_Null")
    @Tag("uuid")
    @Tag("toElement")
    void toElement_UUID_Null() {
        ParsedElement element = typeProcessorsMode1.UUID_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_UUID_String - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_String(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.getString());
        Object obj = typeProcessorsMode1.UUID_P.toObject(element);
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @ParameterizedTest(name="toObject_UUID_ByteArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_ByteArray(UUIDProvider.UUIDHolder holder) {
        Object obj = typeProcessorsMode2.UUID_P.toObject(holder.getBytesArray());
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @ParameterizedTest(name="toObject_UUID_ShortArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_ShortArray(UUIDProvider.UUIDHolder holder) {
        Object obj = typeProcessorsMode3.UUID_P.toObject(holder.getShortsArray());
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @ParameterizedTest(name="toObject_UUID_IntegerArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_IntegerArray(UUIDProvider.UUIDHolder holder) {
        Object obj = typeProcessorsMode4.UUID_P.toObject(holder.getIntegersArray());
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @ParameterizedTest(name="toObject_UUID_LongArray - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_LongArray(UUIDProvider.UUIDHolder holder) {
        Object obj = typeProcessorsMode5.UUID_P.toObject(holder.getLongsArray());
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @ParameterizedTest(name="toObject_UUID_BigInteger - {argumentSetName}")
    @Tag("uuid")
    @Tag("toObject")
    @ArgumentsSource(UUIDProvider.class)
    void toObject_UUID_BigInteger(UUIDProvider.UUIDHolder holder) {
        ParsedElement element = ParsedPrimitive.fromBigInteger(holder.getBigInteger());
        Object obj = typeProcessorsMode6.UUID_P.toObject(element);
        assertInstanceOf(UUID.class, obj);
        assertEquals(holder.getUUID(), obj);
    }

    @Test
    @DisplayName("toObject_UUID_Null")
    @Tag("uuid")
    @Tag("toObject")
    void toObject_UUID_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.UUID_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_URI_String - {argumentSetName}")
    @Tag("uri")
    @Tag("toElement")
    @ArgumentsSource(URIProvider.class)
    void toElement_URI_String(URIProvider.URIHolder holder) {
        ParsedElement element = typeProcessorsMode1.URI_P.toElement(holder.uri());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_URI_Null")
    @Tag("uri")
    @Tag("toElement")
    void toElement_URI_Null() {
        ParsedElement element = typeProcessorsMode1.URI_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_URI_String - {argumentSetName}")
    @Tag("uri")
    @Tag("toObject")
    @ArgumentsSource(URIProvider.class)
    void toObject_URI_String(URIProvider.URIHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.URI_P.toObject(element);
        assertInstanceOf(URI.class, obj);
        assertEquals(holder.uri(), obj);
    }

    @Test
    @DisplayName("toObject_URI_Null")
    @Tag("uri")
    @Tag("toObject")
    void toObject_URI_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.URI_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_URL_String - {argumentSetName}")
    @Tag("url")
    @Tag("toElement")
    @ArgumentsSource(URLProvider.class)
    void toElement_URL_String(URLProvider.URLHolder holder) {
        ParsedElement element = typeProcessorsMode1.URL_P.toElement(holder.url());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_URL_Null")
    @Tag("url")
    @Tag("toElement")
    void toElement_URL_Null() {
        ParsedElement element = typeProcessorsMode1.URL_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_URL_String - {argumentSetName}")
    @Tag("url")
    @Tag("toObject")
    @ArgumentsSource(URLProvider.class)
    void toObject_URL_String(URLProvider.URLHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.URL_P.toObject(element);
        assertInstanceOf(URL.class, obj);
        assertEquals(holder.url(), obj);
    }

    @Test
    @DisplayName("toObject_URL_Null")
    @Tag("url")
    @Tag("toObject")
    void toObject_URL_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.URI_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_Path_String - {argumentSetName}")
    @Tag("path")
    @Tag("toElement")
    @ArgumentsSource(PathProvider.class)
    void toElement_Path_String(PathProvider.PathHolder holder) {
        ParsedElement element = typeProcessorsMode1.PATH_P.toElement(holder.path());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_Path_Null")
    @Tag("path")
    @Tag("toElement")
    void toElement_Path_Null() {
        ParsedElement element = typeProcessorsMode1.PATH_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_Path_String - {argumentSetName}")
    @Tag("path")
    @Tag("toObject")
    @ArgumentsSource(PathProvider.class)
    void toObject_Path_String(PathProvider.PathHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.PATH_P.toObject(element);
        assertInstanceOf(Path.class, obj);
        assertEquals(holder.path(), obj);
    }

    @Test
    @DisplayName("toObject_Path_Null")
    @Tag("path")
    @Tag("toObject")
    void toObject_Path_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.PATH_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_File_String - {argumentSetName}")
    @Tag("file")
    @Tag("toElement")
    @ArgumentsSource(FileProvider.class)
    void toElement_File_String(FileProvider.FileHolder holder) {
        ParsedElement element = typeProcessorsMode1.FILE_P.toElement(holder.file());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.string(), element.asPrimitive().asString());
    }

    @Test
    @DisplayName("toElement_File_Null")
    @Tag("file")
    @Tag("toElement")
    void toElement_File_Null() {
        ParsedElement element = typeProcessorsMode1.FILE_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_File_String - {argumentSetName}")
    @Tag("file")
    @Tag("toObject")
    @ArgumentsSource(FileProvider.class)
    void toObject_File_String(FileProvider.FileHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.string());
        Object obj = typeProcessorsMode1.FILE_P.toObject(element);
        assertInstanceOf(File.class, obj);
        assertEquals(holder.file(), obj);
    }

    @Test
    @DisplayName("toObject_File_Null")
    @Tag("file")
    @Tag("toObject")
    void toObject_File_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.FILE_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_InetAddress_String - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toElement")
    @ArgumentsSource(InetAddressProvider.class)
    void toElement_InetAddress_String(InetAddressProvider.InetAddressHolder holder) {
        ParsedElement element = typeProcessorsMode1.INET_ADDRESS_P.toElement(holder.inetAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.smallString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_InetAddress_String_Fill - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toElement")
    @ArgumentsSource(InetAddressProvider.class)
    void toElement_InetAddress_String_Fill(InetAddressProvider.InetAddressHolder holder) {
        ParsedElement element = typeProcessorsMode2.INET_ADDRESS_P.toElement(holder.inetAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.fullString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_InetAddress_Number - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toElement")
    @ArgumentsSource(InetAddressProvider.class)
    void toElement_InetAddress_Number(InetAddressProvider.InetAddressHolder holder) {
        if (holder.bigInteger() == null) {
            return; // TODO Skip
        }

        ParsedElement element = typeProcessorsMode3.INET_ADDRESS_P.toElement(holder.inetAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.BIGINTEGER, element.asPrimitive().getType());
        assertEquals(holder.bigInteger(), element.asPrimitive().asBigInteger());
    }

    @Test
    @DisplayName("toElement_InetAddress_Null")
    @Tag("inetAddress")
    @Tag("toElement")
    void toElement_InetAddress_Null() {
        ParsedElement element = typeProcessorsMode1.INET_ADDRESS_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_InetAddress_String - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toObject")
    @ArgumentsSource(InetAddressProvider.class)
    void toObject_InetAddress_String(InetAddressProvider.InetAddressHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.smallString());
        Object obj = typeProcessorsMode1.INET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetAddress.class, obj);
        assertEquals(holder.inetAddress(), obj);
    }

    @ParameterizedTest(name="toObject_InetAddress_String_Fill - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toObject")
    @ArgumentsSource(InetAddressProvider.class)
    void toObject_InetAddress_String_Fill(InetAddressProvider.InetAddressHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.fullString());
        Object obj = typeProcessorsMode2.INET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetAddress.class, obj);
        assertEquals(holder.inetAddress(), obj);
    }

    @ParameterizedTest(name="toObject_InetAddress_Number - {argumentSetName}")
    @Tag("inetAddress")
    @Tag("toObject")
    @ArgumentsSource(InetAddressProvider.class)
    void toObject_InetAddress_Number(InetAddressProvider.InetAddressHolder holder) {
        if (holder.bigInteger() == null) {
            return; // TODO Skip
        }

        ParsedElement element = ParsedPrimitive.fromBigInteger(holder.bigInteger());
        Object obj = typeProcessorsMode3.INET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetAddress.class, obj);
        assertEquals(holder.inetAddress(), obj);
    }

    @Test
    @DisplayName("toObject_InetAddress_Null")
    @Tag("inetAddress")
    @Tag("toObject")
    void toObject_InetAddress_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.INET_ADDRESS_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_InetSocketAddress_String - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toElement")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toElement_InetSocketAddress_String(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        ParsedElement element = typeProcessorsMode1.INETSOCKET_ADDRESS_P.toElement(holder.inetSocketAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.smallString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_InetSocketAddress_String_Fill - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toElement")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toElement_InetSocketAddress_String_Fill(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        ParsedElement element = typeProcessorsMode2.INETSOCKET_ADDRESS_P.toElement(holder.inetSocketAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.fullString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_InetSocketAddress_Number - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toElement")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toElement_InetSocketAddress_Number(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        if (holder.bigInteger() == null) {
            return; // TODO Skip
        }

        ParsedElement element = typeProcessorsMode3.INETSOCKET_ADDRESS_P.toElement(holder.inetSocketAddress());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.BIGINTEGER, element.asPrimitive().getType());
        assertEquals(holder.bigInteger(), element.asPrimitive().asBigInteger());
    }

    @Test
    @DisplayName("toElement_InetSocketAddress_Null")
    @Tag("inetSocketAddress")
    @Tag("toElement")
    void toElement_InetSocketAddress_Null() {
        ParsedElement element = typeProcessorsMode1.INETSOCKET_ADDRESS_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_InetSocketAddress_String - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toObject")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toObject_InetSocketAddress_String(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.smallString());
        Object obj = typeProcessorsMode1.INETSOCKET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetSocketAddress.class, obj);
        assertEquals(holder.inetSocketAddress(), obj);
    }

    @ParameterizedTest(name="toObject_InetSocketAddress_String_Fill - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toObject")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toObject_InetSocketAddress_String_Fill(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.fullString());
        Object obj = typeProcessorsMode2.INETSOCKET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetSocketAddress.class, obj);
        assertEquals(holder.inetSocketAddress(), obj);
    }

    @ParameterizedTest(name="toObject_InetSocketAddress_Number - {argumentSetName}")
    @Tag("inetSocketAddress")
    @Tag("toObject")
    @ArgumentsSource(InetSocketAddressProvider.class)
    void toObject_InetSocketAddress_Number(InetSocketAddressProvider.InetSocketAddressHolder holder) {
        if (holder.bigInteger() == null) {
            return; // TODO Skip
        }

        ParsedElement element = ParsedPrimitive.fromBigInteger(holder.bigInteger());
        Object obj = typeProcessorsMode3.INETSOCKET_ADDRESS_P.toObject(element);
        assertInstanceOf(InetSocketAddress.class, obj);
        assertEquals(holder.inetSocketAddress(), obj);
    }

    @Test
    @DisplayName("toObject_InetSocketAddress_Null")
    @Tag("inetSocketAddress")
    @Tag("toObject")
    void toObject_InetSocketAddress_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.INETSOCKET_ADDRESS_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_Calendar_String_DEFAULT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toElement")
    @ArgumentsSource(CalendarProvider.class)
    void toElement_Calendar_String_DEFAULT(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = typeProcessorsMode1.CALENDAR_P.toElement(holder.calendar());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.defaultString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Calendar_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("calendar")
    @Tag("toElement")
    @ArgumentsSource(CalendarProvider.class)
    void toElement_Calendar_String_ISO_DATE_TIME(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = typeProcessorsMode2.CALENDAR_P.toElement(holder.calendar());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoDateTimeString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Calendar_String_ISO_INSTANT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toElement")
    @ArgumentsSource(CalendarProvider.class)
    void toElement_Calendar_String_ISO_INSTANT(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = typeProcessorsMode3.CALENDAR_P.toElement(holder.calendar());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoInstantString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Calendar_Number - {argumentSetName}")
    @Tag("calendar")
    @Tag("toElement")
    @ArgumentsSource(CalendarProvider.class)
    void toElement_Calendar_Number(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = typeProcessorsMode4.CALENDAR_P.toElement(holder.calendar());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.LONG, element.asPrimitive().getType());
        assertEquals(holder.number(), element.asPrimitive().asLong());
    }

    @Test
    @DisplayName("toElement_Calendar_Null")
    @Tag("calendar")
    @Tag("toElement")
    void toElement_Calendar_Null() {
        ParsedElement element = typeProcessorsMode1.CALENDAR_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_Calendar_String_DEFAULT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(CalendarProvider.class)
    void toObject_Calendar_String_DEFAULT(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.defaultString());
        Object obj = typeProcessorsMode1.CALENDAR_P.toObject(element);
        assertInstanceOf(Calendar.class, obj);
        assertEquals(holder.calendar(), obj);
    }

    @ParameterizedTest(name="toObject_Calendar_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(CalendarProvider.class)
    void toObject_Calendar_String_ISO_DATE_TIME(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoDateTimeString());
        Object obj = typeProcessorsMode2.CALENDAR_P.toObject(element);
        assertInstanceOf(Calendar.class, obj);
        assertEquals(holder.calendar(), obj);
    }

    @ParameterizedTest(name="toObject_Calendar_String_ISO_INSTANT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(CalendarProvider.class)
    void toObject_Calendar_String_ISO_INSTANT(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoInstantString());
        Object obj = typeProcessorsMode3.CALENDAR_P.toObject(element);
        assertInstanceOf(Calendar.class, obj);
        assertEquals(holder.calendar(), obj);
    }

    @ParameterizedTest(name="toObject_Calendar_Number - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(CalendarProvider.class)
    void toObject_Calendar_Number(CalendarProvider.CalendarHolder holder) {
        ParsedElement element = ParsedPrimitive.fromLong(holder.number());
        Object obj = typeProcessorsMode4.CALENDAR_P.toObject(element);
        assertInstanceOf(Calendar.class, obj);
        assertEquals(holder.calendar(), obj);
    }

    @Test
    @DisplayName("toObject_Calendar_Null")
    @Tag("calendar")
    @Tag("toObject")
    void toObject_Calendar_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.CALENDAR_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_Date_String_DEFAULT - {argumentSetName}")
    @Tag("date")
    @Tag("toElement")
    @ArgumentsSource(DateProvider.class)
    void toElement_Date_String_DEFAULT(DateProvider.DateHolder holder) {
        ParsedElement element = typeProcessorsMode1.DATE_P.toElement(holder.date());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.defaultString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Date_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("date")
    @Tag("toElement")
    @ArgumentsSource(DateProvider.class)
    void toElement_Date_String_ISO_DATE_TIME(DateProvider.DateHolder holder) {
        ParsedElement element = typeProcessorsMode2.DATE_P.toElement(holder.date());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoDateTimeString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Date_String_ISO_INSTANT - {argumentSetName}")
    @Tag("date")
    @Tag("toElement")
    @ArgumentsSource(DateProvider.class)
    void toElement_Date_String_ISO_INSTANT(DateProvider.DateHolder holder) {
        ParsedElement element = typeProcessorsMode3.DATE_P.toElement(holder.date());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoInstantString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Date_Number - {argumentSetName}")
    @Tag("date")
    @Tag("toElement")
    @ArgumentsSource(DateProvider.class)
    void toElement_Date_Number(DateProvider.DateHolder holder) {
        ParsedElement element = typeProcessorsMode4.DATE_P.toElement(holder.date());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.LONG, element.asPrimitive().getType());
        assertEquals(holder.number(), element.asPrimitive().asLong());
    }

    @Test
    @DisplayName("toElement_Date_Null")
    @Tag("date")
    @Tag("toElement")
    void toElement_Date_Null() {
        ParsedElement element = typeProcessorsMode1.DATE_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_Date_String_DEFAULT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(DateProvider.class)
    void toObject_Date_String_DEFAULT(DateProvider.DateHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.defaultString());
        Object obj = typeProcessorsMode1.DATE_P.toObject(element);
        assertInstanceOf(Date.class, obj);
        assertEquals(holder.date(), obj);
    }

    @ParameterizedTest(name="toObject_Date_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(DateProvider.class)
    void toObject_Date_String_ISO_DATE_TIME(DateProvider.DateHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoDateTimeString());
        Object obj = typeProcessorsMode2.DATE_P.toObject(element);
        assertInstanceOf(Date.class, obj);
        assertEquals(holder.date(), obj);
    }

    @ParameterizedTest(name="toObject_Date_String_ISO_INSTANT - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(DateProvider.class)
    void toObject_Date_String_ISO_INSTANT(DateProvider.DateHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoInstantString());
        Object obj = typeProcessorsMode3.DATE_P.toObject(element);
        assertInstanceOf(Date.class, obj);
        assertEquals(holder.date(), obj);
    }

    @ParameterizedTest(name="toObject_Date_Number - {argumentSetName}")
    @Tag("calendar")
    @Tag("toObject")
    @ArgumentsSource(DateProvider.class)
    void toObject_Date_Number(DateProvider.DateHolder holder) {
        ParsedElement element = ParsedPrimitive.fromLong(holder.number());
        Object obj = typeProcessorsMode4.DATE_P.toObject(element);
        assertInstanceOf(Date.class, obj);
        assertEquals(holder.date(), obj);
    }

    @Test
    @DisplayName("toObject_Date_Null")
    @Tag("calendar")
    @Tag("toObject")
    void toObject_Date_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.DATE_P.toObject(element);
        assertNull(obj);
    }

    @ParameterizedTest(name="toElement_Instant_String_DEFAULT - {argumentSetName}")
    @Tag("instant")
    @Tag("toElement")
    @ArgumentsSource(InstantProvider.class)
    void toElement_Instant_String_DEFAULT(InstantProvider.InstantHolder holder) {
        ParsedElement element = typeProcessorsMode1.INSTANT_P.toElement(holder.instant());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.defaultString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Instant_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("instant")
    @Tag("toElement")
    @ArgumentsSource(InstantProvider.class)
    void toElement_Instant_String_ISO_DATE_TIME(InstantProvider.InstantHolder holder) {
        ParsedElement element = typeProcessorsMode2.INSTANT_P.toElement(holder.instant());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoInstantTimeString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Instant_String_ISO_INSTANT - {argumentSetName}")
    @Tag("instant")
    @Tag("toElement")
    @ArgumentsSource(InstantProvider.class)
    void toElement_Instant_String_ISO_INSTANT(InstantProvider.InstantHolder holder) {
        ParsedElement element = typeProcessorsMode3.INSTANT_P.toElement(holder.instant());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.STRING, element.asPrimitive().getType());
        assertEquals(holder.isoInstantString(), element.asPrimitive().asString());
    }

    @ParameterizedTest(name="toElement_Instant_Number - {argumentSetName}")
    @Tag("instant")
    @Tag("toElement")
    @ArgumentsSource(InstantProvider.class)
    void toElement_Instant_Number(InstantProvider.InstantHolder holder) {
        ParsedElement element = typeProcessorsMode4.INSTANT_P.toElement(holder.instant());
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.LONG, element.asPrimitive().getType());
        assertEquals(holder.number(), element.asPrimitive().asLong());
    }

    @Test
    @DisplayName("toElement_Instant_Null")
    @Tag("instant")
    @Tag("toElement")
    void toElement_Instant_Null() {
        ParsedElement element = typeProcessorsMode1.INSTANT_P.toElement(null);
        assertInstanceOf(ParsedPrimitive.class, element);
        assertEquals(ParsedPrimitive.PrimitiveType.NULL, element.asPrimitive().getType());
    }

    @ParameterizedTest(name="toObject_Instant_String_DEFAULT - {argumentSetName}")
    @Tag("instant")
    @Tag("toObject")
    @ArgumentsSource(InstantProvider.class)
    void toObject_Instant_String_DEFAULT(InstantProvider.InstantHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.defaultString());
        Object obj = typeProcessorsMode1.INSTANT_P.toObject(element);
        assertInstanceOf(Instant.class, obj);
        assertEquals(holder.instant(), obj);
    }

    @ParameterizedTest(name="toObject_Instant_String_ISO_DATE_TIME - {argumentSetName}")
    @Tag("instant")
    @Tag("toObject")
    @ArgumentsSource(InstantProvider.class)
    void toObject_Instant_String_ISO_DATE_TIME(InstantProvider.InstantHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoInstantTimeString());
        Object obj = typeProcessorsMode2.INSTANT_P.toObject(element);
        assertInstanceOf(Instant.class, obj);
        assertEquals(holder.instant(), obj);
    }

    @ParameterizedTest(name="toObject_Instant_String_ISO_INSTANT - {argumentSetName}")
    @Tag("instant")
    @Tag("toObject")
    @ArgumentsSource(InstantProvider.class)
    void toObject_Instant_String_ISO_INSTANT(InstantProvider.InstantHolder holder) {
        ParsedElement element = ParsedPrimitive.fromString(holder.isoInstantString());
        Object obj = typeProcessorsMode3.INSTANT_P.toObject(element);
        assertInstanceOf(Instant.class, obj);
        assertEquals(holder.instant(), obj);
    }

    @ParameterizedTest(name="toObject_Instant_Number - {argumentSetName}")
    @Tag("instant")
    @Tag("toObject")
    @ArgumentsSource(InstantProvider.class)
    void toObject_Instant_Number(InstantProvider.InstantHolder holder) {
        ParsedElement element = ParsedPrimitive.fromLong(holder.number());
        Object obj = typeProcessorsMode4.INSTANT_P.toObject(element);
        assertInstanceOf(Instant.class, obj);
        assertEquals(holder.instant(), obj);
    }

    @Test
    @DisplayName("toObject_Instant_Null")
    @Tag("instant")
    @Tag("toObject")
    void toObject_Instant_Null() {
        ParsedElement element = ParsedPrimitive.fromNull();
        Object obj = typeProcessorsMode1.INSTANT_P.toObject(element);
        assertNull(obj);
    }
}