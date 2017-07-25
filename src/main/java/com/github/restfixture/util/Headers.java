package com.github.restfixture.util;

import com.github.restfixture.rest.parts.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;

/**
 * @author Timofey B.
 */
public class Headers {

    private static final Pattern PATTERN = Pattern.compile("^(?<key>.+?)\\s*+(?::)\\s*+(?<value>.+)$");

    /**
     * constructor
     */
    private Headers() {
    }

    /**
     * @param string
     * @return
     */
    public static List<Header> parse(String string) {
        String parse = checkNotNull(string).trim();

        String[] parts = parse.split(lineSeparator());

        if (parts.length == 0) return null;

        List<Header> headers = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.length() == 0) {
                continue;
            }

            Matcher matcher = PATTERN.matcher(part);

            if (!matcher.matches()) {
                throw new IllegalArgumentException(format("Token '%s' is not a valid token.", part));
            }

            headers.add(new Header(matcher.group("key"), matcher.group("value")));
        }
        return headers;
    }

}
