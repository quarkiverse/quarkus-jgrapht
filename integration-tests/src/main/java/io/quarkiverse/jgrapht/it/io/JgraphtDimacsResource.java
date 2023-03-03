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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Triple;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.dimacs.DIMACSEventDrivenImporter;
import org.jgrapht.nio.dimacs.DIMACSExporter;
import org.jgrapht.nio.dimacs.DIMACSFormat;
import org.jgrapht.nio.dimacs.DIMACSImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/dimacs")
@ApplicationScoped
public class JgraphtDimacsResource {
    private static final Function<Integer, String> NAME_PROVIDER = v -> String.valueOf(v);
    private static final String NL = System.getProperty("line.separator");

    @GET
    @Path("/export")
    public String exportGraph() {
        Graph<Integer, DefaultEdge> g = new DirectedPseudograph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 1);
        g.addEdge(5, 2);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(5, 5);

        DIMACSExporter<Integer, DefaultEdge> exporter = new DIMACSExporter<>();
        exporter.setFormat(DIMACSFormat.SHORTEST_PATH);
        StringWriter w = new StringWriter();
        exporter.exportGraph(g, w);

        return w.toString();
    }

    @GET
    @Path("/import/event-driven")
    public String importGraphEventDriven() {
        String input = "c FILE: myciel3.col\n" +
                "c SOURCE: Michael Trick (trick@cmu.edu)\n" +
                "c DESCRIPTION: Graph based on Mycielski transformation. \n" +
                "c              Triangle free (clique number 2) but increasing\n" +
                "c              coloring number\n" +
                "p edge 11 20\n" +
                "e 1 2 1\n" +
                "e 1 4 2\n" +
                "e 1 7 3\n" +
                "e 1 9 4\n" +
                "e 2 3 5\n" +
                "e 2 6 6\n" +
                "e 2 8 7\n" +
                "e 3 5 8\n" +
                "e 3 7 9\n" +
                "e 3 10 10\n" +
                "e 4 5 11\n" +
                "e 4 6 12\n" +
                "e 4 10 13\n" +
                "e 5 8 14\n" +
                "e 5 9 15\n" +
                "e 6 11 16\n" +
                "e 7 11 17\n" +
                "e 8 11 18\n" +
                "e 9 11 19\n" +
                "e 10 11 20";

        DIMACSEventDrivenImporter importer = new DIMACSEventDrivenImporter();
        importer = importer.renumberVertices(false).zeroBasedNumbering(false);

        List<Triple<Integer, Integer, Double>> collected = new ArrayList<>();
        importer.addEdgeConsumer(t -> {
            collected.add(t);
        });
        importer.importInput(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        return collected.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = "p sp 3 3\n" +
                "a 1 2\n" +
                "a 2 1\n" +
                "a 2 3\n";

        Graph<Integer, DefaultWeightedEdge> graph = readGraph(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                DefaultWeightedEdge.class, false);

        return graph.toString();
    }

    public <E> Graph<Integer, E> readGraph(InputStream in, Class<E> edgeClass, boolean weighted) {
        Graph<Integer, E> g = GraphTypeBuilder
                .directed().allowingMultipleEdges(true).allowingSelfLoops(true).weighted(weighted)
                .vertexSupplier(SupplierUtil.createIntegerSupplier()).edgeClass(edgeClass).buildGraph();

        DIMACSImporter<Integer, E> importer = new DIMACSImporter<>();
        try {
            importer.importGraph(g, new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return g;
    }

}
