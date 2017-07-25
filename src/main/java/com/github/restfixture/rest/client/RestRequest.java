/**
 *
 */
package com.github.restfixture.rest.client;

import com.github.restfixture.rest.parts.Body;
import com.github.restfixture.rest.parts.Header;
import com.github.restfixture.rest.parts.Part;

import java.net.URL;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Timofey B.
 */
public class RestRequest {

    private RestMethod method;
    private URL url;
    private String query;
    private Body body;
    private List<Header> headers;

    /**
     * @param method
     * @param url
     * @param query
     * @param body
     * @param headers
     */
    public RestRequest(RestMethod method, URL url, String query, Body body, List<Header> headers) {
        this.method = checkNotNull(method, "RestMethod can't be null.");
        this.url = checkNotNull(url, "Url can't be null.");
        this.query = query;
        this.body = body;
        this.headers = headers;
    }

    /**
     * @return the method
     */
    public RestMethod getMethod() {
        return method;
    }

    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @return
     */
    public Body getBody() {
        return body;
    }

    /**
     * @return
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * @param part
     * @return
     */
    public Object getPart(Part part) {
        switch (part) {
            case METHOD:
                return method;
            case QUERY:
                return query;
            case URL:
                return url;
            case BODY:
                return body;
            case HEADERS:
                return headers;
            default:
                return null;
        }
    }

    /**
     * @author Timofey B.
     */
    public enum RestMethod {
        GET, POST, PUT, DELETE, HEAD, OPTIONS
    }
}
