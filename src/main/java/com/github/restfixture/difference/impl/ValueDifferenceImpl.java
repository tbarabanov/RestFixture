package com.github.restfixture.difference.impl;


import com.github.restfixture.difference.ValueDifference;

import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class ValueDifferenceImpl implements ValueDifference {

    private Object leftValue;
    private Object rightValue;

    /**
     * @param leftValue
     * @param rightValue
     */
    public ValueDifferenceImpl(Object leftValue, Object rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public Object leftValue() {
        return leftValue;
    }

    @Override
    public Object rightValue() {
        return rightValue;
    }

    @Override
    public String toString() {
        return format("%s != %s", leftValue, rightValue);
    }
}
