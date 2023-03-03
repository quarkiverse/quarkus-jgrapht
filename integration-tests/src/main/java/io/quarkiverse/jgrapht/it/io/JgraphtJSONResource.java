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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.json.JSONExporter;
import org.jgrapht.nio.json.JSONImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/json")
@ApplicationScoped
public class JgraphtJSONResource {

    @GET
    @Path("/export")
    public String exportGraph() {
        Graph<Integer, DefaultEdge> graph = GraphTypeBuilder.directed().edgeClass(DefaultEdge.class)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).allowingMultipleEdges(false)
                .allowingSelfLoops(false).buildGraph();

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(1, 4);

        JSONExporter<Integer, DefaultEdge> exporter = new JSONExporter<>(v -> String.valueOf(v));
        exporter.setEdgeIdProvider(new IntegerIdProvider<>(1));

        StringWriter w = new StringWriter();
        exporter.exportGraph(graph, w);

        return w.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = "{\n"
                + "  \"nodes\": [\n"
                + "  { \"id\":\"1\" },\n"
                + "  { \"id\":\"2\" },\n"
                + "  { \"id\":\"3\" },\n"
                + "  { \"id\":\"4\" }\n"
                + "  ],\n"
                + "  \"edges\": [\n"
                + "  { \"source\":\"1\", \"target\":\"2\", \"weight\": 2.0 },\n"
                + "  { \"source\":\"1\", \"target\":\"3\", \"weight\": 3.0 },\n"
                + "  { \"source\":\"2\", \"target\":\"3\" }\n"
                + "  ]\n"
                + "}";

        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(true)
                .vertexSupplier(SupplierUtil.createStringSupplier(1))
                .edgeSupplier(SupplierUtil.DEFAULT_EDGE_SUPPLIER).buildGraph();
        new JSONImporter<String, DefaultEdge>()
                .importGraph(graph, new StringReader(input));
        return graph.toString();
    }

    @GET
    @Path("/import/broken")
    public String importBrokenGraph() {
        String input = "{\n"
                + "  \"nodes\": [\n"
                + "  { \"id\":\"1\" },\n"
                + "  { \"id\":";
        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(true)
                .vertexSupplier(SupplierUtil.createStringSupplier(1))
                .edgeSupplier(SupplierUtil.DEFAULT_EDGE_SUPPLIER).buildGraph();
        try {
            new JSONImporter<String, DefaultEdge>()
                    .importGraph(graph, new StringReader(input));
        } catch (ImportException ex) {
            if (ex.getCause().getMessage().contains("mismatched input '<EOF>'")) {
                return "OK";
            } else {
                ex.printStackTrace();
                return "FAIL - different cause";
            }
        }

        return "FAIL - no ImportException";
    }

}
