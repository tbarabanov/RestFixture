package com.github.restfixture.difference;


/**
 * @author Timofey B.
 */
public interface ValueDifference {

    /**
     * @return
     */
    Object leftValue();

    /**
     * @return
     */
    Object rightValue();

    /**
     * @return
     */
    String toString();
}
