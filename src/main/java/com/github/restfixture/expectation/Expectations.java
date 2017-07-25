package com.github.restfixture.expectation;

import com.github.restfixture.expectation.common.Expectation;
import com.github.restfixture.rest.parts.Part;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.MethodUtils.invokeStaticMethod;

/**
 * @author Timofey B.
 */
public class Expectations {

    private final static Map<Part, Class<?>> expectations = new EnumMap<>(Part.class);

    static {
        expectations.put(Part.STATUS_LINE, StatusLineExpectation.class);
        expectations.put(Part.HEADERS, HeadersExpectation.class);
        expectations.put(Part.BODY, BodyExpectation.class);
    }

    /**
     * constructor
     */
    private Expectations() {
    }

    /**
     * @param part
     * @param parse
     * @return
     */
    public static <T extends Expectation> T newExpectation(Part part, String parse) {
        try {
            return (T) invokeStaticMethod(checkNotNull(expectations.get(part), "Don't know how to parse '%'.", part), "parse", parse);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            throw new IllegalArgumentException(format("Can't create an expectation for part '%s' (%s).", part, cause.getMessage()), cause);
        }
    }

}
