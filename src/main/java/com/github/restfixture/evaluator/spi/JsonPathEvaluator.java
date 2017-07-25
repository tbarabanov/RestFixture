package com.github.restfixture.evaluator.spi;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.github.restfixture.evaluator.Evaluator;

import static com.jayway.jsonpath.JsonPath.using;
import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * @author Timofey B.
 */
public class JsonPathEvaluator implements Evaluator {

    private Configuration SET_CFG;
    private Configuration SINGLE_CFG;

    /**
     * constructor
     */
    public JsonPathEvaluator() {
        SET_CFG = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS, Option.ALWAYS_RETURN_LIST);
        SINGLE_CFG = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    }

    @Override
    public Object evaluate(Object object, String expression) {
        return evaluate(object, expression, ReturnType.STRING);
    }

    @Override
    public Object evaluate(String string, String expression) {
        return evaluate(string, expression, ReturnType.STRING);
    }

    @Override
    public Object evaluate(Object object, String expression, ReturnType returnType) {
        switch (returnType) {
            case STRING:
                return valueOf(using(SINGLE_CFG).parse(object).read(expression));
            case SINGLE:
                return using(SINGLE_CFG).parse(object).read(expression);
            case SET:
                return using(SET_CFG).parse(object).read(expression);
            default:
                throw new UnsupportedOperationException(format("Class '%s' doesn't support to '%s' return type conversion.", getClass().getName(), returnType));
        }
    }

    @Override
    public Object evaluate(String string, String expression, ReturnType returnType) {
        switch (returnType) {
            case STRING:
                return valueOf(using(SINGLE_CFG).parse(string).read(expression));
            case SINGLE:
                return using(SINGLE_CFG).parse(string).read(expression);
            case SET:
                return using(SET_CFG).parse(string).read(expression);
            default:
                throw new IllegalArgumentException(format("Unknown return type '%s'", returnType));
        }
    }
}
