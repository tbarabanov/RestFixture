package com.github.restfixture.configuration;

import com.github.restfixture.Formatter;
import fit.Fixture;
import fit.Parse;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Timofey B.
 */
public class ConfigurationFixture extends Fixture {

    private static final int KEY = 0;
    private static final int VALUE = 1;
    private Formatter formatter = new Formatter(this);

    private Configuration configuration;

    /**
     * @param table
     */
    @Override
    public void doTable(Parse table) {
        // the best place to initialize me.
        initialize();
        super.doTable(table);
    }

    /**
     * @param cells
     */
    @Override
    public void doCells(Parse cells) {
        process(cells);
    }

    /**
     * initializer
     */
    protected void initialize() {
        configuration = args.length > 0 ? Configuration.getInstance(args[0]) : Configuration.getDefault();
        try {
            if (args.length > 1)
                configuration.load(args[1].split("\\s*,\\s*"));
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Can't load properties '%s (%s)'.", args[1], e.getMessage()), e);
        }
    }

    /**
     * @param parse
     */
    protected void process(Parse parse) {
        checkArgument(parse.size() == 2, "Expected format is '%s', but only '%s' args were given.", "| key | value |", parse.size());
        configuration.put(parse.at(KEY).text(),
                parse.at(VALUE).text());
    }

    // <<< formatting overrides >>>
    @Override
    public void exception(Parse cell, Throwable exception) {
        formatter.error(cell, exception);
    }
}
