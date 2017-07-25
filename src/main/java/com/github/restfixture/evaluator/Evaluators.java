package com.github.restfixture.evaluator;

import com.github.restfixture.evaluator.spi.JsonPathEvaluator;
import com.github.restfixture.evaluator.Evaluator.ReturnType;
import com.github.restfixture.evaluator.spi.RegExEvaluator;
import com.github.restfixture.evaluator.spi.SaxonXQueryEvaluator;
import com.github.restfixture.evaluator.spi.SaxonXpathEvaluator;
import com.github.restfixture.expectation.common.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timofey B.
 */
public class Evaluators {

    public static final String REGEX = "regex";
    public static final String JSON = "json";
    public static final String XPATH = "xpath";
    public static final String XQUERY = "xquery";

    private static Map<String, Evaluator> evaluators = new HashMap<>();

    static {
        evaluators.put(REGEX, new RegExEvaluator());
        evaluators.put(JSON, new JsonPathEvaluator());
        evaluators.put(XPATH, new SaxonXpathEvaluator());
        evaluators.put(XQUERY, new SaxonXQueryEvaluator());
    }

    /**
     * constructor
     */
    private Evaluators() {
    }

    /**
     * @param string
     * @param expression
     * @return
     */
    public static Object evaluate(String string, Expression expression) {
        return evaluators.get(expression.getType()).evaluate(string, expression.getExpression());
    }

    /**
     * @param object
     * @param expression
     * @return
     */
    public static Object evaluate(Object object, Expression expression) {
        return evaluators.get(expression.getType()).evaluate(object, expression.getExpression());
    }

    /**
     * @param string
     * @param expression
     * @param returnType
     * @return
     */
    public static Object evaluate(String string, Expression expression, ReturnType returnType) {
        return evaluators.get(expression.getType()).evaluate(string, expression.getExpression(), returnType);
    }

    /**
     * @param object
     * @param expression
     * @param returnType
     * @return
     */
    public static Object evaluate(Object object, Expression expression, ReturnType returnType) {
        return evaluators.get(expression.getType()).evaluate(object, expression.getExpression(), returnType);
    }

}
