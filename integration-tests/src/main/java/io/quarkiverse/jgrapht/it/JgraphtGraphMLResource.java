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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.jgrapht.nio.graphml.GraphMLImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/graphml")
@ApplicationScoped
public class JgraphtGraphMLResource {

    @GET
    @Path("/export")
    public String exportGraphML() throws URISyntaxException {
        Graph<URI, DefaultEdge> graph = getURIGraph();

        GraphMLExporter<URI, DefaultEdge> exporter = new GraphMLExporter<>(uri -> uri.toString());
        exporter.setEdgeIdProvider(new IntegerIdProvider<DefaultEdge>(0));
        exporter.setVertexAttributeProvider(uri -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("host", DefaultAttribute.createAttribute(uri.getHost().replaceAll("www.", "")));
            return m;
        });
        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("edge-name", DefaultAttribute.createAttribute(e.toString()));
            return m;
        });
        exporter.registerAttribute("host", GraphMLExporter.AttributeCategory.NODE, AttributeType.STRING);
        exporter.registerAttribute("edge-name", GraphMLExporter.AttributeCategory.EDGE, AttributeType.STRING);

        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        return writer.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() throws URISyntaxException {
        Graph<URI, DefaultEdge> graph = getURIGraph();

        GraphMLExporter<URI, DefaultEdge> exporter = new GraphMLExporter<>(uri -> uri.getHost());
        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);
        String graphAsGraphML = writer.toString();

        Graph<URI, DefaultEdge> importedGraph = new DefaultDirectedGraph(
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        GraphMLImporter<URI, DefaultEdge> importer = new GraphMLImporter<>();
        importer.setVertexFactory(stringURI -> URI.create(stringURI));
        importer.importGraph(importedGraph, new StringReader(graphAsGraphML));

        return importedGraph.toString();
    }

    @GET
    @Path("/import/broken")
    public String importBrokenGraph() throws URISyntaxException {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" " +
                "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\" "
                +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <graph edgedefault=\"directed\">\n" +
                "        <Tnode id=\"www.google.com\"/>\n" +
                "    </graph>\n" +
                "</graphml>";

        Graph<URI, DefaultEdge> importedGraph = new DefaultDirectedGraph(
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);
        GraphMLImporter<URI, DefaultEdge> importer = new GraphMLImporter<>();
        importer.setVertexFactory(stringURI -> URI.create(stringURI));

        try {
            importer.importGraph(importedGraph, new StringReader(input));
        } catch (ImportException ex) {
            if (ex.getCause().getMessage().contains(
                    "Invalid content was found starting with element '{\"http://graphml.graphdrawing.org/xmlns\":Tnode}'")) {
                return "OK";
            } else {
                ex.printStackTrace();
                return "FAIL - different cause";
            }
        }

        return "FAIL - no ImportException";
    }

    private Graph<URI, DefaultEdge> getURIGraph() throws URISyntaxException {
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

        return graph;
    }
}
