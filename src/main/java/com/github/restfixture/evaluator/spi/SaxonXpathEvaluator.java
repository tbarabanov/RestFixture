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
public class SaxonXpathEvaluator implements Evaluator {

    private XPathCompiler compiler;
    private DocumentBuilder builder;

    /**
     * constructor
     */
    public SaxonXpathEvaluator() {
        Processor processor = new Processor(false);
        compiler = processor.newXPathCompiler();
        builder = processor.newDocumentBuilder();
        builder.setWhitespaceStrippingPolicy(WhitespaceStrippingPolicy.ALL);
    }

    /**
     * @param selector
     * @param returnType
     * @return
     * @throws SaxonApiException
     */
    private Object valueByType(XPathSelector selector, ReturnType returnType) throws SaxonApiException {
        switch (returnType) {
            case BOOLEAN:
                return selector.effectiveBooleanValue();
            case STRING:
                return valueOf(selector.evaluate());
            case SINGLE:
                return selector.evaluateSingle();
            case SET:
                return selector.evaluate();
            default:
                throw new UnsupportedOperationException(format("Class '%s' doesn't support to '%s' return type conversion.", getClass().getName(), returnType));
        }
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
            XPathExecutable executable = compiler.compile(expression);
            XPathSelector selector = executable.load();
            selector.setContextItem(doc);
            return valueByType(selector, returnType);
        } catch (SaxonApiException e) {
            throw new IllegalArgumentException(format("Can't apply expression '%s' to '%s'", expression, object), e);
        }
    }

    @Override
    public Object evaluate(String string, String expression, ReturnType returnType) {
        try {
            StringReader reader = new StringReader(string);
            XdmNode doc = builder.build(new StreamSource(reader));
            XPathExecutable executable = compiler.compile(expression);
            XPathSelector selector = executable.load();
            selector.setContextItem(doc);
            return valueByType(selector, returnType);
        } catch (SaxonApiException e) {
            throw new IllegalArgumentException(format("Can't apply expression '%s' to '%s'", expression, string), e);
        }
    }
}
