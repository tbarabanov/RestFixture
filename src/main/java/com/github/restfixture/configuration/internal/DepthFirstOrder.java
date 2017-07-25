package com.github.restfixture.configuration.internal;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Timofey B.
 */
public class DepthFirstOrder {

    private boolean[] marked;
    private Deque<Integer> reversePost;

    /**
     * @param G
     */
    public DepthFirstOrder(DirectedGraph G) {
        reversePost = new LinkedList<>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    /**
     * @param G
     * @param v
     */
    private void dfs(DirectedGraph G, int v) {
        marked[v] = true;
        for (int w : G.adjacent(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
        reversePost.push(v);
    }

    /**
     * @return
     */
    public Iterable<Integer> reversePost() {
        return reversePost;
    }

}
