package com.github.restfixture.rest.parts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.readAllBytes;

/**
 * @author Timofey B.
 */
public class FileBody implements Body {

    private Path path;
    private Type type;

    /**
     * @param url
     * @throws URISyntaxException
     */
    public FileBody(URL url) throws URISyntaxException {
        this(url.toURI());
    }

    /**
     * @param uri
     */
    public FileBody(URI uri) {
        this(Paths.get(uri));
    }

    /**
     * @param path
     */
    public FileBody(Path path) {
        this(path, Type.TEXT);
    }

    /**
     * @param path
     * @param type
     */
    public FileBody(Path path, Type type) {
        this.path = path;
        this.type = type;
    }

    /**
     * @return
     */
    @Override
    public Type getType() {
        return type;
    }


    /**
     * @return
     */
    @Override
    public String getText() throws IOException {
        return new String(readAllBytes(path));
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public InputStream asInputStream() throws IOException {
        return newInputStream(path);
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return path.toString();
    }
}
