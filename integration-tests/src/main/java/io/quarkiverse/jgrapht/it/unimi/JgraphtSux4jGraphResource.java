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
package io.quarkiverse.jgrapht.it.unimi;

import java.io.StringWriter;
import java.util.function.Supplier;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.sux4j.SuccinctDirectedGraph;
import org.jgrapht.sux4j.SuccinctIntDirectedGraph;
import org.jgrapht.sux4j.SuccinctIntUndirectedGraph;
import org.jgrapht.sux4j.SuccinctUndirectedGraph;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/sux4j")
@ApplicationScoped
@SuppressWarnings("unchecked")
public class JgraphtSux4jGraphResource {

    @GET
    @Path("/directed")
    public String testDirected() {
        final DefaultDirectedGraph<Integer, DefaultEdge> directedGraph = getDirectedGraph();
        final SuccinctDirectedGraph succinctDirectedGraph = new SuccinctDirectedGraph(directedGraph);
        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(succinctDirectedGraph, w);
        return w.toString();
    }

    @GET
    @Path("/directed/int")
    public String testIntDirected() {
        final DefaultDirectedGraph<Integer, DefaultEdge> directedGraph = getDirectedGraph();
        final SuccinctIntDirectedGraph succinctDirectedGraph = new SuccinctIntDirectedGraph(directedGraph);
        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(succinctDirectedGraph, w);
        return w.toString();
    }

    @GET
    @Path("/undirected")
    public String testUndirected() {
        final DefaultUndirectedGraph<Integer, DefaultEdge> undirectedGraph = getUndirectedGraph();
        final SuccinctUndirectedGraph succinctGraph = new SuccinctUndirectedGraph(undirectedGraph);
        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(succinctGraph, w);
        return w.toString();
    }

    @GET
    @Path("/undirected/int")
    public String testIntUndirected() {
        final DefaultUndirectedGraph<Integer, DefaultEdge> undirectedGraph = getUndirectedGraph();
        final SuccinctIntUndirectedGraph succinctGraph = new SuccinctIntUndirectedGraph(undirectedGraph);
        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(succinctGraph, w);
        return w.toString();
    }

    private DefaultDirectedGraph<Integer, DefaultEdge> getDirectedGraph() {
        final DefaultDirectedGraph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(), false);
        addGraphData(directedGraph);
        return directedGraph;
    }

    private DefaultUndirectedGraph<Integer, DefaultEdge> getUndirectedGraph() {
        final DefaultUndirectedGraph<Integer, DefaultEdge> undirectedGraph = new DefaultUndirectedGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(), false);
        addGraphData(undirectedGraph);
        return undirectedGraph;
    }

    private void addGraphData(AbstractBaseGraph<Integer, DefaultEdge> graph) {
        for (int i = 0; i < 5; i++)
            graph.addVertex(i);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 0);
        graph.addEdge(3, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 1);
    }

    @GET
    @Path("/directed/random/dense")
    public String testDirectedRandomDense() {
        final GnpRandomGraphGenerator<Integer, DefaultEdge> graphGenerator = new GnpRandomGraphGenerator<>(1000, .1, 0, false);
        final DefaultDirectedGraph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(new Supplier<Integer>() {
            private int id = 0;

            @Override
            public Integer get() {
                return id++;
            }
        }, SupplierUtil.createDefaultEdgeSupplier(), false);
        graphGenerator.generateGraph(directedGraph);
        final SuccinctDirectedGraph succinctDirectedGraph = new SuccinctDirectedGraph(directedGraph);

        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(succinctDirectedGraph, w);

        return w.toString();
    }

}
