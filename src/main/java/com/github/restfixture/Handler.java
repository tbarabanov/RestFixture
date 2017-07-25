package com.github.restfixture;

import fit.Parse;

/**
 * @author Timofey B.
 */
public interface Handler {

    /**
     * @param parse
     */
    void handle(Parse parse);
}
