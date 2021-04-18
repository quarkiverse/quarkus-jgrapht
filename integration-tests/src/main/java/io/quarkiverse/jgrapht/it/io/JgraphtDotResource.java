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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/dot")
@ApplicationScoped
public class JgraphtDotResource {

    @GET
    @Path("/export")
    public String exportGraph() throws URISyntaxException {
        Graph<URI, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        URI google = new URI("http://www.google.com");
        URI wikipedia = new URI("http://www.wikipedia.org");
        URI jgrapht = new URI("http://www.jgrapht.org");

        graph.addVertex(google);
        graph.addVertex(wikipedia);
        graph.addVertex(jgrapht);

        graph.addEdge(jgrapht, wikipedia);
        graph.addEdge(google, jgrapht);
        graph.addEdge(google, wikipedia);
        graph.addEdge(wikipedia, google);

        DOTExporter<URI, DefaultEdge> exporter = new DOTExporter<>(uri -> uri.getHost().replace('.', '_'));
        exporter.setVertexAttributeProvider(uri -> Map.of("label", DefaultAttribute.createAttribute(uri.toString())));
        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = "digraph graphname {\n" + "     a -> b -> c;\n" + "     b -> d;\n" + " }";

        DirectedMultigraph<String, DefaultEdge> graph = new DirectedMultigraph<>(
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);

        new DOTImporter<String, DefaultEdge>()
                .importGraph(graph, new StringReader(input));
        return graph.toString();
    }

    @GET
    @Path("/import/broken")
    public String importBrokenGraph() {
        String input = "digraph graphname {\n" + "     a -> b <- c;\n" + "     b <-> d;\n" + " }";

        DirectedMultigraph<String, DefaultEdge> graph = new DirectedMultigraph<>(
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        try {
            new DOTImporter<String, DefaultEdge>()
                    .importGraph(graph, new StringReader(input));
        } catch (ImportException ex) {
            if (ex.getMessage().contains("Failed to import DOT graph") &&
                    ex.getCause() instanceof IllegalArgumentException) {
                return "OK";
            } else {
                ex.printStackTrace();
                return "FAIL - different cause";
            }
        }

        return "FAIL - no ImportException";
    }

}
