package com.github.restfixture.configuration;

import com.github.restfixture.configuration.internal.DepthFirstOrder;
import com.github.restfixture.configuration.internal.DirectedCycle;
import com.github.restfixture.configuration.internal.SymbolGraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.Resources.getResource;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.lineSeparator;
import static java.util.regex.Matcher.quoteReplacement;

/**
 * @author Timofey B.
 */
public class Configuration {

    public static final String DEFAULT = "default";

    private static final Pattern MAP_PATTERN = Pattern.compile("(?<key>.+?)\\s*+(?:=)\\s*+(?<value>.+)");
    private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{(?<var>.*?)\\}");

    private static final Map<String, Configuration> CONFIGURATIONS = new HashMap<>();

    private static final Properties DEFAULT_PROPERTIES = new Properties();

    static {
        DEFAULT_PROPERTIES.setProperty("rest.client.impl.class", "RestClientImpl");
    }

    private Properties properties;
    private String name;

    /**
     * @param name
     */
    private Configuration(String name) {
        this.name = name;
        // default properties >>>
        properties = new Properties(DEFAULT_PROPERTIES);
    }

    /**
     * @return
     */
    public static Configuration getDefault() {
        return getInstance(DEFAULT);
    }

    /**
     * @param name
     * @return
     */
    public static Configuration getInstance(String name) {
        Configuration configuration = CONFIGURATIONS.get(name);
        if (configuration == null) {
            configuration = new Configuration(name);
            CONFIGURATIONS.put(name, configuration);
        }
        return configuration;
    }

    /**
     * @param string
     * @return
     */
    private static Map<String, String> parseMap(String string) {
        String parse = checkNotNull(string).trim();

        if (parse.isEmpty()) {
            return new HashMap<>();
        }

        String[] parts = parse.split(format("\\s*%s\\s*", lineSeparator()));

        Map<String, String> entries = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            Matcher matcher = MAP_PATTERN.matcher(parts[i]);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(format("Token '%s' is not a valid token.", parts[i]));
            }
            entries.put(matcher.group("key"), matcher.group("value"));
        }
        return entries;
    }

    /**
     * @param value
     * @return
     */
    private static String resolveString0(String value, Properties properties) {
        Matcher matcher = VAR_PATTERN.matcher(value);

        if (!matcher.find())
            return value;

        StringBuffer buffer = new StringBuffer();
        matcher.appendReplacement(buffer, quoteReplacement(valueOf(properties.getProperty(matcher.group("var")))));
        while (matcher.find()) {
            matcher.appendReplacement(buffer, quoteReplacement(valueOf(properties.getProperty(matcher.group("var")))));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    /**
     * @param key
     * @return
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return properties.containsKey(key) ? properties.getProperty(key) : defaultValue;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        return properties.containsKey(key) ? Integer.valueOf(properties.getProperty(key)) : defaultValue;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return properties.containsKey(key) ? Boolean.valueOf(properties.getProperty(key)) : defaultValue;
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public Map<String, String> getMap(String key, Map<String, String> defaultValue) {
        return properties.containsKey(key) ? parseMap(properties.getProperty(key)) : defaultValue;
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param names
     * @throws IOException
     */
    public void load(String... names) throws IOException {
        try {
            // load properties >>>
            for (String name : names) {
                properties.load(getResource(name).openStream());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            properties.store(outputStream, null);

            // parse graph >>>
            SymbolGraph symbolGraph = new SymbolGraph(outputStream.toString());
            // stop if not DAG >>>
            checkDAG(symbolGraph);

            // then resolve values >>>
            DepthFirstOrder depthFirstOrder = new DepthFirstOrder(symbolGraph.G());
            for (int v : depthFirstOrder.reversePost()) {
                String name = symbolGraph.name(v);
                if (properties.getProperty(name) != null) {
                    properties.setProperty(name, resolveString(properties.getProperty(name)));
                }
            }
        } catch (Exception e) {
            properties.clear();
            throw e;
        }
    }

    /**
     * @param symbolGraph
     */
    private static void checkDAG(SymbolGraph symbolGraph) {
        DirectedCycle cycle = new DirectedCycle(symbolGraph.G());

        if (cycle.isDAG())
            return;

        StringBuilder builder = new StringBuilder();
        Iterator<Integer> it = cycle.cycle().iterator();
        if (it.hasNext()) {
            builder.append(symbolGraph.name(it.next()));
            while (it.hasNext()) {
                builder.append("->").append(symbolGraph.name(it.next()));
            }
        }
        throw new IllegalArgumentException(format("Unable to resolve: %s", builder));
    }

    /**
     * @param value
     * @return
     */
    public String resolveString(String value) {
        return resolveString0(value, properties);
    }

}
