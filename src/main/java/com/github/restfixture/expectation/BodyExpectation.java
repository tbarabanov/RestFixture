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
import static java.lang.Boolean.valueOf;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

/**
 * @author Timofey B.
 */
public class BodyExpectation extends BaseExpectation {

    private static final Pattern pattern = Pattern.compile("^\\((?<type>(regex|xpath|xquery|json))\\)\\s*+(?<key>.+?)\\s*+(?:=>)\\s*+\"(?<value>.+?)\"$", Pattern.DOTALL);

    /**
     * @param expected
     */
    private BodyExpectation(Collection<ExpectedValue> expected) {
        super(expected);
    }

    /**
     * @param value
     * @return
     */
    public static BodyExpectation parse(String value) {
        String parse = checkNotNull(value).trim();

        if (parse.isEmpty()) {
            return new BodyExpectation(new ArrayList<ExpectedValue>());
        }

        String[] parts = parse.split(format("%s+(?=\\((regex|xpath|xquery|json)\\))", lineSeparator()));

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

            Expression expression = new Expression(matcher.group("type"), matcher.group("key"));

            ExpectedValue expectation = matcher.group("type").equals(Evaluators.REGEX) ?
                    new ExpectedValue(valueOf(matcher.group("value")), expression) :
                    new ExpectedValue(matcher.group("value"), expression);

            expected.add(expectation);
        }
        return new BodyExpectation(expected);
    }

}
