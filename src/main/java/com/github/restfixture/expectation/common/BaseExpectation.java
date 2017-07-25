package com.github.restfixture.expectation.common;

import java.util.Collection;

import static java.lang.String.valueOf;
import static org.apache.commons.lang.StringEscapeUtils.escapeJava;

/**
 * @author Timofey B.
 */
public abstract class BaseExpectation implements Expectation {

    private Collection<ExpectedValue> expected;

    /**
     * @param expected
     */
    public BaseExpectation(Collection<ExpectedValue> expected) {
        this.expected = expected;
    }

    @Override
    public Collection<ExpectedValue> getValue() {
        return expected;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "expected=" + escapeJava(valueOf(expected)) +
                '}';
    }
}
