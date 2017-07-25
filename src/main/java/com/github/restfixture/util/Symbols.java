package com.github.restfixture.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static fit.Fixture.getSymbol;
import static fit.Fixture.hasSymbol;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.quote;

/**
 * @author Timofey B.
 */
public class Symbols {

    private static final String VAR = "[a-zA-Z0-9]{1,5}";

    private static final Pattern PATTERN = Pattern.compile(format("(?<key>%1$s(?<var>%2$s)%1$s)", quote("$"), VAR));
    //Pattern.compile("(?<var>" + quote(QUOTE) + VAR + quote(QUOTE) + ")"); \\$[a-zA-Z]{1,5}\\$

    /**
     * constructor
     */
    private Symbols() {
    }

    /**
     * @param name
     * @return
     */
    public static String toKey(String name) {
        String parse = checkNotNull(name).trim();
        checkArgument(parse.matches(VAR), "Name '%s' is not allowed. Valid PATTERN is '%s'.", parse, VAR);
        return parse;
    }

    /**
     * @param string
     * @return
     */
    public static String substitute(String string) {
        String parse = checkNotNull(string).trim();
        Matcher matcher = PATTERN.matcher(parse);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            if (!hasSymbol(matcher.group("var")))
                continue;
            matcher.appendReplacement(buffer, quoteReplacement(valueOf(getSymbol(matcher.group("var")))));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

}
