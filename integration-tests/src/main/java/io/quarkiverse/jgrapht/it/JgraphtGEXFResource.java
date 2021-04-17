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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.gexf.GEXFAttributeType;
import org.jgrapht.nio.gexf.GEXFExporter;
import org.jgrapht.nio.gexf.SimpleGEXFImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/gexf")
@ApplicationScoped
public class JgraphtGEXFResource {

    @GET
    @Path("/export")
    public String exportGraph() {
        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected().weighted(true).edgeSupplier(SupplierUtil.DEFAULT_EDGE_SUPPLIER)
                .vertexSupplier(SupplierUtil.createStringSupplier()).allowingMultipleEdges(true)
                .allowingSelfLoops(true).buildGraph();

        graph.addVertex("v1");
        graph.addVertex("v2");
        DefaultEdge e12 = graph.addEdge("v1", "v2");
        graph.addVertex("v3");
        DefaultEdge e31 = graph.addEdge("v3", "v1");
        graph.setEdgeWeight(e31, 13.5d);

        GEXFExporter<String, DefaultEdge> exporter = new GEXFExporter<>();
        exporter.setParameter(GEXFExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        exporter.setParameter(GEXFExporter.Parameter.EXPORT_EDGE_TYPES, true);
        exporter.setDescription("Test");

        exporter.registerAttribute("color", GEXFExporter.AttributeCategory.NODE, GEXFAttributeType.STRING, null);
        exporter.registerAttribute("city", GEXFExporter.AttributeCategory.NODE, GEXFAttributeType.STRING, null);
        exporter.registerAttribute("length", GEXFExporter.AttributeCategory.EDGE, GEXFAttributeType.DOUBLE, null);
        exporter.setVertexAttributeProvider(v -> {
            if ("v1".equals(v)) {
                return Map.of("color", DefaultAttribute.createAttribute("Red"),
                        "city", DefaultAttribute.createAttribute("Paris"));
            } else {
                return Collections.emptyMap();
            }
        });

        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> map = new HashMap<String, Attribute>();
            if (e == e12) {
                map.put("label", DefaultAttribute.createAttribute("Edge from node 1 to node 2"));
                map.put("length", DefaultAttribute.createAttribute("100.0"));
            }
            if (e == e31) {
                map.put("length", DefaultAttribute.createAttribute("30.0"));
            }
            return map;
        });

        StringWriter w = new StringWriter();
        exporter.exportGraph(graph, w);

        return w.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<gexf xmlns=\"http://www.gexf.net/1.2draft\" "
                + "      version=\"1.2\" "
                + "      xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" "
                + "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "  <graph defaultedgetype=\"undirected\">\n"
                + "    <nodes>\n"
                + "      <node id=\"1\" label=\"1\"/>\n"
                + "      <node id=\"2\" label=\"2\"/>\n"
                + "      <node id=\"3\" label=\"3\"/>\n"
                + "    </nodes>\n"
                + "    <edges>\n"
                + "      <edge id=\"1\" source=\"2\" target=\"3\" />\n"
                + "      <edge id=\"0\" source=\"1\" target=\"2\" />\n"
                + "      <edge id=\"2\" source=\"3\" target=\"1\" />\n"
                + "    </edges>\n"
                + "  </graph>\n"
                + "</gexf>";

        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected().weighted(false).allowingMultipleEdges(true).allowingSelfLoops(true)
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier()).buildGraph();

        new SimpleGEXFImporter<String, DefaultEdge>()
                .importGraph(graph, new StringReader(input));

        return graph.toString();
    }

    @GET
    @Path("/import/broken")
    public String importBrokenGraph() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<gexf xmlns=\"http://www.gexf.net/1.2draft\" "
                + "      version=\"1.2\" "
                + "      xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" "
                + "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                + "  <graph defaultedgetype=\"undirected\">\n"
                + "    <nodes>\n"
                + "      <node id=\"1\" label=\"1\"/>\n"
                + "      <node id=\"2\" label=\"2\"/>\n"
                + "      <node id=\"3\" label=\"3\"/>\n"
                + "    </nodes>\n";

        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected().weighted(false).allowingMultipleEdges(true).allowingSelfLoops(true)
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier()).buildGraph();

        try {
            new SimpleGEXFImporter<String, DefaultEdge>()
                    .importGraph(graph, new StringReader(input));
        } catch (ImportException ex) {
            if (ex.getCause().getMessage().contains("XML document structures must start and end within the same entity.")) {
                return "OK";
            } else {
                ex.printStackTrace();
                return "FAIL - different cause";
            }
        }

        return "FAIL - no ImportException";
    }

}
