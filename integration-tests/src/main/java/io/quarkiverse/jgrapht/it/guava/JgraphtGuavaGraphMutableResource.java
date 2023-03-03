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
import static io.quarkiverse.jgrapht.it.guava.JgraphtGuavaUtils.generateGraphContentForAlgorithm;
import static io.quarkiverse.jgrapht.it.guava.JgraphtGuavaUtils.generateRandomGraphContent;
import static io.quarkiverse.jgrapht.it.guava.JgraphtGuavaUtils.getGraphAsDot;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexCoverAlgorithm;
import org.jgrapht.alg.vertexcover.RecursiveExactVCImpl;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.guava.MutableGraphAdapter;
import org.jgrapht.graph.guava.MutableNetworkAdapter;
import org.jgrapht.util.SupplierUtil;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.NetworkBuilder;

@Path("/jgrapht/guava/mutable")
@ApplicationScoped
public class JgraphtGuavaGraphMutableResource {

    @GET
    @Path("graph-adapter/directed")
    public String getDirectedGraph() {
        Graph<String, EndpointPair<String>> g = new MutableGraphAdapter<>(
                GraphBuilder.directed().allowsSelfLoops(true).build());
        generateGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("graph-adapter/directed/random")
    public String getDirectedGraphRandom() {
        Graph<String, EndpointPair<String>> g = new MutableGraphAdapter<>(
                GraphBuilder.directed().allowsSelfLoops(true).build());
        generateRandomGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("graph-adapter/undirected")
    public String getUndirectedGraph() {
        Graph<String, EndpointPair<String>> g = new MutableGraphAdapter<>(
                GraphBuilder.undirected().allowsSelfLoops(true).build());
        generateGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("network-adapter/directed")
    public String getNetworkDirectedGraph() {
        Graph<String, DefaultEdge> g = new MutableNetworkAdapter<>(
                NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(true).build(),
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER);
        generateGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("network-adapter/directed/random")
    public String getNetworkDirectedGraphRandom() {
        Graph<String, DefaultEdge> g = new MutableNetworkAdapter<>(
                NetworkBuilder.directed().allowsParallelEdges(true).allowsSelfLoops(true).build(),
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER);
        generateRandomGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("network-adapter/undirected")
    public String getNetworkUndirectedGraph() {
        Graph<String, DefaultEdge> g = new MutableNetworkAdapter<>(
                NetworkBuilder.undirected().allowsParallelEdges(true).allowsSelfLoops(true).build(),
                SupplierUtil.createStringSupplier(), SupplierUtil.DEFAULT_EDGE_SUPPLIER);
        generateGraphContent(g);
        return getGraphAsDot(g);
    }

    @GET
    @Path("graph-adapter/undirected/algorithm")
    public String getUndirectedGraphWithAlgorithm() {
        MutableGraph<String> g = GraphBuilder.undirected().build();
        generateGraphContentForAlgorithm(g);

        Graph<String, EndpointPair<String>> jgrapht = new MutableGraphAdapter<>(g);
        VertexCoverAlgorithm<String> alg = new RecursiveExactVCImpl<>(jgrapht);
        VertexCoverAlgorithm.VertexCover<String> cover = alg.getVertexCover();
        return cover.toString();
    }

}
