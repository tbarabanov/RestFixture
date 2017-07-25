package com.github.restfixture.difference;

import java.util.Map;

/**
 * @author Timofey B.
 */
public interface MapDifference {
    /**
     * @return
     */
    boolean areEqual();

    /**
     * @return
     */
    Map<Object, ValueDifference> entriesDiffering();

    /**
     * @param key
     * @param value
     */
    void addDifference(Object key, ValueDifference value);

    /**
     * @return
     */
    String toString();


}
