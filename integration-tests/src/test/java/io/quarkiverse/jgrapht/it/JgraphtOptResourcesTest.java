package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JgraphtOptResourcesTest {
    @Test
    public void testSparseDirectedGraph() {
        given()
                .when().get("/jgrapht/sparse/directed")
                .then()
                .statusCode(200)
                .body(containsString(" 1 -> 2;"))
                .body(containsString("2 -> 6;"));
    }

    @Test
    public void testSparseDirectedGraphLazyIncoming() {
        given()
                .when().get("/jgrapht/sparse/directed/lazy")
                .then()
                .statusCode(200)
                .body(containsString(" 1 -> 2;"))
                .body(containsString("2 -> 6;"));
    }

    @Test
    public void testDirectedMapIntVertexGraph() {
        given()
                .when().get("/jgrapht/fast-util/int-vertex/directed")
                .then()
                .statusCode(200)
                .body(containsString("1 -> 2"))
                .body(containsString("2 -> 3"))
                .body(containsString("2 -> 4"))
                .body(containsString("4 -> 4"))
                .body(containsString("5 -> 5"))
                .body(containsString("5 -> 2"));
    }

    @Test
    public void testUndirectedMapIntVertexGraph() {
        given()
                .when().get("/jgrapht/fast-util/int-vertex/undirected")
                .then()
                .statusCode(200)
                .body(containsString("1 -- 2"))
                .body(containsString("2 -- 3"))
                .body(containsString("2 -- 4"))
                .body(containsString("4 -- 4"))
                .body(containsString("5 -- 5"))
                .body(containsString("5 -- 2"));
    }

    @Test
    public void testDirectedMapGraph() {
        given()
                .when().get("/jgrapht/fast-util/directed")
                .then()
                .statusCode(200)
                .body(containsString("1 -> 2"))
                .body(containsString("2 -> 3"))
                .body(containsString("2 -> 4"))
                .body(containsString("4 -> 4"))
                .body(containsString("5 -> 5"))
                .body(containsString("5 -> 2"));
    }

    @Test
    public void testUndirectedMapGraph() {
        given()
                .when().get("/jgrapht/fast-util/undirected")
                .then()
                .statusCode(200)
                .body(containsString("1 -- 2"))
                .body(containsString("2 -- 3"))
                .body(containsString("2 -- 4"))
                .body(containsString("4 -- 4"))
                .body(containsString("5 -- 5"))
                .body(containsString("5 -- 2"));
    }

}
