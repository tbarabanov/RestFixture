package com.github.restfixture.rest.client.impl;

import com.github.restfixture.configuration.Configuration;
import com.github.restfixture.rest.client.RestClient;
import com.github.restfixture.rest.client.RestRequest;
import com.github.restfixture.rest.client.RestResponse;
import com.github.restfixture.rest.parts.Header;
import com.github.restfixture.rest.parts.StatusLine;
import com.github.restfixture.rest.parts.StringBody;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.http.util.EntityUtils.consumeQuietly;

/**
 * @author Timofey B.
 */
public class RestClientImpl implements RestClient {

    private static ResponseHandler<RestResponse> handler = new ResponseHandlerImpl();
    private HttpClient client = HttpClients.createDefault();

    /**
     * @param configuration
     */
    public RestClientImpl(Configuration configuration) {
        // Build client from configuration
    }

    /**
     * @param headers
     * @return
     */
    private static org.apache.http.Header[] transform(List<Header> headers) {

        if (headers == null) {
            return null;
        }

        org.apache.http.Header[] transformed = new org.apache.http.Header[headers.size()];

        for (int i = 0; i < transformed.length; i++) {
            Header header = headers.get(i);
            transformed[i] = new BasicHeader(header.getName(), header.getValue());
        }

        return transformed;
    }

    @Override
    public RestResponse execute(RestRequest request) throws IOException {
        try {
            switch (request.getMethod()) {
                case GET:
                    return doGet(request);
                case POST:
                    return doPost(request);
                case PUT:
                    return doPut(request);
                case DELETE:
                    return doDelete(request);
                default:
                    return null;
            }
        } catch (URISyntaxException e) {
            throw new IOException(format("Bad destination address '%s'.", request.getUrl()), e);
        }
    }

    /**
     * @param request
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private RestResponse doGet(RestRequest request) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(request.getUrl().toURI())
                .setCustomQuery(request.getQuery()).build();

        HttpGet httpGet = new HttpGet(uri);

        httpGet.setHeaders(transform(request.getHeaders()));

        return client.execute(httpGet, handler);
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private RestResponse doPost(RestRequest request) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(request.getUrl().toURI());
        httpPost.setEntity(new InputStreamEntity(request.getBody().asInputStream()));

        httpPost.setHeaders(transform(request.getHeaders()));

        return client.execute(httpPost, handler);
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private RestResponse doPut(RestRequest request) throws IOException, URISyntaxException {
        HttpPut httpPut = new HttpPut(request.getUrl().toURI());
        httpPut.setEntity(new InputStreamEntity(request.getBody().asInputStream()));

        httpPut.setHeaders(transform(request.getHeaders()));

        return client.execute(httpPut, handler);
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private RestResponse doDelete(RestRequest request) throws IOException, URISyntaxException {
        HttpDelete httpDelete = new HttpDelete(request.getUrl().toURI());

        httpDelete.setHeaders(transform(request.getHeaders()));

        return client.execute(httpDelete, handler);
    }

    /**
     * @author Timofey B.
     */
    private static class ResponseHandlerImpl implements ResponseHandler<RestResponse> {
        @Override
        public RestResponse handleResponse(HttpResponse httpResponse) throws IOException {
            HttpEntity httpEntity = httpResponse.getEntity();
            try {
                StatusLine statusLine = new StatusLine(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().getReasonPhrase());

                List<Header> headers = new ArrayList<>();
                for (org.apache.http.Header header : httpResponse.getAllHeaders()) {
                    headers.add(new Header(header.getName(), header.getValue()));
                }

                StringBody body = new StringBody(EntityUtils.toString(httpResponse.getEntity()));

                return new RestResponse(statusLine, headers, body);
            } finally {
                consumeQuietly(httpEntity);
            }
        }
    }

}
