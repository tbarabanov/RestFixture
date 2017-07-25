package com.github.restfixture.evaluator.spi;

import com.github.restfixture.evaluator.Evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * @author Timofey B.
 */
public class RegExEvaluator implements Evaluator {

    @Override
    public Object evaluate(Object object, String expression) {
        return evaluate(valueOf(object), expression);
    }

    @Override
    public Object evaluate(String string, String expression) {
        return evaluate(string, expression, ReturnType.BOOLEAN);
    }

    @Override
    public Object evaluate(Object object, String expression, ReturnType returnType) {
        return evaluate(valueOf(object), expression, returnType);
    }

    @Override
    public Object evaluate(String string, String expression, ReturnType returnType) {
        switch (returnType) {
            case BOOLEAN:
                return string.matches(expression);
            case SINGLE:
            case STRING: {
                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(string);
                return matcher.find() ? string.substring(matcher.start(), matcher.end()) : null;
            }
            case SET: {
                Collection<String> collection = new ArrayList<>();
                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(string);
                while (matcher.find()) {
                    collection.add(string.substring(matcher.start(), matcher.end()));
                }
                return collection;
            }
            default:
                throw new UnsupportedOperationException(format("Class '%s' doesn't support to '%s' return type conversion.", getClass().getName(), returnType));
        }
    }

}
