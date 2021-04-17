/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.jgrapht.it;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.opt.graph.sparse.IncomingEdgesSupport;
import org.jgrapht.opt.graph.sparse.SparseIntDirectedGraph;

@Path("/jgrapht/sparse")
@ApplicationScoped
public class JgraphtSparseGraphResource {

    @GET
    @Path("/directed")
    public String getDirectedGraph() {
        BiFunction<Integer, List<Pair<Integer, Integer>>, Graph<Integer, Integer>> graphSupplier = (vc,
                edges) -> new SparseIntDirectedGraph(vc, edges);
        return getDirectedGraphAsDot(graphSupplier);
    }

    @GET
    @Path("/directed/lazy")
    public String getDirectedGraphLazyIncoming() {
        BiFunction<Integer, List<Pair<Integer, Integer>>, Graph<Integer, Integer>> graphSupplier = (vc,
                edges) -> new SparseIntDirectedGraph(vc, edges, IncomingEdgesSupport.LAZY_INCOMING_EDGES);
        return getDirectedGraphAsDot(graphSupplier);
    }

    private String getDirectedGraphAsDot(
            BiFunction<Integer, List<Pair<Integer, Integer>>, Graph<Integer, Integer>> graphSupplier) {
        final Integer vertexCount = 8;
        List<Pair<Integer, Integer>> edges = Arrays
                .asList(
                        Pair.of(0, 1), Pair.of(1, 0), Pair.of(1, 4), Pair.of(1, 5), Pair.of(1, 6),
                        Pair.of(2, 4), Pair.of(2, 4), Pair.of(2, 4), Pair.of(3, 4), Pair.of(4, 5),
                        Pair.of(5, 6), Pair.of(7, 6), Pair.of(7, 7));

        Graph<Integer, Integer> graph = graphSupplier.apply(vertexCount, edges);

        StringWriter w = new StringWriter();
        new DOTExporter<Integer, Integer>().exportGraph(graph, w);
        return w.toString();
    }
}
