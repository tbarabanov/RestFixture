/**
 *
 */
package com.github.restfixture.rest.parts;

import static java.lang.String.format;

/**
 * @author Timofey B.
 */
public class StatusLine {

    private int statusCode;
    private String reasonPhrase;

    /**
     * @param statusCode
     * @param reasonPhrase
     */
    public StatusLine(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return the reasonPhrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String toString() {
        return format("%s %s", statusCode, reasonPhrase);
    }
}
