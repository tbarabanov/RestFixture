package com.github.restfixture.rest.client;

import com.github.restfixture.rest.parts.Body;
import com.github.restfixture.rest.parts.Header;

import java.net.URL;
import java.util.List;

/**
 * @author Timofey B.
 */
public class RestRequestBuilder {

    private final URL url;
    private RestRequest.RestMethod method;
    private String query;
    private Body body;
    private List<Header> headers;

    /**
     * @param url
     */
    public RestRequestBuilder(URL url) {
        this.url = url;
    }

    /**
     * @param query
     * @return
     */
    public RestRequestBuilder with(String query) {
        this.query = query;
        return this;
    }

    /**
     * @param method
     * @return
     */
    public RestRequestBuilder with(RestRequest.RestMethod method) {
        this.method = method;
        return this;
    }

    /**
     * @param body
     * @return
     */
    public RestRequestBuilder with(Body body) {
        this.body = body;
        return this;
    }

    /**
     * @param headers
     * @return
     */
    public RestRequestBuilder with(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * @return
     */
    public RestRequest build() {
        return new RestRequest(method, url, query, body, headers);
    }

    /**
     * reset
     */
    public void reset() {
        this.method = null;
        this.query = null;
        this.body = null;
        this.headers = null;
    }

}
