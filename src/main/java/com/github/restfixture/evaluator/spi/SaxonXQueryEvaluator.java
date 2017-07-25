package com.github.restfixture.evaluator.spi;

import com.github.restfixture.evaluator.Evaluator;
import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * @author Timofey B.
 */
public class SaxonXQueryEvaluator implements Evaluator {

    private XQueryCompiler compiler;
    private DocumentBuilder builder;

    /**
     * constructor
     */
    public SaxonXQueryEvaluator() {
        Processor processor = new Processor(false);
        compiler = processor.newXQueryCompiler();
        builder = processor.newDocumentBuilder();
        builder.setWhitespaceStrippingPolicy(WhitespaceStrippingPolicy.ALL);
    }

    @Override
    public Object evaluate(Object object, String expression) {
        return evaluate(object, expression, ReturnType.STRING);
    }

    @Override
    public Object evaluate(String string, String expression) {
        return evaluate(string, expression, ReturnType.STRING);
    }

    @Override
    public Object evaluate(Object object, String expression, ReturnType returnType) {
        try {
            XdmNode doc = builder.wrap(object);
            XQueryExecutable executable = compiler.compile(expression);
            XQueryEvaluator evaluator = executable.load();
            evaluator.setContextItem(doc);
            return valueByType(evaluator, returnType);
        } catch (SaxonApiException e) {
            throw new IllegalArgumentException(format("Can't apply expression '%s' to '%s'", expression, object), e);
        }
    }

    @Override
    public Object evaluate(String string, String expression, ReturnType returnType) {
        try {
            StringReader reader = new StringReader(string);
            XdmNode doc = builder.build(new StreamSource(reader));
            XQueryExecutable executable = compiler.compile(expression);
            XQueryEvaluator selector = executable.load();
            selector.setContextItem(doc);
            return valueByType(selector, returnType);
        } catch (SaxonApiException e) {
            throw new IllegalArgumentException(format("Can't apply expression '%s' to '%s'", expression, string), e);
        }
    }

    /**
     * @param evaluator
     * @param returnType
     * @return
     * @throws SaxonApiException
     */
    private Object valueByType(XQueryEvaluator evaluator, ReturnType returnType) throws SaxonApiException {
        switch (returnType) {
            case STRING:
                return valueOf(evaluator.evaluate());
            case SINGLE:
                return evaluator.evaluateSingle();
            case SET:
                return evaluator.evaluate();
            default:
                throw new UnsupportedOperationException(format("Class '%s' doesn't support to '%s' return type conversion.", getClass().getName(), returnType));
        }
    }
}
