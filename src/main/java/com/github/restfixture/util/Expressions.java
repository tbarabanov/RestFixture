package com.github.restfixture.util;

import com.github.restfixture.expectation.common.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class Expressions {

    private static final Pattern PATTERN = Pattern.compile("^\\((?<type>(regex|xpath|xquery|json))\\)\\s*+(?<key>.+?)$");

    /**
     * constructor
     */
    private Expressions() {
    }

    /**
     * @param string
     * @return
     */
    public static Expression newExpression(String string) {
        String parse = checkNotNull(string).trim();
        Matcher matcher = PATTERN.matcher(parse);
        if (!matcher.matches())
            throw new IllegalArgumentException(format("Token '%s' is not a valid token.", parse));
        return new Expression(matcher.group("type"), matcher.group("key"));
    }

}
