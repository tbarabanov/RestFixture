package com.github.restfixture.assertion;


import com.github.restfixture.rest.parts.Part;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;

/**
 * @author Timofey B.
 */
public class Assertions {

    private static final Map<Part, Class<?>> assertions = new EnumMap<>(Part.class);

    static {
        assertions.put(Part.STATUS_LINE, StatusLineAssertion.class);
        assertions.put(Part.HEADERS, HeadersAssertion.class);
        assertions.put(Part.BODY, BodyAssertion.class);
    }

    /**
     * @param part
     * @param args
     * @param <T>
     * @return
     */
    public static <T extends Assertion<?, ?>> T newAssertion(Part part, Object... args) {
        try {
            return (T) invokeConstructor(checkNotNull(assertions.get(part), "Don't know what to do with '%s'.", part), args);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            throw new IllegalArgumentException(format("Can't create an assertion for part '%s (%s)'.", part, cause.getMessage()), cause);
        }
    }
}
