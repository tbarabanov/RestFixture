package com.github.restfixture.rest.parts;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Timofey B.
 */
public interface Body {

    /**
     * @return
     */
    Type getType();

    /**
     * @return
     * @throws IOException
     */
    String getText() throws IOException;

    /**
     * @return
     * @throws IOException
     */
    InputStream asInputStream() throws IOException;

    /**
     * @author Timofey B.
     */
    enum Type {
        JSON, XML, TEXT
    }
}
