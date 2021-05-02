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
package io.quarkiverse.jgrapht.it.unimi;

import java.io.File;
import java.io.StringWriter;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.webgraph.ImmutableDirectedBigGraphAdapter;
import org.jgrapht.webgraph.ImmutableUndirectedBigGraphAdapter;

import it.unimi.dsi.big.webgraph.EFGraph;
import it.unimi.dsi.big.webgraph.ImmutableGraph;
import it.unimi.dsi.webgraph.ArrayListMutableGraph;
import it.unimi.dsi.webgraph.Transform;
import it.unimi.dsi.webgraph.examples.ErdosRenyiGraph;

@Path("/jgrapht/webgraph")
@ApplicationScoped
public class JgraphtWebGraphResource {

    @GET
    @Path("directed")
    public String testWebGraphDirected() {
        final it.unimi.dsi.webgraph.ImmutableGraph mg = new ArrayListMutableGraph(new ErdosRenyiGraph(500, .1, 0L, true))
                .immutableView();
        final ImmutableGraph g = ImmutableGraph.wrap(mg);
        final ImmutableDirectedBigGraphAdapter directedBigGraphAdapter = new ImmutableDirectedBigGraphAdapter(
                g, ImmutableGraph.wrap(Transform.transpose(mg)));

        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(directedBigGraphAdapter, w);
        return w.toString();
    }

    @GET
    @Path("undirected")
    public String testWebGraphUndirected() {
        final ArrayListMutableGraph m = new ArrayListMutableGraph();
        m.addNodes(4);
        m.addArc(0, 1);
        m.addArc(0, 2);
        m.addArc(1, 3);
        m.addArc(2, 3);
        m.addArc(1, 1);
        m.addArc(3, 3);

        final ImmutableUndirectedBigGraphAdapter undirectedBigGraphAdapter = new ImmutableUndirectedBigGraphAdapter(
                ImmutableGraph.wrap(Transform.symmetrize(m.immutableView())));

        StringWriter w = new StringWriter();
        new DOTExporter().exportGraph(undirectedBigGraphAdapter, w);
        return w.toString();
    }

    @GET
    @Path("store")
    public String testEFGraphStore() throws Exception {
        final it.unimi.dsi.webgraph.ArrayListMutableGraph m = new ArrayListMutableGraph();
        m.addNodes(3);
        m.addArc(0, 1);
        m.addArc(0, 2);
        m.addArc(1, 2);
        m.addArc(2, 2);

        String storedFilePath = storeGraph(Transform.transpose(m.immutableView())).toString();
        final it.unimi.dsi.webgraph.ImmutableGraph g = it.unimi.dsi.webgraph.ImmutableGraph.load(storedFilePath);

        return storedFilePath;
    }

    public static File storeGraph(final it.unimi.dsi.webgraph.ImmutableGraph g) throws Exception {
        final File basename = File.createTempFile(JgraphtWebGraphResource.class.getSimpleName(), "test");
        it.unimi.dsi.webgraph.EFGraph.store(g, basename.toString());
        basename.deleteOnExit();
        new File(basename + EFGraph.GRAPH_EXTENSION).deleteOnExit();
        new File(basename + EFGraph.PROPERTIES_EXTENSION).deleteOnExit();
        new File(basename + EFGraph.OFFSETS_EXTENSION).deleteOnExit();
        return basename;
    }

    @GET
    @Path("store/big")
    public String testBigEFGraphStore() throws Exception {
        final ArrayListMutableGraph m = new ArrayListMutableGraph();
        m.addNodes(4);
        m.addArc(0, 1);
        m.addArc(0, 2);
        m.addArc(1, 3);
        m.addArc(2, 3);
        m.addArc(1, 1);
        m.addArc(3, 3);

        String storedFilePath = storeBigGraph(ImmutableGraph.wrap(Transform.symmetrize(m.immutableView()))).toString();
        final ImmutableGraph g = ImmutableGraph.load(storedFilePath);

        return storedFilePath;
    }

    public static File storeBigGraph(final ImmutableGraph g) throws Exception {
        final File basename = File.createTempFile(JgraphtWebGraphResource.class.getSimpleName(), "test");
        EFGraph.store(g, basename.toString());
        basename.deleteOnExit();
        new File(basename + EFGraph.GRAPH_EXTENSION).deleteOnExit();
        new File(basename + EFGraph.PROPERTIES_EXTENSION).deleteOnExit();
        new File(basename + EFGraph.OFFSETS_EXTENSION).deleteOnExit();
        return basename;
    }
}
