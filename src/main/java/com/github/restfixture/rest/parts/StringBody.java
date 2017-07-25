package com.github.restfixture.rest.parts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Timofey B.
 */
public class StringBody implements Body {

    private String text;
    private Type type;

    /**
     * @param text
     */
    public StringBody(String text) {
        this(text, Type.TEXT);
    }

    /**
     * @param text
     * @param type
     */
    public StringBody(String text, Type type) {
        this.type = type;
        this.text = text;
    }

    /**
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream asInputStream() throws IOException {
        return new ByteArrayInputStream(text.getBytes());
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return text;
    }

}
