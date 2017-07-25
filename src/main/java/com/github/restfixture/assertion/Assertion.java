package com.github.restfixture.assertion;

/**
 * @author Timofey B.
 */
public abstract class Assertion<E, A> {

    protected E expected;
    protected A actual;

    /**
     * @param expected
     * @param actual
     */
    public Assertion(E expected, A actual) {
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * @return
     */
    public E getExpected() {
        return expected;
    }

    /**
     * @return
     */
    public A getActual() {
        return actual;
    }

    /**
     * @return
     */
    public abstract AssertionResult<?> check() throws Exception;

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "expected=" + expected +
                ", actual=" + actual +
                "}";
    }
}
