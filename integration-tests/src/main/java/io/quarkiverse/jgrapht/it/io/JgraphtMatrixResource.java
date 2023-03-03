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

import java.io.StringWriter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.GraphExporter;
import org.jgrapht.nio.matrix.MatrixExporter;

@Path("/jgrapht/matrix")
@ApplicationScoped
public class JgraphtMatrixResource {

    @GET
    @Path("/export")
    public String exportGraph() {
        Graph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addEdge("v1", "v2");
        graph.addVertex("v3");
        graph.addEdge("v3", "v1");

        GraphExporter<String, DefaultEdge> exporter = new MatrixExporter<>(MatrixExporter.Format.SPARSE_LAPLACIAN_MATRIX);
        StringWriter w = new StringWriter();
        exporter.exportGraph(graph, w);

        return w.toString();
    }
}
