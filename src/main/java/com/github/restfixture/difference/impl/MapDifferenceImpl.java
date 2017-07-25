package com.github.restfixture.difference.impl;

import com.github.restfixture.difference.MapDifference;
import com.github.restfixture.difference.ValueDifference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Timofey B.
 */
public class MapDifferenceImpl implements MapDifference {

    private Map<Object, ValueDifference> entriesDiffering;

    /**
     * constructor
     */
    public MapDifferenceImpl() {
        this(new HashMap<Object, ValueDifference>());
    }

    /**
     * @param entriesDiffering
     */
    public MapDifferenceImpl(Map<Object, ValueDifference> entriesDiffering) {
        this.entriesDiffering = entriesDiffering;
    }

    @Override
    public boolean areEqual() {
        return entriesDiffering.isEmpty();
    }

    @Override
    public Map<Object, ValueDifference> entriesDiffering() {
        return entriesDiffering;
    }

    @Override
    public void addDifference(Object key, ValueDifference value) {
        entriesDiffering.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Iterator<Map.Entry<Object, ValueDifference>> it = entriesDiffering.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Object, ValueDifference> e = it.next();
            builder.append(e.getKey()).append(" => ").append(e.getValue()).append("\n");
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) :
                builder.toString();
    }

}
