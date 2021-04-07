package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JgraphtResourceTest {

    @Test
    public void testDotExport() {
        given()
                .when().get("/jgrapht/dot")
                .then()
                .statusCode(200)
                .body(containsString("www_wikipedia_org"))
                .body(containsString("label=\"http://www.wikipedia.org\""))
                .body(containsString("www_jgrapht_org"))
                .body(containsString("www_google_com"))
                .body(containsString("www_google_com -> www_wikipedia_org"));
    }

    @Test
    public void testGraphMLExport() {
        given()
                .when().get("/jgrapht/graphml")
                .then()
                .statusCode(200)
                .body(containsString("<node id=\"http://www.wikipedia.org\">"))
                .body(containsString("<node id=\"http://www.jgrapht.org\">"))
                .body(containsString("<node id=\"http://www.google.com\">"))
                .body(containsString(">google.com</data>"))
                .body(containsString("source=\"http://www.google.com\" target=\"http://www.jgrapht.org\""))
                .body(containsString("source=\"http://www.wikipedia.org\" target=\"http://www.google.com\""));
    }

    @Test
    public void testGraphMLImport() {
        given()
                .when().get("/jgrapht/import/graphml")
                .then()
                .statusCode(200)
                .body(containsString("(www.jgrapht.org,www.wikipedia.org)"))
                .body(containsString("(www.google.com,www.jgrapht.org)"))
                .body(containsString("(www.google.com,www.wikipedia.org)"))
                .body(containsString("(www.wikipedia.org,www.google.com)"));
    }
}
