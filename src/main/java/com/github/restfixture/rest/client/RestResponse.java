/**
 *
 */
package com.github.restfixture.rest.client;

import com.github.restfixture.rest.parts.Header;
import com.github.restfixture.rest.parts.Part;
import com.github.restfixture.rest.parts.StatusLine;
import com.github.restfixture.rest.parts.Body;

import java.util.List;

/**
 * @author Timofey B.
 */
public class RestResponse {

    private StatusLine statusLine;
    private List<Header> headers;
    private Body body;

    /**
     * @param statusLine
     * @param headers
     * @param body
     */
    public RestResponse(StatusLine statusLine, List<Header> headers, Body body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    /**
     * @return the statusLine
     */
    public StatusLine getStatusLine() {
        return statusLine;
    }

    /**
     * @return the headers
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * @return the body
     */
    public Body getBody() {
        return body;
    }

    /**
     * @param part
     * @return
     */
    public Object getPart(Part part) {
        switch (part) {
            case STATUS_LINE:
                return statusLine;
            case HEADERS:
                return headers;
            case BODY:
                return body;
            default:
                return null;
        }
    }

}
