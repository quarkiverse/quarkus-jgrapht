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
package io.quarkiverse.jgrapht.it.guava;

import static io.quarkiverse.jgrapht.it.guava.JgraphtGuavaUtils.generateGraphContent;
import static io.quarkiverse.jgrapht.it.guava.JgraphtGuavaUtils.getGraphAsDot;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.guava.ImmutableGraphAdapter;
import org.jgrapht.graph.guava.ImmutableNetworkAdapter;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

@Path("/jgrapht/guava/immutable")
@ApplicationScoped
public class JgraphtGuavaGraphImmutableResource {

    @GET
    @Path("graph-adapter/directed")
    public String getDirectedGraph() {
        MutableGraph<String> graph = GraphBuilder.directed().allowsSelfLoops(true).build();
        generateGraphContent(graph);

        Graph<String, EndpointPair<String>> immutableGraph = new ImmutableGraphAdapter<>(ImmutableGraph.copyOf(graph));
        return getGraphAsDot(immutableGraph);
    }

    @GET
    @Path("graph-adapter/undirected")
    public String getUndirectedGraph() {
        MutableGraph<String> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();
        generateGraphContent(graph);

        Graph<String, EndpointPair<String>> immutableGraph = new ImmutableGraphAdapter<>(ImmutableGraph.copyOf(graph));
        return getGraphAsDot(immutableGraph);
    }

    @GET
    @Path("network-adapter/directed")
    public String getNetworkDirectedGraph() {
        MutableNetwork<String, DefaultEdge> network = NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(true)
                .build();
        generateGraphContent(network);

        Graph<String, DefaultEdge> g = new ImmutableNetworkAdapter<>(ImmutableNetwork.copyOf(network));
        return getGraphAsDot(g);
    }

    @GET
    @Path("network-adapter/undirected")
    public String getNetworkUndirectedGraph() {
        MutableNetwork<String, DefaultEdge> network = NetworkBuilder.undirected().allowsParallelEdges(true)
                .allowsSelfLoops(true).build();
        generateGraphContent(network);

        Graph<String, DefaultEdge> g = new ImmutableNetworkAdapter<>(ImmutableNetwork.copyOf(network));
        return getGraphAsDot(g);
    }

}
