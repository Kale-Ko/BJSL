package io.github.kale_ko.bjsl.processor;

import io.github.kale_ko.bjsl.elements.ParsedElement;
import io.github.kale_ko.bjsl.elements.ParsedPrimitive;
import java.io.File;
import java.net.*;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DefaultTypeProcessors {
    private DefaultTypeProcessors() {
    }

    public static void registry(@NotNull ObjectProcessor.Builder builder) {
        if (!builder.hasTypeProcessor(UUID.class)) {
            builder.createTypeProcessor(UUID.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof UUID) {
                        return ParsedPrimitive.fromString(object.toString());
                    } else {
                        throw new InvalidParameterException("object must be UUID");
                    }
                }

                @Override
                public @Nullable Object toObject(@NotNull ParsedElement element) {
                    if (element.isPrimitive() && element.asPrimitive().isNull()) {
                        return null;
                    }

                    if (element.isPrimitive() && element.asPrimitive().isString()) {
                        return UUID.fromString(element.asPrimitive().asString());
                    } else {
                        throw new InvalidParameterException("object must be String");
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
                        return ParsedPrimitive.fromString(object.toString());
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
                        return ParsedPrimitive.fromString(object.toString());
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
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof InetAddress) {
                        byte[] address = ((InetAddress) object).getAddress();

                        if (address.length == 4) {
                            String[] addressBytes = new String[4];

                            for (int i = 0; i < 4; i++) {
                                addressBytes[i] = Integer.toUnsignedString(address[i] & 0xff, 10);
                            }

                            return ParsedPrimitive.fromString(String.join(".", addressBytes));
                        } else if (address.length == 16) {
                            String[] addressBytes = new String[8];

                            for (int i = 0; i < 8; i++) {
                                addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xff) << 8) + (address[(i * 2) + 1] & 0xff), 16);
                            }

                            return ParsedPrimitive.fromString(String.join(":", addressBytes));
                        } else {
                            throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
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

                    if (element.isPrimitive() && element.asPrimitive().isString()) {
                        try {
                            String[] addressBytes = element.asPrimitive().asString().split("[.:]");

                            if (addressBytes.length == 4) {
                                byte[] address = new byte[4];

                                for (int i = 0; i < 4; i++) {
                                    address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xff);
                                }

                                return InetAddress.getByAddress(address);
                            } else if (addressBytes.length == 8) {
                                byte[] address = new byte[16];

                                for (int i = 0; i < 8; i++) {
                                    address[i * 2] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                    address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                }

                                return InetAddress.getByAddress(address);
                            } else {
                                throw new InvalidParameterException("InetAddress must be IPv4 or IPv6");
                            }
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetAddress");
                        }
                    } else {
                        throw new InvalidParameterException("object must be String");
                    }
                }
            });
        }

        if (!builder.hasTypeProcessor(InetSocketAddress.class)) {
            builder.createTypeProcessor(InetSocketAddress.class, new TypeProcessor() {
                @Override
                public @NotNull ParsedElement toElement(@Nullable Object object) {
                    if (object == null) {
                        return ParsedPrimitive.fromNull();
                    }

                    if (object instanceof InetSocketAddress) {
                        byte[] address = ((InetSocketAddress) object).getAddress().getAddress();
                        int port = ((InetSocketAddress) object).getPort();

                        if (address.length == 4) {
                            String[] addressBytes = new String[4];

                            for (int i = 0; i < 4; i++) {
                                addressBytes[i] = Integer.toUnsignedString(address[i] & 0xff, 10);
                            }

                            return ParsedPrimitive.fromString(String.join(".", addressBytes) + ":" + Integer.toUnsignedString(port, 10));
                        } else if (address.length == 16) {
                            String[] addressBytes = new String[8];

                            for (int i = 0; i < 8; i++) {
                                addressBytes[i] = Integer.toUnsignedString(((address[i * 2] & 0xff) << 8) + (address[(i * 2) + 1] & 0xff), 16);
                            }

                            return ParsedPrimitive.fromString("[" + String.join(":", addressBytes) + "]:" + Integer.toUnsignedString(port, 10));
                        } else {
                            throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
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

                    if (element.isPrimitive() && element.asPrimitive().isString()) {
                        try {
                            String[] addressBytes = element.asPrimitive().asString().replace("[", "").replace("]", "").split("[.:]");

                            if (addressBytes.length == 5) {
                                byte[] address = new byte[4];
                                int port;

                                for (int i = 0; i < 4; i++) {
                                    address[i] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 10) & 0xff);
                                }

                                port = Integer.parseUnsignedInt(addressBytes[4], 10);

                                return new InetSocketAddress(InetAddress.getByAddress(address), port);
                            } else if (addressBytes.length == 9) {
                                byte[] address = new byte[16];
                                int port;

                                for (int i = 0; i < 8; i++) {
                                    address[i * 2] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                    address[(i * 2) + 1] = (byte) (Integer.parseUnsignedInt(addressBytes[i], 16) & 0xff);
                                }

                                port = Integer.parseUnsignedInt(addressBytes[8], 10);

                                return new InetSocketAddress(InetAddress.getByAddress(address), port);
                            } else {
                                throw new InvalidParameterException("InetSocketAddress must be IPv4 or IPv6");
                            }
                        } catch (UnknownHostException e) {
                            throw new InvalidParameterException("object is an invalid InetSocketAddress");
                        }
                    } else {
                        throw new InvalidParameterException("object must be String");
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
                        return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format(((Calendar) object).getTime()));
                    } else {
                        throw new InvalidParameterException("object must be Calendar");
                    }
                }

                @Override
                public @Nullable Object toObject(@NotNull ParsedElement element) {
                    if (element.isPrimitive() && element.asPrimitive().isNull()) {
                        return null;
                    }

                    if (element.isPrimitive() && element.asPrimitive().isString()) {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString()));
                            return calendar;
                        } catch (ParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    } else {
                        throw new InvalidParameterException("object must be String");
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
                        return ParsedPrimitive.fromString(DateFormat.getDateInstance(DateFormat.DEFAULT).format((Date) object));
                    } else {
                        throw new InvalidParameterException("object must be Date");
                    }
                }

                @Override
                public @Nullable Object toObject(@NotNull ParsedElement element) {
                    if (element.isPrimitive() && element.asPrimitive().isNull()) {
                        return null;
                    }

                    if (element.isPrimitive() && element.asPrimitive().isString()) {
                        try {
                            return DateFormat.getDateInstance(DateFormat.DEFAULT).parse(element.asPrimitive().asString());
                        } catch (ParseException e) {
                            throw new InvalidParameterException("object is an invalid Date");
                        }
                    } else {
                        throw new InvalidParameterException("object must be String");
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
                        return ParsedPrimitive.fromLong(((Instant) object).toEpochMilli());
                    } else {
                        throw new InvalidParameterException("object must be Instant");
                    }
                }

                @Override
                public @Nullable Object toObject(@NotNull ParsedElement element) {
                    if (element.isPrimitive() && element.asPrimitive().isNull()) {
                        return null;
                    }

                    if (element.isPrimitive() && element.asPrimitive().isLong()) {
                        return Instant.ofEpochMilli(element.asPrimitive().asLong());
                    } else if (element.isPrimitive() && element.asPrimitive().isString()) {
                        return Instant.ofEpochMilli(Long.parseLong(element.asPrimitive().asString()));
                    } else {
                        throw new InvalidParameterException("object must be Long");
                    }
                }
            });
        }
    }
}