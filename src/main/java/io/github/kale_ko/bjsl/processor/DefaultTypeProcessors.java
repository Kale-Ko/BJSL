package io.github.kale_ko.bjsl.processor;

import io.github.kale_ko.bjsl.elements.ParsedArray;
import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import java.io.File;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A collection of type processors for common java types that are enabled by default
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public final class DefaultTypeProcessors {
    /**
     * The default instance of the default type processors. Uses {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors.Options#DEFAULT}
     *
     * @see io.github.kale_ko.bjsl.processor.DefaultTypeProcessors.Options#DEFAULT
     * @since 2.0.0
     */
    public static final @NotNull DefaultTypeProcessors DEFAULT = new DefaultTypeProcessors(Options.DEFAULT);

    /**
     * An instance of options to pass to the default type processors
     *
     * @since 2.0.0
     */
    public static class Options {
        /**
         * The default instance of the options
         *
         * @since 2.0.0
         */
        public static final @NotNull Options DEFAULT = new Options(Options.UUIDMode.STRING, Options.InetAddressMode.STRING, false, Options.DateMode.STRING, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()));

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#UUID_P} type processor
         *
         * @since 2.0.0
         */
        public enum UUIDMode {
            /**
             * Stored as a string (e.g. "e4ec171a-665d-43fa-946e-852cc67cc590")
             */
            STRING,
            /**
             * Stored as a byte array (e.g. [-28, -20, 23, 26, 102, 93, 67, -6, -108, 110, -123, 44, -58, 124, -59, -112])
             */
            BYTE_ARRAY,
            /**
             * Stored as a short array (e.g. [-6932, 5914, 26205, 17402, -27538, -31444, -14724, -14960])
             */
            SHORT_ARRAY,
            /**
             * Stored as an int array (e.g. [-454289638, 1717388282, -1804696276, -964901488])
             */
            INT_ARRAY,
            /**
             * Stored as a long array (e.g. [-1951159136404290566L, -7751111481302923888L])
             */
            LONG_ARRAY,
            /**
             * Stored as a big integer (e.g. -35992533236330093637743511526032226928)
             */
            NUMBER
        }

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INET_ADDRESS_P} and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INETSOCKET_ADDRESS_P} type processors
         *
         * @since 2.0.0
         */
        public enum InetAddressMode {
            /**
             * Stored as a string (e.g. "192.168.0.1", "192.168.0.1:8000")
             */
            STRING,
            /**
             * Stored as a big integer (e.g. 34362970603521)
             */
            NUMBER
        }

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @since 2.0.0
         */
        public enum DateMode {
            /**
             * Stored as a string (e.g. "July 04 2024 @ 07:00:00.000 AM -05:00", "2024-07-04T07:00:00-05:00", "2024-07-04T12:00:00Z")
             */
            STRING,
            /**
             * Stored as a long (e.g. 1720094400000)
             */
            NUMBER
        }

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#UUID_P} type processor
         *
         * @since 2.0.0
         */
        protected final @NotNull UUIDMode uuidMode;

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INET_ADDRESS_P} and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INETSOCKET_ADDRESS_P} type processors
         *
         * @since 2.0.0
         */
        protected final @NotNull InetAddressMode inetAddressMode;

        /**
         * Weather or not to fill in InetAddresses (e.g. 127.0.0.1 &#x2D;&#x3E; 127.000.000.001, 2001:db8::1 &#x2D;&#x3E; 2001:0db8:0000:0000:0000:0000:0000:0001)
         *
         * @since 2.0.0
         */
        protected final boolean fillAddresses;

        /**
         * A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @since 2.0.0
         */
        protected final @NotNull DateMode dateMode;

        /**
         * The formatter for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         * <p>
         * This is used for parsing and formatting the dates so <b>be careful changing this</b>
         *
         * @since 2.0.0
         */
        protected final @NotNull DateTimeFormatter dateTimeFormatter;

        /**
         * Create an instance of Options
         *
         * @param uuidMode          A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#UUID_P} type processor
         * @param inetAddressMode   A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INET_ADDRESS_P} and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INETSOCKET_ADDRESS_P} type processors
         * @param fillAddresses     Weather or not to fill in InetAddresses (e.g. 127.0.0.1 &#x2D;&#x3E; 127.000.000.001, 2001:db8::1 &#x2D;&#x3E; 2001:0db8:0000:0000:0000:0000:0000:0001)
         * @param dateMode          A mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         * @param dateTimeFormatter The formatter for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @since 2.0.0
         */
        public Options(@NotNull UUIDMode uuidMode, @NotNull InetAddressMode inetAddressMode, boolean fillAddresses, @NotNull DateMode dateMode, @NotNull DateTimeFormatter dateTimeFormatter) {
            this.uuidMode = uuidMode;
            this.inetAddressMode = inetAddressMode;
            this.fillAddresses = fillAddresses;

            this.dateMode = dateMode;
            this.dateTimeFormatter = dateTimeFormatter;
        }

        /**
         * Get the mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#UUID_P} type processor
         *
         * @return The mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#UUID_P} type processor
         *
         * @since 2.0.0
         */
        public @NotNull UUIDMode getUuidMode() {
            return this.uuidMode;
        }

        /**
         * Get the mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INET_ADDRESS_P} and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INETSOCKET_ADDRESS_P} type processors
         *
         * @return The mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INET_ADDRESS_P} and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INETSOCKET_ADDRESS_P} type processors
         *
         * @since 2.0.0
         */
        public @NotNull InetAddressMode getInetAddressMode() {
            return inetAddressMode;
        }

        /**
         * Get weather or not to fill in InetAddresses (e.g. 127.0.0.1 &#x2D;&#x3E; 127.000.000.001, 2001:db8::1 &#x2D;&#x3E; 2001:0db8:0000:0000:0000:0000:0000:0001)
         *
         * @return Weather or not to fill in InetAddresses (e.g. 127.0.0.1 &#x2D;&#x3E; 127.000.000.001, 2001:db8::1 &#x2D;&#x3E; 2001:0db8:0000:0000:0000:0000:0000:0001)
         *
         * @since 2.0.0
         */
        public boolean isFillAddresses() {
            return this.fillAddresses;
        }

        /**
         * Get the mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @return The mode for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @since 2.0.0
         */
        public @NotNull DateMode getDateMode() {
            return this.dateMode;
        }

        /**
         * Get the formatter for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         * <p>
         * This is used for parsing and formatting the dates so <b>be careful changing this</b>
         *
         * @return The formatter for the {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#CALENDAR_P}, {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#DATE_P}, and {@link io.github.kale_ko.bjsl.processor.DefaultTypeProcessors#INSTANT_P} type processors
         *
         * @since 2.0.0
         */
        public @NotNull DateTimeFormatter getDateTimeFormatter() {
            return this.dateTimeFormatter;
        }
    }

    /**
     * The options to pass to the type processors
     *
     * @since 2.0.0
     */
    private final @NotNull Options options;

    /**
     * Create an instance of DefaultTypeProcessors using the given options
     *
     * @param options The options to pass to the type processors
     *
     * @since 2.0.0
     */
    public DefaultTypeProcessors(@NotNull Options options) {
        this.options = options;
    }

    /**
     * A type processor for {@link java.lang.StringBuilder}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor STRING_BUILDER_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof StringBuilder) {
                return ParsedPrimitive.fromString(((StringBuilder) object).toString());
            } else {
                throw new InvalidParameterException("object must be StringBuilder");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                return new StringBuilder(element.asPrimitive().asString());
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.lang.StringBuffer}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor STRING_BUFFER_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof StringBuffer) {
                return ParsedPrimitive.fromString(((StringBuffer) object).toString());
            } else {
                throw new InvalidParameterException("object must be StringBuffer");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                return new StringBuffer(element.asPrimitive().asString());
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.util.UUID}. Converts to and from a ParsedPrimitive string, array, or bigint
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor UUID_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof UUID uuid) {
                switch (options.getUuidMode()) {
                    case STRING -> {
                        return ParsedPrimitive.fromString(uuid.toString());
                    }
                    case BYTE_ARRAY -> {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
                        byteBuffer.putLong(uuid.getMostSignificantBits());
                        byteBuffer.putLong(uuid.getLeastSignificantBits());

                        ParsedArray byteArray = ParsedArray.create();
                        byte[] array = new byte[16];
                        byteBuffer.position(0).get(array);
                        for (byte b : array) {
                            byteArray.add(ParsedPrimitive.fromByte(b));
                        }
                        return byteArray;
                    }
                    case SHORT_ARRAY -> {
                        ByteBuffer shortBuffer = ByteBuffer.wrap(new byte[16]);
                        shortBuffer.putLong(uuid.getMostSignificantBits());
                        shortBuffer.putLong(uuid.getLeastSignificantBits());

                        ParsedArray shortArray = ParsedArray.create();
                        short[] array = new short[8];
                        shortBuffer.position(0).asShortBuffer().get(array);
                        for (short s : array) {
                            shortArray.add(ParsedPrimitive.fromShort(s));
                        }
                        return shortArray;
                    }
                    case INT_ARRAY -> {
                        ByteBuffer intBuffer = ByteBuffer.wrap(new byte[16]);
                        intBuffer.putLong(uuid.getMostSignificantBits());
                        intBuffer.putLong(uuid.getLeastSignificantBits());

                        ParsedArray intArray = ParsedArray.create();
                        int[] array = new int[4];
                        intBuffer.position(0).asIntBuffer().get(array);
                        for (int i : array) {
                            intArray.add(ParsedPrimitive.fromInteger(i));
                        }
                        return intArray;
                    }
                    case LONG_ARRAY -> {
                        ByteBuffer longBuffer = ByteBuffer.wrap(new byte[16]);
                        longBuffer.putLong(uuid.getMostSignificantBits());
                        longBuffer.putLong(uuid.getLeastSignificantBits());

                        ParsedArray longArray = ParsedArray.create();
                        long[] array = new long[2];
                        longBuffer.position(0).asLongBuffer().get(array);
                        for (long l : array) {
                            longArray.add(ParsedPrimitive.fromLong(l));
                        }
                        return longArray;
                    }
                    case NUMBER -> {
                        ByteBuffer bigIntBuffer = ByteBuffer.wrap(new byte[16]);
                        bigIntBuffer.putLong(uuid.getMostSignificantBits());
                        bigIntBuffer.putLong(uuid.getLeastSignificantBits());

                        return ParsedPrimitive.fromBigInteger(new BigInteger(bigIntBuffer.array()));
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be UUID");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isArray()) {
                ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);

                for (ParsedElement subElement : element.asArray().getValues()) {
                    if (subElement.isPrimitive()) {
                        switch (element.asArray().getSize()) {
                            case 16 -> {
                                buffer.put(subElement.asPrimitive().toByte());
                            }
                            case 8 -> {
                                buffer.putShort(subElement.asPrimitive().toShort());
                            }
                            case 4 -> {
                                buffer.putInt(subElement.asPrimitive().toInteger());
                            }
                            case 2 -> {
                                buffer.putLong(subElement.asPrimitive().toLong());
                            }
                            default -> {
                                throw new RuntimeException();
                            }
                        }
                    } else {
                        throw new RuntimeException();
                    }
                }

                buffer = buffer.position(0);

                return new UUID(buffer.getLong(), buffer.getLong());
            } else if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        return UUID.fromString(element.asPrimitive().asString());
                    }
                    case BYTE,
                         SHORT,
                         INTEGER,
                         LONG,
                         BIGINTEGER -> {
                        BigInteger bigInt = element.asPrimitive().toBigInteger();
                        return new UUID(bigInt.shiftRight(64).longValue(), bigInt.longValue());
                    }
                    default -> {
                        throw new InvalidParameterException("object must be Array, BigInt, or String");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be Array, BigInt, or String");
            }
        }
    };

    /**
     * A type processor for {@link java.net.URI}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor URI_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof URI) {
                return ParsedPrimitive.fromString(((URI) object).toString());
            } else {
                throw new InvalidParameterException("object must be URI");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                try {
                    return new URI(element.asPrimitive().asString());
                } catch (URISyntaxException e) {
                    throw new InvalidParameterException("object is an invalid URI");
                }
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.net.URL}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation") public final @NotNull TypeProcessor URL_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof URL) {
                return ParsedPrimitive.fromString(((URL) object).toString());
            } else {
                throw new InvalidParameterException("object must be URL");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                try {
                    return new URL(element.asPrimitive().asString());
                } catch (MalformedURLException e) {
                    throw new InvalidParameterException("object is an invalid URL");
                }
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.nio.file.Path}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor PATH_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Path) {
                return ParsedPrimitive.fromString(((Path) object).toString());
            } else {
                throw new InvalidParameterException("object must be Path");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                return Path.of(element.asPrimitive().asString());
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.io.File}. Converts to and from a ParsedPrimitive string
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor FILE_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof File) {
                return ParsedPrimitive.fromString(((File) object).getPath());
            } else {
                throw new InvalidParameterException("object must be File");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive() && element.asPrimitive().isString()) {
                return new File(element.asPrimitive().asString());
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.net.InetAddress}. Converts to and from a ParsedPrimitive string or bigint
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor INET_ADDRESS_P = new TypeProcessor() {
        final BigInteger MASK_8 = BigInteger.TWO.pow(8).subtract(BigInteger.ONE);
        final BigInteger MASK_32 = BigInteger.TWO.pow(32).subtract(BigInteger.ONE);
        final BigInteger MASK_128 = BigInteger.TWO.pow(128).subtract(BigInteger.ONE);

        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof InetAddress) {
                boolean hasHostname = !((InetAddress) object).toString().startsWith("/"); // Kinda hacky but the only way

                // This works but is illegal in newer java versions
                // try {
                //     Field holderField = InetAddress.class.getDeclaredField("holder");
                //     holderField.setAccessible(true);
                //     Object holder = holderField.get(((InetAddress) object));
                //
                //     Field hostnameField = holder.getClass().getDeclaredField("hostName");
                //     hostnameField.setAccessible(true);
                //
                //     hasHostname = hostnameField.get(holder) != null;
                // } catch (IllegalAccessException | NoSuchFieldException e) {
                //     throw new RuntimeException(e);
                // }

                if (!hasHostname) {
                    byte[] address = ((InetAddress) object).getAddress();

                    switch (options.getInetAddressMode()) {
                        case STRING -> {
                            if (address.length == 4) {
                                String[] addressBytes = new String[4];

                                for (int i = 0; i < 4; i++) {
                                    addressBytes[i] = Integer.toUnsignedString(address[i] & 0xFF, 10);
                                    while (options.isFillAddresses() && addressBytes[i].length() < 3) {
                                        addressBytes[i] = "0" + addressBytes[i];
                                    }
                                }

                                return ParsedPrimitive.fromString(String.join(".", addressBytes));
                            } else if (address.length == 16) {
                                StringBuilder stringBuilder = new StringBuilder();

                                if (Arrays.equals(address, new byte[address.length])) {
                                    return ParsedPrimitive.fromString("::");
                                }

                                int currentIndex = -1, currentLength = 0;
                                int maxIndex = -1, maxLength = 0;

                                for (int i = 0; i < 8; i++) {
                                    if ((((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF)) == 0) {
                                        if (currentLength == 0) {
                                            currentIndex = i;
                                        }

                                        currentLength++;
                                    } else {
                                        if (currentLength > maxLength) {
                                            maxIndex = currentIndex;
                                            maxLength = currentLength;
                                        }

                                        currentLength = 0;
                                    }
                                }

                                if (currentLength > maxLength) {
                                    maxIndex = currentIndex;
                                    maxLength = currentLength;
                                }

                                boolean first = true;

                                for (int i = 0; i < 8; i++) {
                                    if (!options.isFillAddresses() && i == maxIndex) {
                                        stringBuilder.append(":");
                                        if (first) {
                                            stringBuilder.append(":");
                                            first = false;
                                        }
                                        i += maxLength - 1;
                                    } else {
                                        StringBuilder subStringBuilder = new StringBuilder(Integer.toUnsignedString(((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF), 16));
                                        while (options.isFillAddresses() && subStringBuilder.length() < 4) {
                                            subStringBuilder.insert(0, "0");
                                        }
                                        stringBuilder.append(subStringBuilder);

                                        if (i < 7) {
                                            stringBuilder.append(":");
                                        }

                                        first = false;
                                    }
                                }

                                return ParsedPrimitive.fromString(stringBuilder.toString());
                            } else {
                                throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                            }
                        }
                        case NUMBER -> {
                            byte[] extendedAddress = new byte[address.length + 1]; // Avoid our number being interpreted as a signed number
                            System.arraycopy(address, 0, extendedAddress, 1, address.length);

                            if (address.length == 4) {
                                return ParsedPrimitive.fromBigInteger(new BigInteger(extendedAddress));
                            } else if (address.length == 16) {
                                return ParsedPrimitive.fromBigInteger(new BigInteger(extendedAddress).or(BigInteger.ONE.shiftLeft(128)));
                            } else {
                                throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                            }
                        }
                        default -> {
                            throw new RuntimeException();
                        }
                    }
                } else {
                    switch (options.getInetAddressMode()) {
                        case STRING -> {
                            return ParsedPrimitive.fromString(((InetAddress) object).getHostName());
                        }
                        case NUMBER -> {
                            throw new InvalidParameterException("Cannot convert hostname to Number");
                        }
                        default -> {
                            throw new RuntimeException();
                        }
                    }
                }
            } else {
                throw new InvalidParameterException("object must be InetAddress");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        try {
                            String addressString = element.asPrimitive().asString();
                            if (addressString.startsWith("[") && addressString.endsWith("]")) {
                                addressString = addressString.substring(1, addressString.length() - 1);
                            }

                            if (addressString.equalsIgnoreCase("::")) {
                                return InetAddress.getByAddress(new byte[16]);
                            }
                            if (addressString.startsWith("::")) {
                                addressString = addressString.substring(1);
                            }
                            if (addressString.endsWith("::")) {
                                addressString = addressString.substring(0, addressString.length() - 1);
                            }

                            String[] addressBytes = addressString.split("[.:]", -1);

                            if (addressBytes.length == 4 && addressString.contains(".") && !addressString.contains(":")) {
                                byte[] address = new byte[4];

                                for (int i = 0; i < 4; i++) {
                                    address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xFF);
                                }

                                return InetAddress.getByAddress(address);
                            } else if (addressString.contains(":") && !addressString.contains(".")) {
                                byte[] address = new byte[16];

                                for (int i = 0, j = 0; i < 8; i++) {
                                    if (addressBytes[i + j].isEmpty()) {
                                        int length = 8 - addressBytes.length;
                                        i += length;
                                        j -= length;
                                        continue;
                                    }

                                    address[(i) * 2] = (byte) ((Integer.parseUnsignedInt(addressBytes[i + j], 16) >> 8) & 0xFF);
                                    address[((i) * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i + j], 16) & 0xFF);
                                }

                                return InetAddress.getByAddress(address);
                            } else {
                                throw new InvalidParameterException("InetAddress is not IPv4 or IPv6");
                            }
                        } catch (UnknownHostException e) {
                            throw new RuntimeException("object is an invalid InetAddress", e);
                        } catch (InvalidParameterException e) {
                            try {
                                return InetAddress.getByName(element.asPrimitive().asString());
                            } catch (UnknownHostException e2) {
                                throw new RuntimeException("object is an invalid InetAddress", e2);
                            }
                        }
                    }
                    case BYTE,
                         SHORT,
                         INTEGER,
                         LONG -> {
                        ByteBuffer longBuffer = ByteBuffer.wrap(new byte[4]);
                        longBuffer.putLong(element.asPrimitive().toLong());

                        try {
                            return InetAddress.getByAddress(longBuffer.array());
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    }
                    case BIGINTEGER -> {
                        BigInteger bigInt = element.asPrimitive().asBigInteger();
                        BigInteger upper = bigInt.shiftRight(128);

                        boolean isIPv4 = upper.and(MASK_8).equals(BigInteger.ZERO);

                        BigInteger lower = bigInt.and(isIPv4 ? MASK_32 : MASK_128);
                        byte[] address = lower.toByteArray();

                        if ((isIPv4 && address.length < 4) || (!isIPv4 && address.length < 16)) {
                            byte[] fullAddress = new byte[isIPv4 ? 4 : 16];
                            System.arraycopy(address, 0, fullAddress, fullAddress.length - address.length, address.length);
                            address = fullAddress;
                        }
                        if ((isIPv4 && address.length > 4) || (!isIPv4 && address.length > 16)) {
                            address = Arrays.copyOfRange(address, address.length - (isIPv4 ? 4 : 16), address.length);
                        }

                        try {
                            return InetAddress.getByAddress(address);
                        } catch (UnknownHostException e) {
                            throw new RuntimeException("object is an invalid InetAddress", e);
                        }
                    }
                    default -> {
                        throw new InvalidParameterException("object must be String");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

    /**
     * A type processor for {@link java.net.InetSocketAddress}. Converts to and from a ParsedPrimitive string or bigint
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor INETSOCKET_ADDRESS_P = new TypeProcessor() {
        final BigInteger MASK_8 = BigInteger.TWO.pow(8).subtract(BigInteger.ONE);
        final BigInteger MASK_32 = BigInteger.TWO.pow(32).subtract(BigInteger.ONE);
        final BigInteger MASK_48 = BigInteger.TWO.pow(48).subtract(BigInteger.ONE);
        final BigInteger MASK_128 = BigInteger.TWO.pow(128).subtract(BigInteger.ONE);
        final BigInteger MASK_136 = BigInteger.TWO.pow(136).subtract(BigInteger.ONE);
        final BigInteger MASK_152 = BigInteger.TWO.pow(152).subtract(BigInteger.ONE);

        final BigInteger MASK_32_48 = MASK_48.subtract(MASK_32);
        final BigInteger MASK_136_152 = MASK_152.subtract(MASK_136);

        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof InetSocketAddress) {
                boolean hasHostname = !((InetSocketAddress) object).getAddress().toString().startsWith("/"); // Kinda hacky but the only way

                // This works but is illegal in newer java versions
                // try {
                //     Field holderField = InetAddress.class.getDeclaredField("holder");
                //     holderField.setAccessible(true);
                //     Object holder = holderField.get(((InetAddress) object));
                //
                //     Field hostnameField = holder.getClass().getDeclaredField("hostName");
                //     hostnameField.setAccessible(true);
                //
                //     hasHostname = hostnameField.get(holder) != null;
                // } catch (IllegalAccessException | NoSuchFieldException e) {
                //     throw new RuntimeException(e);
                // }

                if (!hasHostname) {
                    byte[] address = ((InetSocketAddress) object).getAddress().getAddress();
                    int port = ((InetSocketAddress) object).getPort();

                    switch (options.getInetAddressMode()) {
                        case STRING -> {
                            if (address.length == 4) {
                                String[] addressBytes = new String[4];

                                for (int i = 0; i < 4; i++) {
                                    addressBytes[i] = Integer.toUnsignedString(address[i] & 0xFF, 10);
                                    while (options.isFillAddresses() && addressBytes[i].length() < 3) {
                                        addressBytes[i] = "0" + addressBytes[i];
                                    }
                                }

                                return ParsedPrimitive.fromString(String.join(".", addressBytes) + ":" + Integer.toUnsignedString(port, 10));
                            } else if (address.length == 16) {
                                StringBuilder stringBuilder = new StringBuilder();

                                if (!Arrays.equals(address, new byte[address.length])) {
                                    stringBuilder.append("[");

                                    int currentIndex = -1, currentLength = 0;
                                    int maxIndex = -1, maxLength = 0;

                                    for (int i = 0; i < 8; i++) {
                                        if ((((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF)) == 0) {
                                            if (currentLength == 0) {
                                                currentIndex = i;
                                            }

                                            currentLength++;
                                        } else {
                                            if (currentLength > maxLength) {
                                                maxIndex = currentIndex;
                                                maxLength = currentLength;
                                            }

                                            currentLength = 0;
                                        }
                                    }

                                    if (currentLength > maxLength) {
                                        maxIndex = currentIndex;
                                        maxLength = currentLength;
                                    }

                                    boolean first = true;

                                    for (int i = 0; i < 8; i++) {
                                        if (!options.isFillAddresses() && i == maxIndex) {
                                            stringBuilder.append(":");
                                            if (first) {
                                                stringBuilder.append(":");
                                                first = false;
                                            }
                                            i += maxLength - 1;
                                        } else {
                                            StringBuilder subStringBuilder = new StringBuilder(Integer.toUnsignedString(((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF), 16));
                                            while (options.isFillAddresses() && subStringBuilder.length() < 4) {
                                                subStringBuilder.insert(0, "0");
                                            }
                                            stringBuilder.append(subStringBuilder);

                                            if (i < 7) {
                                                stringBuilder.append(":");
                                            }

                                            first = false;
                                        }
                                    }

                                    stringBuilder.append("]:");
                                } else {
                                    stringBuilder.append("[::]:");
                                }

                                stringBuilder.append(Integer.toUnsignedString(port, 10));

                                return ParsedPrimitive.fromString(stringBuilder.toString());
                            } else {
                                throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                            }
                        }
                        case NUMBER -> {
                            byte[] extendedAddress = new byte[address.length + 1]; // Avoid our number being interpreted as a signed number
                            System.arraycopy(address, 0, extendedAddress, 1, address.length);

                            if (address.length == 4) {
                                return ParsedPrimitive.fromBigInteger(new BigInteger(extendedAddress).or(new BigInteger(new byte[] { (byte) ((port >> 8) & 0xFF), (byte) (port & 0xFF), 0, 0, 0, 0 })));
                            } else if (address.length == 16) {
                                return ParsedPrimitive.fromBigInteger(new BigInteger(extendedAddress).or(new BigInteger(new byte[] { (byte) ((port >> 8) & 0xFF), (byte) (port & 0xFF), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 })).or(BigInteger.ONE.shiftLeft(128)));
                            } else {
                                throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                            }
                        }
                        default -> {
                            throw new RuntimeException();
                        }
                    }
                } else {
                    switch (options.getInetAddressMode()) {
                        case STRING -> {
                            return ParsedPrimitive.fromString(((InetSocketAddress) object).getAddress().getHostName() + ":" + ((InetSocketAddress) object).getPort());
                        }
                        case NUMBER -> {
                            throw new InvalidParameterException("Cannot convert hostname to Number");
                        }
                        default -> {
                            throw new RuntimeException();
                        }
                    }
                }
            } else {
                throw new InvalidParameterException("object must be InetSocketAddress");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        try {
                            String fullString = element.asPrimitive().asString();
                            String addressString = fullString;
                            if (addressString.startsWith("[") && addressString.lastIndexOf("]:") != -1) {
                                addressString = addressString.substring(1, addressString.lastIndexOf("]:"));
                            } else {
                                addressString = addressString.substring(0, addressString.lastIndexOf(":"));
                            }
                            String portString = fullString.substring(fullString.lastIndexOf(":") + 1);
                            int port = Integer.parseUnsignedInt(portString, 10);

                            if (addressString.equalsIgnoreCase("::")) {
                                return new InetSocketAddress(InetAddress.getByAddress(new byte[16]), port);
                            }
                            if (addressString.startsWith("::")) {
                                addressString = addressString.substring(1);
                            }
                            if (addressString.endsWith("::")) {
                                addressString = addressString.substring(0, addressString.length() - 1);
                            }

                            String[] addressBytes = addressString.split("[.:]", -1);

                            if (addressBytes.length == 4 && addressString.contains(".") && !addressString.contains(":")) {
                                byte[] address = new byte[4];

                                for (int i = 0; i < 4; i++) {
                                    address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xFF);
                                }

                                return new InetSocketAddress(InetAddress.getByAddress(address), port);
                            } else if (addressString.contains(":") && !addressString.contains(".")) {
                                byte[] address = new byte[16];

                                for (int i = 0, j = 0; i < 8; i++) {
                                    if (addressBytes[i + j].isEmpty()) {
                                        int length = 8 - addressBytes.length;
                                        i += length;
                                        j -= length;
                                        continue;
                                    }

                                    address[(i) * 2] = (byte) ((Integer.parseUnsignedInt(addressBytes[i + j], 16) >> 8) & 0xFF);
                                    address[((i) * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i + j], 16) & 0xFF);
                                }

                                return new InetSocketAddress(InetAddress.getByAddress(address), port);
                            } else {
                                throw new InvalidParameterException("InetAddress is not IPv4 or IPv6");
                            }
                        } catch (UnknownHostException e) {
                            throw new RuntimeException("object is an invalid InetAddress", e);
                        } catch (InvalidParameterException e) {
                            String fullString = element.asPrimitive().asString();
                            String addressString = fullString;
                            if (addressString.lastIndexOf(":") != -1) {
                                addressString = addressString.substring(0, addressString.lastIndexOf(":"));
                            }
                            String portString = fullString.substring(fullString.lastIndexOf(":") + 1);

                            int port = Integer.parseUnsignedInt(portString, 10);

                            try {
                                return new InetSocketAddress(InetAddress.getByName(addressString), port);
                            } catch (UnknownHostException e2) {
                                throw new RuntimeException("object is an invalid InetAddress", e2);
                            }
                        }
                    }
                    case LONG -> {
                        ByteBuffer longBuffer = ByteBuffer.wrap(new byte[4]);
                        longBuffer.putLong(element.asPrimitive().asLong() & 0xFFFFFFFFL);

                        int port = (int) (element.asPrimitive().asLong() >> 32);

                        try {
                            return new InetSocketAddress(InetAddress.getByAddress(longBuffer.array()), port);
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    }
                    case BIGINTEGER -> {
                        BigInteger bigInt = element.asPrimitive().asBigInteger();
                        BigInteger upper = bigInt.shiftRight(128);

                        boolean isIPv4 = upper.and(MASK_8).equals(BigInteger.ZERO);

                        BigInteger lower = bigInt.and(isIPv4 ? MASK_32 : MASK_128);
                        byte[] address = lower.toByteArray();

                        if ((isIPv4 && address.length < 4) || (!isIPv4 && address.length < 16)) {
                            byte[] fullAddress = new byte[isIPv4 ? 4 : 16];
                            System.arraycopy(address, 0, fullAddress, fullAddress.length - address.length, address.length);
                            address = fullAddress;
                        }
                        if ((isIPv4 && address.length > 4) || (!isIPv4 && address.length > 16)) {
                            address = Arrays.copyOfRange(address, address.length - (isIPv4 ? 4 : 16), address.length);
                        }

                        int port = bigInt.and(isIPv4 ? MASK_32_48 : MASK_136_152).shiftRight(isIPv4 ? 32 : 136).shortValue();

                        try {
                            return new InetSocketAddress(InetAddress.getByAddress(address), port);
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or BigInt");
            }
        }
    };

    /**
     * A type processor for {@link java.util.Calendar}. Converts to and from a ParsedPrimitive string or long
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor CALENDAR_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Calendar) {
                switch (options.getDateMode()) {
                    case STRING -> {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format(Instant.ofEpochMilli(((Calendar) object).getTimeInMillis())));
                    }
                    case NUMBER -> {
                        return ParsedPrimitive.fromLong(((Calendar) object).getTimeInMillis());
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be Calendar");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
                            return calendar;
                        } catch (DateTimeParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    }
                    case BYTE,
                         SHORT,
                         INTEGER,
                         LONG -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(element.asPrimitive().toLong());
                        return calendar;
                    }
                    default -> {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    /**
     * A type processor for {@link java.util.Date}. Converts to and from a ParsedPrimitive string or long
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor DATE_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Date) {
                switch (options.getDateMode()) {
                    case STRING -> {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format(Instant.ofEpochMilli(((Date) object).getTime())));
                    }
                    case NUMBER -> {
                        return ParsedPrimitive.fromLong(((Date) object).getTime());
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be Date");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        try {
                            return new Date(Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
                        } catch (DateTimeParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    }
                    case BYTE,
                         SHORT,
                         INTEGER,
                         LONG -> {
                        return new Date(element.asPrimitive().toLong());
                    }
                    default -> {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    /**
     * A type processor for {@link java.time.Instant}. Converts to and from a ParsedPrimitive string or long
     *
     * @since 2.0.0
     */
    public final @NotNull TypeProcessor INSTANT_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Instant) {
                switch (options.getDateMode()) {
                    case STRING -> {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format((Instant) object));
                    }
                    case NUMBER -> {
                        return ParsedPrimitive.fromLong(((Instant) object).toEpochMilli());
                    }
                    default -> {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be Instant");
            }
        }

        @Override
        public @Nullable Object toObject(@NotNull ParsedElement element) {
            if (element.isPrimitive() && element.asPrimitive().isNull()) {
                return null;
            }

            if (element.isPrimitive()) {
                switch (element.asPrimitive().getType()) {
                    case STRING -> {
                        return Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString()));
                    }
                    case BYTE,
                         SHORT,
                         INTEGER,
                         LONG -> {
                        return Instant.ofEpochMilli(element.asPrimitive().toLong());
                    }
                    default -> {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    /**
     * Registers the default type processors for a given builder
     *
     * @param builder The builder to register them to
     *
     * @since 2.0.0
     */
    static void register(@NotNull ObjectProcessor.Builder builder) {
        if (builder.getDefaultTypeProcessorsOptions() == null) {
            return;
        }

        DefaultTypeProcessors typeProcessors = new DefaultTypeProcessors(builder.getDefaultTypeProcessorsOptions());

        if (!builder.hasTypeProcessor(StringBuilder.class)) {
            builder.createTypeProcessor(StringBuilder.class, typeProcessors.STRING_BUILDER_P);
        }

        if (!builder.hasTypeProcessor(StringBuffer.class)) {
            builder.createTypeProcessor(StringBuffer.class, typeProcessors.STRING_BUFFER_P);
        }

        if (!builder.hasTypeProcessor(UUID.class)) {
            builder.createTypeProcessor(UUID.class, typeProcessors.UUID_P);
        }

        if (!builder.hasTypeProcessor(URI.class)) {
            builder.createTypeProcessor(URI.class, typeProcessors.URI_P);
        }

        if (!builder.hasTypeProcessor(URL.class)) {
            builder.createTypeProcessor(URL.class, typeProcessors.URL_P);
        }

        if (!builder.hasTypeProcessor(Path.class)) {
            builder.createTypeProcessor(Path.class, typeProcessors.PATH_P);
        }

        if (!builder.hasTypeProcessor(File.class)) {
            builder.createTypeProcessor(File.class, typeProcessors.FILE_P);
        }

        if (!builder.hasTypeProcessor(InetAddress.class)) {
            builder.createTypeProcessor(InetAddress.class, typeProcessors.INET_ADDRESS_P);
        }

        if (!builder.hasTypeProcessor(InetSocketAddress.class)) {
            builder.createTypeProcessor(InetSocketAddress.class, typeProcessors.INETSOCKET_ADDRESS_P);
        }

        if (!builder.hasTypeProcessor(Calendar.class)) {
            builder.createTypeProcessor(Calendar.class, typeProcessors.CALENDAR_P);
        }

        if (!builder.hasTypeProcessor(Date.class)) {
            builder.createTypeProcessor(Date.class, typeProcessors.DATE_P);
        }

        if (!builder.hasTypeProcessor(Instant.class)) {
            builder.createTypeProcessor(Instant.class, typeProcessors.INSTANT_P);
        }
    }
}