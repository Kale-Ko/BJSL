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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO Docs
public final class DefaultTypeProcessors {
    public static final @NotNull DefaultTypeProcessors DEFAULT = new DefaultTypeProcessors(Options.DEFAULT);

    public static class Options {
        public static final @NotNull Options DEFAULT = new Options(Options.UUIDMode.STRING, Options.InetAddressMode.STRING, false, Options.DateMode.STRING, DateTimeFormatter.ISO_DATE_TIME);

        public enum UUIDMode {
            STRING,
            BYTE_ARRAY,
            SHORT_ARRAY,
            INT_ARRAY,
            LONG_ARRAY,
            BIGINT
        }

        public enum InetAddressMode {
            STRING,
            NUMBER
        }

        public enum DateMode {
            STRING,
            NUMBER
        }

        protected final @NotNull UUIDMode uuidMode;

        protected final @NotNull InetAddressMode inetAddressMode;
        protected final boolean fillAddresses;

        protected final @NotNull DateMode dateMode;
        protected final @NotNull DateTimeFormatter dateTimeFormatter;

        public Options(@NotNull UUIDMode uuidMode, @NotNull InetAddressMode inetAddressMode, boolean fillAddresses, @NotNull DateMode dateMode, @NotNull DateTimeFormatter dateTimeFormatter) {
            this.uuidMode = uuidMode;
            this.inetAddressMode = inetAddressMode;
            this.fillAddresses = fillAddresses;

            this.dateMode = dateMode;
            this.dateTimeFormatter = dateTimeFormatter;
        }

        public @NotNull UUIDMode getUuidMode() {
            return this.uuidMode;
        }

        public @NotNull InetAddressMode getInetAddressMode() {
            return inetAddressMode;
        }

        public boolean isFillAddresses() {
            return this.fillAddresses;
        }

        public @NotNull DateMode getDateMode() {
            return this.dateMode;
        }

        public @NotNull DateTimeFormatter getDateTimeFormatter() {
            return this.dateTimeFormatter;
        }
    }

    private final @NotNull Options options;

    public DefaultTypeProcessors(@NotNull Options options) {
        this.options = options;
    }

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

    public final @NotNull TypeProcessor UUID_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof UUID) {
                UUID uuid = (UUID) object;

                switch (options.getUuidMode()) {
                    case STRING: {
                        return ParsedPrimitive.fromString(uuid.toString());
                    }
                    case BYTE_ARRAY: {
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
                    case SHORT_ARRAY: {
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
                    case INT_ARRAY: {
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
                    case LONG_ARRAY: {
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
                    case BIGINT: {
                        ByteBuffer bigIntBuffer = ByteBuffer.wrap(new byte[16]);
                        bigIntBuffer.putLong(uuid.getMostSignificantBits());
                        bigIntBuffer.putLong(uuid.getLeastSignificantBits());

                        return ParsedPrimitive.fromBigInteger(new BigInteger(bigIntBuffer.array()));
                    }
                    default: {
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
                            case 16: {
                                switch (subElement.asPrimitive().getType()) {
                                    case BYTE: {
                                        buffer.put(subElement.asPrimitive().asByte());
                                        break;
                                    }
                                    case SHORT: {
                                        buffer.put((byte) subElement.asPrimitive().asShort());
                                        break;
                                    }
                                    case INTEGER: {
                                        buffer.put((byte) subElement.asPrimitive().asInteger());
                                        break;
                                    }
                                    case LONG: {
                                        buffer.put((byte) subElement.asPrimitive().asLong());
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException();
                                    }
                                }
                                break;
                            }
                            case 8: {
                                switch (subElement.asPrimitive().getType()) {
                                    case BYTE: {
                                        buffer.putShort(subElement.asPrimitive().asByte());
                                        break;
                                    }
                                    case SHORT: {
                                        buffer.putShort(subElement.asPrimitive().asShort());
                                        break;
                                    }
                                    case INTEGER: {
                                        buffer.putShort((short) subElement.asPrimitive().asInteger());
                                        break;
                                    }
                                    case LONG: {
                                        buffer.putShort((short) subElement.asPrimitive().asLong());
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException();
                                    }
                                }
                                break;
                            }
                            case 4: {
                                switch (subElement.asPrimitive().getType()) {
                                    case BYTE: {
                                        buffer.putInt(subElement.asPrimitive().asByte());
                                        break;
                                    }
                                    case SHORT: {
                                        buffer.putInt(subElement.asPrimitive().asShort());
                                        break;
                                    }
                                    case INTEGER: {
                                        buffer.putInt(subElement.asPrimitive().asInteger());
                                        break;
                                    }
                                    case LONG: {
                                        buffer.putInt((int) subElement.asPrimitive().asLong());
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException();
                                    }
                                }
                                break;
                            }
                            case 2: {
                                switch (subElement.asPrimitive().getType()) {
                                    case BYTE: {
                                        buffer.putLong(subElement.asPrimitive().asByte());
                                        break;
                                    }
                                    case SHORT: {
                                        buffer.putLong(subElement.asPrimitive().asShort());
                                        break;
                                    }
                                    case INTEGER: {
                                        buffer.putLong(subElement.asPrimitive().asInteger());
                                        break;
                                    }
                                    case LONG: {
                                        buffer.putLong(subElement.asPrimitive().asLong());
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException();
                                    }
                                }
                                break;
                            }
                            default: {
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
                    case STRING: {
                        return UUID.fromString(element.asPrimitive().asString());
                    }
                    case BYTE: {
                        return new UUID(0, (long) element.asPrimitive().asByte());
                    }
                    case SHORT: {
                        return new UUID(0, (long) element.asPrimitive().asShort());
                    }
                    case INTEGER: {
                        return new UUID(0, (long) element.asPrimitive().asInteger());
                    }
                    case LONG: {
                        return new UUID(0, element.asPrimitive().asLong());
                    }
                    case BIGINTEGER: {
                        BigInteger bigInt = element.asPrimitive().asBigInteger();
                        return new UUID(bigInt.shiftRight(64).longValue(), bigInt.longValue());
                    }
                    default: {
                        throw new InvalidParameterException("object must be Array, BigInt, or String");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be Array, BigInt, or String");
            }
        }
    };

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

    public final @NotNull TypeProcessor URL_P = new TypeProcessor() {
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
                        case STRING: {
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
                        case NUMBER: {
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
                        default: {
                            throw new RuntimeException();
                        }
                    }
                } else {
                    switch (options.getInetAddressMode()) {
                        case STRING: {
                            return ParsedPrimitive.fromString(((InetAddress) object).getHostName());
                        }
                        case NUMBER: {
                            throw new InvalidParameterException("Cannot convert hostname to Number");
                        }
                        default: {
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
                    case STRING: {
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
                    case BYTE:
                        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[4]);
                        byteBuffer.put(element.asPrimitive().asByte());

                        try {
                            return InetAddress.getByAddress(byteBuffer.array());
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    case SHORT:
                        ByteBuffer shortBuffer = ByteBuffer.wrap(new byte[4]);
                        shortBuffer.putShort(element.asPrimitive().asShort());

                        try {
                            return InetAddress.getByAddress(shortBuffer.array());
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    case INTEGER:
                        ByteBuffer intBuffer = ByteBuffer.wrap(new byte[4]);
                        intBuffer.putInt(element.asPrimitive().asInteger());

                        try {
                            return InetAddress.getByAddress(intBuffer.array());
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    case LONG:
                        ByteBuffer longBuffer = ByteBuffer.wrap(new byte[4]);
                        longBuffer.putLong(element.asPrimitive().asLong());

                        try {
                            return InetAddress.getByAddress(longBuffer.array());
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    case BIGINTEGER: {
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
                    default: {
                        throw new InvalidParameterException("object must be String");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String");
            }
        }
    };

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
                        case STRING: {
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
                                stringBuilder.append(Integer.toUnsignedString(port, 10));

                                return ParsedPrimitive.fromString(stringBuilder.toString());
                            } else {
                                throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                            }
                        }
                        case NUMBER: {
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
                        default: {
                            throw new RuntimeException();
                        }
                    }
                } else {
                    switch (options.getInetAddressMode()) {
                        case STRING: {
                            return ParsedPrimitive.fromString(((InetSocketAddress) object).getAddress().getHostName() + ":" + ((InetSocketAddress) object).getPort());
                        }
                        case NUMBER: {
                            throw new InvalidParameterException("Cannot convert hostname to Number");
                        }
                        default: {
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
                    case STRING: {
                        try {
                            String fullString = element.asPrimitive().asString();
                            String addressString = fullString;
                            if (addressString.startsWith("[") && addressString.lastIndexOf("]:") != -1) {
                                addressString = addressString.substring(1, addressString.lastIndexOf("]:"));
                            } else {
                                addressString = addressString.substring(0, addressString.lastIndexOf(":"));
                            }
                            String portString = fullString.substring(fullString.lastIndexOf(":") + 1);

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
                            int port = Integer.parseUnsignedInt(portString, 10);

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
                    case LONG: {
                        ByteBuffer longBuffer = ByteBuffer.wrap(new byte[4]);
                        longBuffer.putLong(element.asPrimitive().asLong() & 0xFFFFFFFFL);

                        int port = (int) (element.asPrimitive().asLong() >> 32);

                        try {
                            return new InetSocketAddress(InetAddress.getByAddress(longBuffer.array()), port);
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    }
                    case BIGINTEGER: {
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
                    default: {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or BigInt");
            }
        }
    };

    public final @NotNull TypeProcessor CALENDAR_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Calendar) {
                switch (options.getDateMode()) {
                    case STRING: {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format(Instant.ofEpochMilli(((Calendar) object).getTimeInMillis())));
                    }
                    case NUMBER: {
                        return ParsedPrimitive.fromLong(((Calendar) object).getTimeInMillis());
                    }
                    default: {
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
                    case STRING: {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
                            return calendar;
                        } catch (DateTimeParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    }
                    case BYTE: {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis((long) element.asPrimitive().asByte());
                        return calendar;
                    }
                    case SHORT: {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis((long) element.asPrimitive().asShort());
                        return calendar;
                    }
                    case INTEGER: {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis((long) element.asPrimitive().asInteger());
                        return calendar;
                    }
                    case LONG: {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(element.asPrimitive().asLong());
                        return calendar;
                    }
                    default: {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    public final @NotNull TypeProcessor DATE_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Date) {
                switch (options.getDateMode()) {
                    case STRING: {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format(Instant.ofEpochMilli(((Date) object).getTime())));
                    }
                    case NUMBER: {
                        return ParsedPrimitive.fromLong(((Date) object).getTime());
                    }
                    default: {
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
                    case STRING: {
                        try {
                            return new Date(Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
                        } catch (DateTimeParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    }
                    case BYTE: {
                        return new Date((long) element.asPrimitive().asByte());
                    }
                    case SHORT: {
                        return new Date((long) element.asPrimitive().asShort());
                    }
                    case INTEGER: {
                        return new Date((long) element.asPrimitive().asInteger());
                    }
                    case LONG: {
                        return new Date(element.asPrimitive().asLong());
                    }
                    default: {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    public final @NotNull TypeProcessor INSTANT_P = new TypeProcessor() {
        @Override
        public @NotNull ParsedElement toElement(@Nullable Object object) {
            if (object == null) {
                return ParsedPrimitive.fromNull();
            }

            if (object instanceof Instant) {
                switch (options.getDateMode()) {
                    case STRING: {
                        return ParsedPrimitive.fromString(options.getDateTimeFormatter().format((Instant) object));
                    }
                    case NUMBER: {
                        return ParsedPrimitive.fromLong(((Instant) object).toEpochMilli());
                    }
                    default: {
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
                    case STRING: {
                        return Instant.from(options.getDateTimeFormatter().parse(element.asPrimitive().asString()));
                    }
                    case BYTE: {
                        return Instant.ofEpochMilli((long) element.asPrimitive().asByte());
                    }
                    case SHORT: {
                        return Instant.ofEpochMilli((long) element.asPrimitive().asShort());
                    }
                    case INTEGER: {
                        return Instant.ofEpochMilli((long) element.asPrimitive().asInteger());
                    }
                    case LONG: {
                        return Instant.ofEpochMilli(element.asPrimitive().asLong());
                    }
                    default: {
                        throw new InvalidParameterException("object must be String or Long");
                    }
                }
            } else {
                throw new InvalidParameterException("object must be String or Long");
            }
        }
    };

    public static void registry(@NotNull ObjectProcessor.Builder builder) {
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