package io.quarkiverse.jgrapht.it.guava;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;

public class JgraphtGuavaUtils {

    public static <T> void generateRandomGraphContent(Graph<String, T> g) {
        Random r = new Random();
        ArrayList<String> nodes = new ArrayList<>();
        for (int i = 0; i < (5 + r.nextInt(30)); i++) {
            String nodeName = "x" + r.nextInt(26);
            g.addVertex(nodeName);
            nodes.add(nodeName);
        }
        for (int i = 0; i < r.nextInt(nodes.size()); i++) {
            g.addEdge(nodes.get(r.nextInt(nodes.size())), nodes.get(r.nextInt(nodes.size())));
        }
    }

    public static <T> void generateGraphContent(Graph<String, T> g) {
        g.addVertex("v1");
        g.addVertex("v2");
        g.addVertex("v3");
        g.addVertex("v4");
        g.addVertex("v5");
        g.addEdge("v1", "v2");
        g.addEdge("v2", "v3");
        g.addEdge("v2", "v4");
        g.addEdge("v4", "v4");
        g.addEdge("v5", "v5");
        g.addEdge("v5", "v2");
    }

    public static void generateGraphContent(com.google.common.graph.MutableGraph<String> g) {
        g.addNode("v1");
        g.addNode("v2");
        g.addNode("v3");
        g.addNode("v4");
        g.addNode("v5");
        g.putEdge("v1", "v2");
        g.putEdge("v2", "v3");
        g.putEdge("v2", "v4");
        g.putEdge("v4", "v4");
        g.putEdge("v5", "v5");
        g.putEdge("v5", "v2");
    }

    public static void generateGraphContent(com.google.common.graph.MutableNetwork<String, DefaultEdge> network) {
        network.addNode("v1");
        network.addNode("v2");
        network.addNode("v3");
        network.addNode("v4");
        network.addNode("v5");
        DefaultEdge e12 = new DefaultEdge();
        network.addEdge("v1", "v2", e12);
        DefaultEdge e23_1 = new DefaultEdge();
        network.addEdge("v2", "v3", e23_1);
        DefaultEdge e23_2 = new DefaultEdge();
        network.addEdge("v2", "v3", e23_2);
        DefaultEdge e24 = new DefaultEdge();
        network.addEdge("v2", "v4", e24);
        DefaultEdge e44 = new DefaultEdge();
        network.addEdge("v4", "v4", e44);
        DefaultEdge e55_1 = new DefaultEdge();
        network.addEdge("v5", "v5", e55_1);
        DefaultEdge e52 = new DefaultEdge();
        network.addEdge("v5", "v2", e52);
        DefaultEdge e55_2 = new DefaultEdge();
        network.addEdge("v5", "v5", e55_2);
    }

    public static <T> void generateGraphContentForAlgorithm(com.google.common.graph.MutableGraph<String> g) {
        g.addNode("ul");
        g.addNode("um");
        g.addNode("ur");
        g.addNode("ml");
        g.addNode("mm");
        g.addNode("mr");
        g.addNode("ll");
        g.addNode("lm");
        g.addNode("lr");
        g.putEdge("ul", "um");
        g.putEdge("um", "ur");
        g.putEdge("ml", "mm");
        g.putEdge("mm", "mr");
        g.putEdge("ll", "lm");
        g.putEdge("lm", "lr");
        g.putEdge("ul", "ml");
        g.putEdge("ml", "ll");
        g.putEdge("um", "mm");
        g.putEdge("mm", "lm");
        g.putEdge("ur", "mr");
        g.putEdge("mr", "lr");
    }

    public static <T> String getGraphAsDot(Graph<String, T> g) {
        StringWriter w = new StringWriter();
        new DOTExporter<String, T>().exportGraph(g, w);
        return w.toString();
    }

}
