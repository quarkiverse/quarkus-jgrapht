package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JgraphtGuavaResourcesTest {

    @Test
    public void testMutableDirected() {
        testDirected("/jgrapht/guava/mutable/graph-adapter/directed");
    }

    @Test
    public void testMutableNetworkDirected() {
        testDirected("/jgrapht/guava/mutable/network-adapter/directed");
    }

    @Test
    public void testImmutableDirected() {
        testDirected("/jgrapht/guava/immutable/graph-adapter/directed");
    }

    @Test
    public void testImmutableNetworkDirected() {
        testDirected("/jgrapht/guava/immutable/network-adapter/directed");
    }

    private void testDirected(String path) {
        given()
                .when().get(path)
                .then()
                .statusCode(200)
                .body(containsString("1 -> 2"))
                .body(containsString("2 -> 3"))
                .body(containsString("2 -> 4"))
                .body(containsString("4 -> 4"))
                .body(containsString("5 -> 2"))
                .body(containsString("5 -> 5"));
    }

    @Test
    public void testMutableDirectedRandom() {
        testDirectedRandom("/jgrapht/guava/mutable/graph-adapter/directed/random");
    }

    @Test
    public void testMutableNetworkDirectedRandom() {
        testDirectedRandom("/jgrapht/guava/mutable/network-adapter/directed/random");
    }

    private void testDirectedRandom(String path) {
        ArrayList<String> responseStrings = new ArrayList<>(20);
        for (int i = 0; i < 10; i++) {
            String responseString = given()
                    .when().get(path)
                    .then().statusCode(200)
                    .extract().asString();
            responseStrings.add(i, responseString);
        }
        // If we convert a List to a HashSet and the resulting size is less than or equal to 1, then we know that all elements in the list are equal
        assertThat(new HashSet<>(responseStrings).size(), greaterThan(1));
    }

    @Test
    public void testMutableUndirected() {
        testUndirected("/jgrapht/guava/mutable/graph-adapter/undirected");
    }

    @Test
    public void testMutableNetworkUndirected() {
        testUndirected("/jgrapht/guava/mutable/network-adapter/undirected");
    }

    @Test
    public void testImmutableUndirected() {
        testUndirected("/jgrapht/guava/immutable/graph-adapter/undirected");
    }

    @Test
    public void testImmutableNetworkUndirected() {
        testUndirected("/jgrapht/guava/immutable/network-adapter/undirected");
    }

    private void testUndirected(String path) {
        given()
                .when().get(path)
                .then()
                .statusCode(200)
                .body(containsString("  2 -- 3;"))
                .body(containsString("  2 -- 4;"))
                .body(containsString("  2 -- 5;"))
                .body(containsString("  4 -- 4;"))
                .body(containsString("  5 -- 5;"));
    }

    @Test
    public void testMutableUndirectedAlgorithm() {
        given().when().get("/jgrapht/guava/mutable/graph-adapter/undirected/algorithm")
                .then()
                .statusCode(200)
                .body(equalTo("[um, ml, mr, lm]"));
    }

}
