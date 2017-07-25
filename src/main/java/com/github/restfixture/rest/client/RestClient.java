/**
 *
 */
package com.github.restfixture.rest.client;

import java.io.IOException;

/**
 * @author Timofey B.
 */
public interface RestClient {

    /**
     * @param request
     * @return
     * @throws IOException
     */
    RestResponse execute(RestRequest request) throws IOException;
}
