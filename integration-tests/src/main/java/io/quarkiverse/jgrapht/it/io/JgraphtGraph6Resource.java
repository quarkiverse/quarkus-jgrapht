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
package io.quarkiverse.jgrapht.it.io;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.graph6.Graph6Sparse6Exporter;
import org.jgrapht.nio.graph6.Graph6Sparse6Importer;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/graph6")
@ApplicationScoped
public class JgraphtGraph6Resource {

    @GET
    @Path("/export")
    public String exportGraph() {
        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder.undirected().edgeClass(DefaultEdge.class)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).allowingMultipleEdges(false)
                .allowingSelfLoops(false).buildGraph();

        Graphs.addAllVertices(graph, Arrays.asList(1, 2, 3, 4));

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(1, 4);

        Graph6Sparse6Exporter<Integer, DefaultEdge> exporter = new Graph6Sparse6Exporter<>(
                Graph6Sparse6Exporter.Format.SPARSE6);
        StringWriter w = new StringWriter();
        exporter.exportGraph(graph, w);

        return w.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = ":Fa@x^\n";

        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(false)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).edgeClass(DefaultEdge.class).buildGraph();

        new Graph6Sparse6Importer<Integer, DefaultEdge>()
                .importGraph(graph, new StringReader(input));
        return graph.toString();
    }

    @GET
    @Path("/import/broken")
    public String importBrokenGraph() {
        String input = ":%";

        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(false)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).edgeClass(DefaultEdge.class).buildGraph();

        try {
            new Graph6Sparse6Importer<Integer, DefaultEdge>()
                    .importGraph(graph, new StringReader(input));
        } catch (ImportException ex) {
            if (ex.getMessage().contains("Illegal character detected")) {
                return "OK";
            } else {
                ex.printStackTrace();
                return "FAIL - different cause";
            }
        }
        return "FAIL - no ImportException";
    }

}
