package com.github.restfixture.assertion;

import com.github.restfixture.difference.MapDifference;
import com.github.restfixture.difference.impl.MapDifferenceImpl;
import com.github.restfixture.difference.impl.ValueDifferenceImpl;
import com.github.restfixture.expectation.BodyExpectation;
import com.github.restfixture.expectation.common.ExpectedValue;
import com.github.restfixture.rest.parts.Body;

import java.io.IOException;

import static com.github.restfixture.evaluator.Evaluators.evaluate;

/**
 * @author Timofey B.
 */
public class BodyAssertion extends Assertion<BodyExpectation, Body> {

    /**
     * @param expected
     * @param actual
     */
    public BodyAssertion(BodyExpectation expected, Body actual) {
        super(expected, actual);
    }

    @Override
    public AssertionResult<MapDifference> check() throws IOException {
        MapDifference difference = new MapDifferenceImpl();

        if (expected.getValue().isEmpty()) {
            return new AssertionResult<>(AssertionResult.Status.IGNORED, difference, this);
        }

        for (ExpectedValue e : expected.getValue()) {
            Object var = evaluate(actual.getText(), e.getExpression());
            if (!e.getValue().equals(var)) {
                difference.addDifference(e.getExpression(), new ValueDifferenceImpl(e.getValue(), var));
            }
        }

        return difference.areEqual() ? new AssertionResult<>(AssertionResult.Status.PASSED, difference, this) :
                new AssertionResult<>(AssertionResult.Status.FAILED, difference, this);
    }
}
