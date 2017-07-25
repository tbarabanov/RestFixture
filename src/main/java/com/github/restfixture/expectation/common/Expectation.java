package com.github.restfixture.expectation.common;

import java.util.Collection;

/**
 * @author Timofey B.
 */
public interface Expectation {

    /**
     * @return
     */
    Collection<ExpectedValue> getValue();
}
