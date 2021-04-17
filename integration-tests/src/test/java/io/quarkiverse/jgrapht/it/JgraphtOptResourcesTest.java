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
}
