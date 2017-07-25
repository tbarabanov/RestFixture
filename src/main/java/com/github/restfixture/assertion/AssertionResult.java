package com.github.restfixture.assertion;

/**
 * @author Timofey B.
 */
public class AssertionResult<T> {

    private Status status;
    private T difference;
    private Assertion<?, ?> assertion;

    /**
     * @param status
     * @param difference
     * @param assertion
     */
    public AssertionResult(Status status, T difference, Assertion<?, ?> assertion) {
        this.status = status;
        this.difference = difference;
        this.assertion = assertion;
    }

    /**
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return
     */
    public T getDifference() {
        return difference;
    }

    /**
     * @return
     */
    public Assertion<?, ?> getAssertion() {
        return assertion;
    }

    /**
     * @author Timofey B.
     */
    public enum Status {
        PASSED, FAILED, IGNORED
    }

}
