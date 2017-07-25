package com.github.restfixture.expectation;

import com.github.restfixture.expectation.common.BaseExpectation;
import com.github.restfixture.expectation.common.ExpectedValue;
import com.github.restfixture.expectation.common.Expression;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.github.restfixture.evaluator.Evaluators.REGEX;
import static java.util.Arrays.asList;

/**
 * @author Timofey B.
 */
public class StatusLineExpectation extends BaseExpectation {

    /**
     * @param expectations
     */
    private StatusLineExpectation(Collection<ExpectedValue> expectations) {
        super(expectations);
    }

    /**
     * @param value
     * @return
     */
    public static StatusLineExpectation parse(String value) {
        String parse = checkNotNull(value).trim();
        return parse.isEmpty() ? new StatusLineExpectation(new ArrayList<ExpectedValue>()) :
                new StatusLineExpectation(asList(new ExpectedValue(true, new Expression(REGEX, parse))));
    }

}
