package com.github.restfixture.expectation;

import com.github.restfixture.expectation.common.BaseExpectation;
import com.github.restfixture.evaluator.Evaluators;
import com.github.restfixture.expectation.common.ExpectedValue;
import com.github.restfixture.expectation.common.Expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

/**
 * @author Timofey B.
 */
public class HeadersExpectation extends BaseExpectation {

    private static final Pattern pattern = Pattern.compile("^(?<key>.+?)\\s*+(?::)\\s*+(?<value>.+)$");

    /**
     * @param expected
     */
    private HeadersExpectation(Collection<ExpectedValue> expected) {
        super(expected);
    }

    /**
     * @param value
     * @return
     */
    public static HeadersExpectation parse(String value) {
        String parse = checkNotNull(value).trim();

        if (parse.isEmpty()) {
            return new HeadersExpectation(new ArrayList<ExpectedValue>());
        }

        String[] parts = parse.split(lineSeparator());

        Collection<ExpectedValue> expected = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.length() == 0) {
                continue;
            }

            Matcher matcher = pattern.matcher(part);

            if (!matcher.matches()) {
                throw new IllegalArgumentException(format("Token '%s' is not a valid token.", part));
            }

            Expression expression = new Expression(Evaluators.REGEX, matcher.group("value"));
            ExpectedValue expectation = new ExpectedValue(matcher.group("key"), expression);

            expected.add(expectation);
        }
        return new HeadersExpectation(expected);
    }

}
