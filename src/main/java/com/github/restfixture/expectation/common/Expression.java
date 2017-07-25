package com.github.restfixture.expectation.common;

import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class Expression {

    private String type;
    private String expression;

    /**
     * @param type
     * @param expression
     */
    public Expression(String type, String expression) {
        this.type = type;
        this.expression = expression;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @return
     */
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return format("(%s) %s", type, expression);
    }
}
