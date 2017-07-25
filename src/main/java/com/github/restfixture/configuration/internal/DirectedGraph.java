package com.github.restfixture.configuration.internal;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Timofey B.
 */
public class DirectedGraph {

    private int V;
    private int E;
    private Collection<Integer>[] G;

    /**
     * @param V
     */
    public DirectedGraph(int V) {
        this.V = V;
        this.E = 0;
        G = (Collection<Integer>[]) new Collection[V];
        for (int i = 0; i < V; i++) {
            G[i] = new LinkedList<>();
        }
    }

    /**
     * @return
     */
    public int V() {
        return V;
    }

    /**
     * @return
     */
    public int E() {
        return E;
    }

    /**
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        G[v].add(w);
        E++;
    }

    /**
     * @param v
     * @return
     */
    public Iterable<Integer> adjacent(int v) {
        return G[v];
    }

}
