package com.github.restfixture.util;

import com.github.restfixture.rest.parts.Body;
import com.github.restfixture.rest.parts.FileBody;
import com.github.restfixture.rest.parts.StringBody;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.Resources.getResource;
import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class Bodies {

    private static final Pattern PATTERN = Pattern.compile("^(?<type>(path|content))\\s*+=\\s*+(?<value>.+?)$", Pattern.DOTALL);

    /**
     * constructor
     */
    private Bodies() {
    }

    /**
     * @param string
     * @return
     */
    public static Body newBody(String string) throws URISyntaxException {
        String parse = checkNotNull(string).trim();

        Matcher mather = PATTERN.matcher(parse);
        if (!mather.matches())
            throw new IllegalArgumentException(format("Token '%s' is not a valid token.", parse));

        String value = mather.group("value");
        String type = mather.group("type");
        switch (type) {
            case "content":
                return new StringBody(value);
            case "path":
                return new FileBody(getResource(value));
            default:
                throw new IllegalArgumentException(format("Don't know what to do with '%s'.", type));
        }
    }
}
