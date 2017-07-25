package com.github.restfixture.configuration.internal;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Timofey B.
 */
public class DirectedCycle {

    private boolean[] marked;
    private int[] edgeTo;
    private Deque<Integer> cycle;
    private boolean[] onStack;

    /**
     * @param G
     */
    public DirectedCycle(DirectedGraph G) {
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
    }

    /**
     * @param G
     * @param v
     */
    private void dfs(DirectedGraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adjacent(v)) {
            if (hasCycle()) return;
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            } else if (onStack[w]) {
                cycle = new ArrayDeque<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    /**
     * @return
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * @return
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }

    /**
     * @return
     */
    public boolean isDAG() {
        return !hasCycle();
    }

}
