package com.github.restfixture.evaluator;

/**
 * @author Timofey B.
 */
public interface Evaluator {

    /**
     * @param object
     * @param expression
     * @return
     */
    Object evaluate(Object object, String expression);

    /**
     * @param string
     * @param expression
     * @return
     */
    Object evaluate(String string, String expression);

    /**
     * @param object
     * @param expression
     * @param returnType
     * @return
     */
    Object evaluate(Object object, String expression, ReturnType returnType);

    /**
     * @param string
     * @param expression
     * @param returnType
     * @return
     */
    Object evaluate(String string, String expression, ReturnType returnType);


    /**
     * @author Timofey B.
     */
    enum ReturnType {
        BOOLEAN,
        STRING,
        SINGLE,
        SET
    }
}
