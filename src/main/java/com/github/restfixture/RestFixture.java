/**
 *
 */
package com.github.restfixture;

import com.github.restfixture.assertion.Assertion;
import com.github.restfixture.assertion.AssertionResult;
import com.github.restfixture.assertion.Assertions;
import com.github.restfixture.configuration.Configuration;
import com.github.restfixture.evaluator.Evaluator;
import com.github.restfixture.expectation.Expectations;
import com.github.restfixture.expectation.common.Expectation;
import com.github.restfixture.expectation.common.Expression;
import com.github.restfixture.rest.client.RestClient;
import com.github.restfixture.rest.client.RestRequest;
import com.github.restfixture.rest.client.RestRequestBuilder;
import com.github.restfixture.rest.client.RestResponse;
import com.github.restfixture.rest.parts.Part;
import com.github.restfixture.util.Bodies;
import com.github.restfixture.util.Expressions;
import com.github.restfixture.util.Headers;
import fit.Fixture;
import fit.Parse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.github.restfixture.evaluator.Evaluators.evaluate;
import static com.github.restfixture.util.Symbols.substitute;
import static com.github.restfixture.util.Symbols.toKey;
import static java.lang.String.format;
import static org.apache.commons.lang3.reflect.ConstructorUtils.invokeConstructor;

/**
 * @author Timofey B.
 */
public class RestFixture extends Fixture {

    private Map<String, Handler> handlers = new HashMap<>();
    private Formatter formatter = new Formatter(this);
    // Rest
    private RestClient client;
    private RestResponse response;
    private RestRequestBuilder builder;
    private Configuration configuration;

    /**
     * constructor
     */
    public RestFixture() {
          /* <<< Rest Methods>>> */
        Handler restHandler = new RestHandler();
        handlers.put("GET", restHandler);
        handlers.put("POST", restHandler);
        handlers.put("PUT", restHandler);
        handlers.put("DELETE", restHandler);
          /* <<< Other >>> */
        handlers.put("Headers", new HeadersHandler());
        handlers.put("Body", new BodyHandler());
        handlers.put("let", new LetHandler());
    }

    /* <<< Overrides >>> */

    @Override
    public void doTable(Parse table) {
        initialize(table);
        super.doTable(table);
    }

    /**
     * initializer
     *
     * @param table
     */
    protected void initialize(Parse table) {
        checkArgument(args.length > 0, "Please create an url.");
        configuration = args.length > 1 ? Configuration.getInstance(args[1]) : Configuration.getDefault();

        String className = configuration.getString("rest.client.impl.class");
        try {
            client = (RestClient) invokeConstructor(Class.forName(className), configuration);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            throw new IllegalArgumentException(format("Can't instantiate RestClient class '%s' (%s).", className, cause.getMessage()), cause);
        }

        try {
            URL url = new URL(configuration.getString(args[0],
                    substitute(args[0])));
            formatter.replace(table.at(0, 0, 1), url);
            builder = new RestRequestBuilder(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(format("Can't parse the url: '%s'.", args[0]));
        }
    }

    // <<< formatting overrides >>>

    @Override
    public void exception(Parse cell, Throwable exception) {
        formatter.error(cell, exception);
    }

    /* <<< Handlers >>> */
    @Override
    public void doCells(Parse cells) {
        String name = cells.at(0).text();
        checkNotNull(handlers.get(name), "Handler '%s' is not found. Registered handlers '%s'.", name, handlers.keySet()).handle(cells);
    }

    // <<< for external usage >>>

    /**
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @return
     */
    public Collection<String> getHandlers() {
        return handlers.keySet();
    }

    /**
     * @param key
     * @param handler
     */
    public void addHandler(String key, Handler handler) {
        handlers.put(key, handler);
    }

    /**
     * @return
     */
    public RestClient getClient() {
        return client;
    }

    /**
     * @return
     */
    public RestRequestBuilder getBuilder() {
        return builder;
    }

    /**
     * @return
     */
    public RestResponse getResponse() {
        return response;
    }

    /**
     * @param response
     */
    public void setResponse(RestResponse response) {
        this.response = response;
    }

    /**
     * @return
     */
    public Formatter getFormatter() {
        return formatter;
    }

    /* <<< Handlers >>> */

    /**
     * @author Timofey B.
     */
    private class RestHandler implements Handler {

        @Override
        public void handle(Parse cells) {
            checkArgument(Part.BODY.ordinal() == cells.size() - 1, "Expected format is '%s', but only '%s' args were given.", "| GET | query | status line | headers | body |", cells.size());

            RestRequest.RestMethod method = RestRequest.RestMethod.valueOf(cells.at(Part.METHOD.ordinal()).text());

            Parse cell = cells.at(Part.QUERY.ordinal());
            String query = substitute(cell.text());

            formatter.replace(cell, query);

            try {
                response = execute(builder.with(method).with(query).build());
            } catch (Exception e) {
                formatter.exception(cells.at(Part.BODY.ordinal()), e);
                throw new IllegalStateException("Request couldn't be completed and the execution was interrupted. See details below.");
            } finally {
                builder.reset();
            }

            for (int i = Part.STATUS_LINE.ordinal(); i <= Part.BODY.ordinal(); i++) {
                Part part = Part.values()[i];

                cell = cells.at(part.ordinal());
                String text = substitute(cell.text());
                formatter.replace(cell, text);

                try {
                    AssertionResult<?> result = check(part, text);
                    show(cell, result);
                } catch (Exception e) {
                    formatter.error(cell, e);
                }
            }
        }

        /**
         * @param part
         * @param text
         * @return
         * @throws Exception
         */
        AssertionResult<?> check(Part part, String text) throws Exception {
            Expectation expectation = Expectations.newExpectation(part, text);
            Assertion<?, ?> assertion = Assertions.newAssertion(part, expectation, response.getPart(part));
            return assertion.check();
        }

        /**
         * @param cell
         * @param result
         */
        void show(Parse cell, AssertionResult<?> result) {
            switch (result.getStatus()) {
                case PASSED:
                    formatter.passed(cell, result);
                    break;
                case FAILED:
                    formatter.failed(cell, result);
                    break;
                case IGNORED:
                    formatter.ignored(cell, result);
                    break;
            }
        }

        /**
         * @param request
         * @return
         * @throws IOException
         */
        RestResponse execute(RestRequest request) throws IOException {
            return client.execute(request);
        }
    }

    /**
     * @author Timofey B.
     */
    private class LetHandler implements Handler {
        @Override
        public void handle(Parse cells) {
            checkArgument(4 == cells.size(), "Expected format is '%s', but only '%s' args were given.", "| let | var | expression | result |", cells.size());
            checkNotNull(response, "No requests have been done yet. Execute Rest request first.");

            Parse cell = cells.at(1);

            String name;
            Object object;
            try {
                name = toKey(cell.text());

                cell = cells.at(2);
                Expression expression = Expressions.newExpression(cell.text());

                cell = cells.at(3);
                object = evaluate(response.getBody().getText(), expression, Evaluator.ReturnType.SINGLE);
            } catch (Exception e) {
                formatter.error(cell, e);
                throw new IllegalStateException("Expression couldn't be evaluated and the execution was interrupted. See details below.");
            }
            setSymbol(name, object);
            formatter.message(cell, object);
        }
    }

    /**
     * @author Timofey B.
     */
    private class HeadersHandler implements Handler {
        @Override
        public void handle(Parse cells) {
            checkArgument(cells.size() == 2, "Expected format is '%s', but only '%s' args were given.", "| Headers | headers |", cells.size());
            try {
                String text = substitute(cells.at(1).text());
                formatter.replace(cells.at(1), text);
                builder.with(Headers.parse(text));
            } catch (Exception e) {
                formatter.error(cells.at(1), e);
                throw new IllegalStateException("Headers couldn't be parsed and the execution was interrupted. See details below.");
            }
        }
    }

    /**
     * @author Timofey B.
     */
    private class BodyHandler implements Handler {
        @Override
        public void handle(Parse cells) {
            checkArgument(cells.size() == 2, "Expected format is '%s', but only '%s' args were given.", "| Body | value: string |", cells.size());
            try {
                String text = substitute(cells.at(1).text());
                formatter.replace(cells.at(1), text);
                builder.with(Bodies.newBody(text));
            } catch (Exception e) {
                formatter.error(cells.at(1), e);
                throw new IllegalStateException("Request body couldn't be parsed and the execution was interrupted. See details below.");
            }
        }
    }

}

