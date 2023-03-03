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
import java.util.function.Function;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.nio.csv.CSVExporter;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.nio.csv.CSVImporter;
import org.jgrapht.util.SupplierUtil;

@Path("/jgrapht/csv")
@ApplicationScoped
public class JgraphtCsvResource {
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

        CSVExporter<Integer, DefaultEdge> exporter = new CSVExporter<>(NAME_PROVIDER, CSVFormat.MATRIX, ';');
        exporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        exporter.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, true);
        StringWriter w = new StringWriter();
        exporter.exportGraph(g, w);
        return w.toString();
    }

    @GET
    @Path("/import")
    public String importGraph() {
        String input = ";A;B;C;D;E" + NL
                + "A;0;1;1;0;0" + NL
                + "B;0;0;0;0;0" + NL
                + "C;1;0;0;1;0" + NL
                + "D;0;0;0;0;1" + NL
                + "E;1;1;1;1;1" + NL;

        Graph<String, DefaultEdge> graph = new DirectedPseudograph<>(
                SupplierUtil.createStringSupplier(1), SupplierUtil.DEFAULT_EDGE_SUPPLIER, false);

        CSVImporter<String, DefaultEdge> importer = new CSVImporter<>(CSVFormat.MATRIX, ';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, true);
        importer.importGraph(graph, new StringReader(input));

        return graph.toString();
    }

}
