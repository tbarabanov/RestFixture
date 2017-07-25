package com.github.restfixture;

import com.github.restfixture.assertion.AssertionResult;
import fit.Fixture;
import fit.Parse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Joiner.on;
import static com.google.common.html.HtmlEscapers.htmlEscaper;
import static fit.Fixture.label;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

/**
 * @author Timofey B.
 */
public class Formatter {

    private Fixture fixture;

    /**
     * @param fixture
     */
    public Formatter(Fixture fixture) {
        this.fixture = fixture;
    }

    /**
     * @param object
     * @return
     */
    public static String toString(Object object) {
        String escapeHtml;
        if (object != null && object.getClass().isArray()) {
            escapeHtml = on(", ").join(asList(object));
        } else if (object instanceof Collection) {
            escapeHtml = on(", ").join((Collection<?>) object);
        } else if (object instanceof Map) {
            escapeHtml = on('\n').withKeyValueSeparator(": ").join((Map<?, ?>) object);
        } else {
            escapeHtml = valueOf(object);
        }
        return htmlEscaper().escape(escapeHtml);
    }


    /**
     * @param cell
     * @param errorMessageTemplate
     * @param errorMessageArgs
     */
    public void message(Parse cell, String errorMessageTemplate,
                        Object... errorMessageArgs) {
        cell.addToBody(format(errorMessageTemplate, errorMessageArgs));
    }

    /**
     * @param cell
     * @param message
     */
    public void message(Parse cell, Object message) {
        cell.addToBody(valueOf(message));
    }

    /**
     * @param cell
     * @param message
     */
    public void replace(Parse cell, Object message) {
        cell.body = toString(message);
    }

    /**
     * @param cell
     * @param e
     */
    public void exception(Parse cell, Throwable e) {
        cell.addToTag(" class=\"error\"");

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        StringBuilder builder = new StringBuilder();
        builder.append("<hr>")
                .append("<div>")
                .append("<pre>")
                .append(htmlEscaper().escape(errors.toString()))
                .append("</pre>")
                .append("</div>");
        cell.addToBody(builder.toString());

        fixture.counts.exceptions++;
    }

    /**
     * @param cell
     * @param throwable
     */
    public void error(Parse cell, Throwable throwable) {
        error(cell, throwable.getMessage());
    }

    /**
     * @param cell
     * @param message
     */
    public void error(Parse cell, Object message) {
        cell.addToTag(" class=\"error\"");

        StringBuilder builder = new StringBuilder();
        builder.append("<hr>")
                .append("<div>")
                .append("<pre>")
                .append(htmlEscaper().escape(valueOf(message)))
                .append("</pre>")
                .append("</div>");
        cell.addToBody(builder.toString());

        fixture.counts.exceptions++;
    }

    /**
     * @param cell
     * @param result
     */
    public void passed(Parse cell, AssertionResult<?> result) {
        cell.addToTag(" class=\"pass\"");

        StringBuilder builder = new StringBuilder();
        builder.append(label("expected"))
                .append("<hr>")
                .append(generateCollapsible("Result", result.getAssertion().getActual()))
                .append(label("actual"));
        cell.addToBody(builder.toString());

        fixture.counts.right++;
    }

    /**
     * @param cell
     * @param result
     */
    public void failed(Parse cell, AssertionResult<?> result) {
        cell.addToTag(" class=\"fail\"");

        StringBuilder builder = new StringBuilder();
        builder.append(label("expected"))
                .append("<hr>")
                .append(toString(result.getAssertion().getActual()))
                .append(label("actual"))
                .append("<hr>")
                .append(toString(result.getDifference()))
                .append(label("difference"));
        cell.addToBody(builder.toString());

        fixture.counts.wrong++;
    }

    /**
     * @param cell
     */
    public void ignored(Parse cell, AssertionResult<?> result) {
        cell.addToTag(" class=\"ignore\"");

        StringBuilder builder = new StringBuilder();
        builder.append(toString(result.getAssertion().getActual()))
                .append(label("actual"))
                .append("<hr>")
                .append(label("ignored"));
        cell.addToBody(builder.toString());

        fixture.counts.ignores++;
    }

    /**
     * @param title
     * @param content
     * @return
     */
    public static String generateCollapsible(String title, Object content) {
        return generateCollapsible(title, toString(content));
    }

    /**
     * @param title
     * @param content
     */
    public static String generateCollapsible(String title, String content) {
        StringBuilder builder = new StringBuilder();
        builder.append("<div class=\"collapsible closed\">")
                .append("<ul>")
                .append("<li><a href=\"#\" class=\"expandall\">Expand</a></li>")
                .append("<li><a href=\"#\" class=\"collapseall\">Collapse</a></li>")
                .append("</ul>")
                .append("<p class=\"title\">").append(title).append("</p>")
                .append("<div>").append(content).append("</div>")
                .append("</div>");
        return builder.toString();
    }

}