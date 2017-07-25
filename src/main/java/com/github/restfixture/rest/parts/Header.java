package com.github.restfixture.rest.parts;

/**
 * @author Timofey B.
 */
public class Header {

    private String name;
    private String value;

    /**
     * @param name
     * @param value
     */
    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ":" + value;
    }
}
