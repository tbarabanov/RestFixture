package com.github.restfixture.expectation.common;

import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class ExpectedValue {

    private Object value;
    private Expression expression;

    /**
     * @param value
     * @param expression
     */
    public ExpectedValue(Object value, Expression expression) {
        this.value = value;
        this.expression = expression;
    }

    /**
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return
     */
    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return format("%s => %s", expression, value);
    }
}
