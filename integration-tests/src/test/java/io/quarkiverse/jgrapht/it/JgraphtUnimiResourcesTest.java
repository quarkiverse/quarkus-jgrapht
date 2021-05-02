package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
public class JgraphtUnimiResourcesTest {

    @Test
    public void testDirected() {
        given()
                .when().get("/jgrapht/sux4j/directed")
                .then()
                .statusCode(200)
                .body(containsString("1 -> 2"))
                .body(containsString("1 -> 3"))
                .body(containsString("2 -> 3"))
                .body(containsString("3 -> 2"))
                .body(containsString("3 -> 4"))
                .body(containsString("4 -> 1"))
                .body(containsString("4 -> 4"))
                .body(containsString("4 -> 5"))
                .body(containsString("5 -> 2"));
    }

    @Test
    public void testIntDirected() {
        given()
                .when().get("/jgrapht/sux4j/directed/int")
                .then()
                .statusCode(200)
                .body(containsString("  1 -> 2;\n" +
                        "  1 -> 3;\n" +
                        "  2 -> 3;\n" +
                        "  3 -> 2;\n" +
                        "  3 -> 4;\n" +
                        "  4 -> 1;\n" +
                        "  4 -> 4;\n" +
                        "  4 -> 5;\n" +
                        "  5 -> 2;\n"));
    }

    @Test
    public void testUndirected() {
        given()
                .when().get("/jgrapht/sux4j/undirected")
                .then()
                .statusCode(200)
                .body(containsString("4 -> 4"))
                .body(containsString("1 -> 3"))
                .body(containsString("4 -> 5"))
                .body(containsString("1 -> 2"))
                .body(containsString("1 -> 4"))
                .body(containsString("2 -> 5"))
                .body(containsString("3 -> 4"))
                .body(containsString("2 -> 3"));
    }

    @Test
    public void testIntUndirected() {
        given()
                .when().get("/jgrapht/sux4j/undirected/int")
                .then()
                .statusCode(200)
                .body(containsString("  1 -> 2;\n" +
                        "  1 -> 3;\n" +
                        "  1 -> 4;\n" +
                        "  2 -> 3;\n" +
                        "  2 -> 5;\n" +
                        "  3 -> 4;\n" +
                        "  4 -> 4;\n" +
                        "  4 -> 5;\n"));
    }

    @Test
    public void testDirectedRandomDense() {
        Response response = given().when().get("/jgrapht/sux4j/directed/random/dense");
        int responseLength = response.asByteArray().length;
        assertTrue(responseLength > 1024 * 1024, "Response has short length, just " + responseLength + " bytes");
    }

    @Test
    public void testWebGraphUndirected() {
        given()
                .when().get("/jgrapht/webgraph/undirected")
                .then()
                .statusCode(200)
                .body(containsString("  3 -- 4;\n" +
                        "  2 -- 2;\n" +
                        "  2 -- 4;\n" +
                        "  1 -- 3;\n" +
                        "  1 -- 2;\n" +
                        "  4 -- 4;\n"));
    }

    @Test
    public void testWebGraphDirected() {
        Response response = given().when().get("/jgrapht/webgraph/directed");
        int responseLength = response.asByteArray().length;
        assertTrue(responseLength > 256 * 1024, "Response has short length, just " + responseLength + " bytes");
    }

    @Test
    public void testEFGraphStore() {
        given()
                .when().get("/jgrapht/webgraph/store")
                .then()
                .statusCode(200)
                .body(containsString("JgraphtWebGraphResource"));
    }

    @Test
    public void testBigEFGraphStore() {
        given()
                .when().get("/jgrapht/webgraph/store/big")
                .then()
                .statusCode(200)
                .body(containsString("JgraphtWebGraphResource"));
    }

}
