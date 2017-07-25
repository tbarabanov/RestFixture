package com.github.restfixture.assertion;

import com.github.restfixture.difference.MapDifference;
import com.github.restfixture.difference.impl.MapDifferenceImpl;
import com.github.restfixture.difference.impl.ValueDifferenceImpl;
import com.github.restfixture.expectation.HeadersExpectation;
import com.github.restfixture.expectation.common.ExpectedValue;
import com.github.restfixture.rest.parts.Header;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.restfixture.assertion.AssertionResult.Status.FAILED;
import static com.github.restfixture.assertion.AssertionResult.Status.IGNORED;
import static com.github.restfixture.assertion.AssertionResult.Status.PASSED;
import static com.github.restfixture.evaluator.Evaluators.evaluate;

/**
 * @author Timofey B.
 */
public class HeadersAssertion extends Assertion<HeadersExpectation, List<Header>> {

    /**
     * @param expected
     * @param actual
     */
    public HeadersAssertion(HeadersExpectation expected, List<Header> actual) {
        super(expected, actual);
    }

    @Override
    public AssertionResult<MapDifference> check() {
        MapDifference difference = new MapDifferenceImpl();
        if (expected.getValue().isEmpty()) {
            return new AssertionResult<>(IGNORED, difference, this);
        }

        List<ExpectedValue> copy = new ArrayList<>(expected.getValue());
        for (Iterator<ExpectedValue> it = copy.iterator(); it.hasNext(); ) {
            ExpectedValue o = it.next();
            for (Header header : actual) {
                if (o.getValue().equals(header.getName())) {
                    if (!(boolean) evaluate(header.getValue(), o.getExpression())) {
                        difference.addDifference(o.getValue(), new ValueDifferenceImpl(o.getExpression(), header.getValue()));
                    }
                    it.remove();
                }
            }
        }

        if (copy.size() > 0) {
            for (ExpectedValue o : copy) {
                difference.addDifference(o.getValue(), new ValueDifferenceImpl(o.getExpression(), "not-found"));
            }
        }

        return difference.areEqual() ? new AssertionResult<>(PASSED, difference, this) :
                new AssertionResult<>(FAILED, difference, this);
    }
}
