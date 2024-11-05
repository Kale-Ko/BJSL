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

// TODO Move processors to public static final variables
public final class DefaultTypeProcessors {
    // TODO Docs
    public static class Options {
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
            LONG
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

    private DefaultTypeProcessors() {
    }

    public static void registry(@NotNull ObjectProcessor.Builder builder) {
        if (builder.getDefaultTypeProcessorsOptions() == null) {
            return;
        }

        if (!builder.hasTypeProcessor(StringBuilder.class)) {
            builder.createTypeProcessor(StringBuilder.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(StringBuffer.class)) {
            builder.createTypeProcessor(StringBuffer.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(UUID.class)) {
            builder.createTypeProcessor(UUID.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof UUID) {
                        UUID uuid = (UUID) object;

                        switch (builder.getDefaultTypeProcessorsOptions().getUuidMode()) {
                            case STRING: {
                                return ParsedPrimitive.fromString(uuid.toString());
                            }
                            case BYTE_ARRAY: {
                                ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
                                byteBuffer.putLong(uuid.getMostSignificantBits());
                                byteBuffer.putLong(uuid.getLeastSignificantBits());

                                ParsedArray byteArray = ParsedArray.create();
                                for (byte b : byteBuffer.array()) {
                                    byteArray.add(ParsedPrimitive.fromByte(b));
                                }
                                return byteArray;
                            }
                            case SHORT_ARRAY: {
                                ByteBuffer shortBuffer = ByteBuffer.wrap(new byte[16]);
                                shortBuffer.putLong(uuid.getMostSignificantBits());
                                shortBuffer.putLong(uuid.getLeastSignificantBits());

                                ParsedArray shortArray = ParsedArray.create();
                                for (short s : shortBuffer.asShortBuffer().array()) {
                                    shortArray.add(ParsedPrimitive.fromShort(s));
                                }
                                return shortArray;
                            }
                            case INT_ARRAY: {
                                ByteBuffer intBuffer = ByteBuffer.wrap(new byte[16]);
                                intBuffer.putLong(uuid.getMostSignificantBits());
                                intBuffer.putLong(uuid.getLeastSignificantBits());

                                ParsedArray intArray = ParsedArray.create();
                                for (int i : intBuffer.asIntBuffer().array()) {
                                    intArray.add(ParsedPrimitive.fromInteger(i));
                                }
                                return intArray;
                            }
                            case LONG_ARRAY: {
                                ByteBuffer longBuffer = ByteBuffer.wrap(new byte[16]);
                                longBuffer.putLong(uuid.getMostSignificantBits());
                                longBuffer.putLong(uuid.getLeastSignificantBits());

                                ParsedArray longArray = ParsedArray.create();
                                for (long l : longBuffer.asLongBuffer().array()) {
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
            });
        }

        if (!builder.hasTypeProcessor(URI.class)) {
            builder.createTypeProcessor(URI.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(URL.class)) {
            builder.createTypeProcessor(URL.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(Path.class)) {
            builder.createTypeProcessor(Path.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(File.class)) {
            builder.createTypeProcessor(File.class, new TypeProcessor() {
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
            });
        }

        if (!builder.hasTypeProcessor(InetAddress.class)) {
            builder.createTypeProcessor(InetAddress.class, new TypeProcessor() {
                final BigInteger MASK_128 = BigInteger.TWO.pow(128).subtract(BigInteger.ONE);

                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof InetAddress) {
                        byte[] address = ((InetAddress) object).getAddress();

                        switch (builder.getDefaultTypeProcessorsOptions().getDateMode()) {
                            case STRING: {
                                if (address.length == 4) {
                                    String[] addressBytes = new String[4];

                                    for (int i = 0; i < 4; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(address[i] & 0xFF, 10);
                                        while (builder.getDefaultTypeProcessorsOptions().isFillAddresses() && addressBytes[i].length() < 3) {
                                            addressBytes[i] = "0" + addressBytes[i];
                                        }
                                    }

                                    return ParsedPrimitive.fromString(String.join(".", addressBytes));
                                } else if (address.length == 16) {
                                    String[] addressBytes = new String[8];

                                    for (int i = 0; i < 8; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF), 16);
                                        while (builder.getDefaultTypeProcessorsOptions().isFillAddresses() && addressBytes[i].length() < 4) {
                                            addressBytes[i] = "0" + addressBytes[i];
                                        }
                                    }

                                    return ParsedPrimitive.fromString(String.join(":", addressBytes));
                                } else {
                                    throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                }
                            }
                            case NUMBER: {
                                if (address.length == 4) {
                                    return ParsedPrimitive.fromBigInteger(new BigInteger(address));
                                } else if (address.length == 16) {
                                    return ParsedPrimitive.fromBigInteger(new BigInteger(address).or(BigInteger.ONE.shiftRight(128)));
                                } else {
                                    throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                }
                            }
                            default: {
                                throw new RuntimeException();
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
                                    String[] addressBytes = element.asPrimitive().asString().split("[.:\\[\\]]");

                                    if (addressBytes.length == 4) {
                                        byte[] address = new byte[4];

                                        for (int i = 0; i < 4; i++) {
                                            address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xFF);
                                        }

                                        return InetAddress.getByAddress(address);
                                    } else if (addressBytes.length == 8 || addressBytes.length == 10) {
                                        byte[] address = new byte[16];

                                        for (int i = 0; i < 8; i++) {
                                            address[i * 2] = (byte) ((Integer.parseUnsignedInt(addressBytes[i + (addressBytes.length == 10 ? 1 : 0)], 16) >> 8) & 0xFF);
                                            address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i + (addressBytes.length == 10 ? 1 : 0)], 16) & 0xFF);
                                        }

                                        return InetAddress.getByAddress(address);
                                    } else {
                                        throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                    }
                                } catch (UnknownHostException e) {
                                    throw new InvalidParameterException("object is an invalid InetAddress");
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
                                BigInteger bigInt = element.asPrimitive().asBigInteger().and(MASK_128);
                                byte[] address = bigInt.toByteArray();

                                System.out.println(address.length + ": " + Arrays.toString(address)); // TODO Make sure IPv4 is length 4

                                try {
                                    return InetAddress.getByAddress(address);
                                } catch (UnknownHostException e) {
                                    throw new InvalidParameterException("object is an invalid InetAddress");
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
            });
        }

        if (!builder.hasTypeProcessor(InetSocketAddress.class)) {
            builder.createTypeProcessor(InetSocketAddress.class, new TypeProcessor() {
                final BigInteger MASK_8 = BigInteger.TWO.pow(8).subtract(BigInteger.ONE);
                final BigInteger MASK_32 = BigInteger.TWO.pow(32).subtract(BigInteger.ONE);
                final BigInteger MASK_48 = BigInteger.TWO.pow(48).subtract(BigInteger.ONE);
                final BigInteger MASK_128 = BigInteger.TWO.pow(128).subtract(BigInteger.ONE);
                final BigInteger MASK_144 = BigInteger.TWO.pow(144).subtract(BigInteger.ONE);

                final BigInteger MASK_32_48 = MASK_48.subtract(MASK_32);
                final BigInteger MASK_128_144 = MASK_144.subtract(MASK_128);

                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof InetSocketAddress) {
                        byte[] address = ((InetSocketAddress) object).getAddress().getAddress();
                        int port = ((InetSocketAddress) object).getPort();

                        switch (builder.getDefaultTypeProcessorsOptions().getDateMode()) {
                            case STRING: {
                                if (address.length == 4) {
                                    String[] addressBytes = new String[4];

                                    for (int i = 0; i < 4; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(address[i] & 0xFF, 10);
                                        while (builder.getDefaultTypeProcessorsOptions().isFillAddresses() && addressBytes[i].length() < 3) {
                                            addressBytes[i] = "0" + addressBytes[i];
                                        }
                                    }

                                    return ParsedPrimitive.fromString(String.join(".", addressBytes) + ":" + Integer.toUnsignedString(port, 10));
                                } else if (address.length == 16) {
                                    String[] addressBytes = new String[8];

                                    for (int i = 0; i < 8; i++) {
                                        addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xFF) << 8) + (address[(i * 2) + 1] & 0xFF), 16);
                                        while (builder.getDefaultTypeProcessorsOptions().isFillAddresses() && addressBytes[i].length() < 4) {
                                            addressBytes[i] = "0" + addressBytes[i];
                                        }
                                    }

                                    return ParsedPrimitive.fromString("[" + String.join(":", addressBytes) + "]:" + Integer.toUnsignedString(port, 10));
                                } else {
                                    throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                                }
                            }
                            case NUMBER: {
                                if (address.length == 4) {
                                    return ParsedPrimitive.fromBigInteger(new BigInteger(address).or(new BigInteger(new byte[] { (byte) ((port >> 8) & 0xFF), (byte) (port & 0xFF), 0, 0, 0, 0 })));
                                } else if (address.length == 16) {
                                    return ParsedPrimitive.fromBigInteger(new BigInteger(address).or(new BigInteger(new byte[] { (byte) ((port >> 8) & 0xFF), (byte) (port & 0xFF), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 })).or(BigInteger.ONE.shiftRight(128)));
                                } else {
                                    throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                                }
                            }
                            default: {
                                throw new RuntimeException();
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
                                    String[] addressBytes = element.asPrimitive().asString().split("[.:\\[\\]]");

                                    if (addressBytes.length == 5) {
                                        byte[] address = new byte[4];
                                        int port;

                                        for (int i = 0; i < 4; i++) {
                                            address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xFF);
                                        }

                                        port = Integer.parseUnsignedInt(addressBytes[4], 10);

                                        return new InetSocketAddress(InetAddress.getByAddress(address), port);
                                    } else if (addressBytes.length == 9 || addressBytes.length == 11) {
                                        byte[] address = new byte[16];
                                        int port;

                                        for (int i = 0; i < 8; i++) {
                                            address[i * 2] = (byte) ((Integer.parseUnsignedInt(addressBytes[i + (addressBytes.length == 11 ? 1 : 0)], 16) << 8) & 0xFF);
                                            address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i + (addressBytes.length == 11 ? 1 : 0)], 16) & 0xFF);
                                        }

                                        port = Integer.parseUnsignedInt(addressBytes[8] + (addressBytes.length == 11 ? 2 : 0), 10);

                                        return new InetSocketAddress(InetAddress.getByAddress(address), port);
                                    } else {
                                        throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                                    }
                                } catch (UnknownHostException e) {
                                    throw new InvalidParameterException("object is an invalid InetSocketAddress");
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

                                int port = bigInt.and(isIPv4 ? MASK_32_48 : MASK_128_144).shiftRight(isIPv4 ? 32 : 128).shortValue();

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
            });
        }

        if (!builder.hasTypeProcessor(Calendar.class)) {
            builder.createTypeProcessor(Calendar.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof Calendar) {
                        switch (builder.getDefaultTypeProcessorsOptions().getDateMode()) {
                            case STRING: {
                                return ParsedPrimitive.fromString(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().format(Instant.ofEpochMilli(((Calendar) object).getTimeInMillis())));
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
                                    calendar.setTimeInMillis(Instant.from(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
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
            });
        }

        if (!builder.hasTypeProcessor(Date.class)) {
            builder.createTypeProcessor(Date.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof Date) {
                        switch (builder.getDefaultTypeProcessorsOptions().getDateMode()) {
                            case STRING: {
                                return ParsedPrimitive.fromString(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().format(Instant.ofEpochMilli(((Date) object).getTime())));
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
                                    return new Date(Instant.from(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().parse(element.asPrimitive().asString())).toEpochMilli());
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
            });
        }

        if (!builder.hasTypeProcessor(Instant.class)) {
            builder.createTypeProcessor(Instant.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof Instant) {
                        switch (builder.getDefaultTypeProcessorsOptions().getDateMode()) {
                            case STRING: {
                                return ParsedPrimitive.fromString(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().format((Instant) object));
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
                                return Instant.from(builder.getDefaultTypeProcessorsOptions().getDateTimeFormatter().parse(element.asPrimitive().asString()));
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
            });
        }
    }
}