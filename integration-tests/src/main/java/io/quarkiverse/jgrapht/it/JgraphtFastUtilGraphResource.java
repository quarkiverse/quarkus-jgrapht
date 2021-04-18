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
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultGraphType;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.opt.graph.fastutil.FastutilMapGraph;
import org.jgrapht.opt.graph.fastutil.FastutilMapIntVertexGraph;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/fast-util")
@ApplicationScoped
public class JgraphtFastUtilGraphResource {

    @GET
    @Path("/int-vertex/directed")
    public String getDirectedMapIntVertexGraph() {
        Supplier<Graph<Integer, DefaultEdge>> graphSupplier = () -> new FastutilMapIntVertexGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.directedPseudograph());
        return getGraphAsDot(graphSupplier);
    }

    @GET
    @Path("/int-vertex/undirected")
    public String getUndirectedMapIntVertexGraph() {
        Supplier<Graph<Integer, DefaultEdge>> graphSupplier = () -> new FastutilMapIntVertexGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.pseudograph());
        return getGraphAsDot(graphSupplier);
    }

    @GET
    @Path("/directed")
    public String getDirectedGraph() {
        Supplier<Graph<Integer, DefaultEdge>> graphSupplier = () -> new FastutilMapGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.directedPseudograph());
        return getGraphAsDot(graphSupplier);
    }

    @GET
    @Path("/undirected")
    public String getUndirectedGraph() {
        Supplier<Graph<Integer, DefaultEdge>> graphSupplier = () -> new FastutilMapGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.pseudograph());
        return getGraphAsDot(graphSupplier);
    }

    private String getGraphAsDot(Supplier<Graph<Integer, DefaultEdge>> graphSupplier) {
        Graph<Integer, DefaultEdge> g = graphSupplier.get();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);

        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(2, 3);
        g.addEdge(2, 4);
        g.addEdge(4, 4);
        g.addEdge(5, 5);
        g.addEdge(5, 2);
        g.addEdge(5, 5);

        StringWriter w = new StringWriter();
        new DOTExporter<Integer, DefaultEdge>().exportGraph(g, w);
        return w.toString();
    }
}
