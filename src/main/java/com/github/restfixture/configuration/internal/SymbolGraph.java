package com.github.restfixture.configuration.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.lineSeparator;

/**
 * @author Timofey B.
 */
public class SymbolGraph {

    private static final Pattern VAR = Pattern.compile("\\$\\{(?<edge>.*?)\\}");

    private Map<String, Integer> st;
    private String[] keys;
    private DirectedGraph G;

    /**
     * @param value
     * @throws IOException
     */
    public SymbolGraph(String value) throws IOException {
        st = new HashMap<>();
        String[] parse = value.split(lineSeparator());
        for (String line : parse) {
            if (line.startsWith("#"))
                continue;

            String[] parts = line.split("\\s*=\\s*", 2);

            String name = parts[0];
            String part = parts[1];

            Matcher matcher = VAR.matcher(part);

            if (!st.containsKey(name)) {
                st.put(name, st.size());
            }

            while (matcher.find()) {
                String edge = matcher.group("edge");
                if (!st.containsKey(edge)) {
                    st.put(edge, st.size());
                }
            }
        }

        keys = new String[st.size()];
        for (String name : st.keySet()) {
            keys[st.get(name)] = name;
        }

        G = new DirectedGraph(st.size());
        for (String line : parse) {
            if (line.startsWith("#"))
                continue;
            
            String[] parts = line.split("\\s*=\\s*", 2);

            String name = parts[0];
            String part = parts[1];

            Matcher matcher = VAR.matcher(part);
            while (matcher.find()) {
                String edge = matcher.group("edge");
                G.addEdge(st.get(edge), st.get(name));
            }
        }
    }

    /**
     * @param v
     * @return
     */
    public String name(int v) {
        return keys[v];
    }

    /**
     * @return
     */
    public DirectedGraph G() {
        return G;
    }

}
