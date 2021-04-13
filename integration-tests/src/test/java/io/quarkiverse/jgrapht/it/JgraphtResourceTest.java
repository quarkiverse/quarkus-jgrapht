package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static final String DIRECTED_CSV = ";1;2;3;4;5\n"
            + "1;0;1;1;0;0\n"
            + "2;0;0;0;0;0\n"
            + "3;1;0;0;1;0\n"
            + "4;0;0;0;0;1\n"
            + "5;1;1;1;1;1\n";

    private static final String DIRECTED_DIMACS = "c\n" +
            "c SOURCE: Generated using the JGraphT library\n" +
            "c\n" +
            "p sp 5 10\n" +
            "a 1 2\n" +
            "a 1 3\n" +
            "a 3 1\n" +
            "a 3 4\n" +
            "a 4 5\n" +
            "a 5 1\n" +
            "a 5 2\n" +
            "a 5 3\n" +
            "a 5 4\n" +
            "a 5 5\n";

    @Test
    public void testCSVExport() {
        String responseString = given()
                .when().get("/jgrapht/csv/export")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals(DIRECTED_CSV, responseString);
    }

    @Test
    public void testCSVImport() {
        given()
                .when().get("/jgrapht/csv/import")
                .then()
                .statusCode(200)
                .body(equalTo("([1, 2, 3, 4, 5], [(1,2), (1,3), (3,1), (3,4), (4,5), (5,1), (5,2), (5,3), (5,4), (5,5)])"));
    }

    @Test
    public void testDimacsExport() {
        String responseString = given()
                .when().get("/jgrapht/dimacs/export")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals(DIRECTED_DIMACS, responseString);
    }

    @Test
    public void testDimacsImport() {
        given()
                .when().get("/jgrapht/dimacs/import")
                .then()
                .statusCode(200)
                .body(equalTo("([0, 1, 2], [(0,1), (1,0), (1,2)])"));
    }

    @Test
    public void testDimacsImportEventDriven() {
        given()
                .when().get("/jgrapht/dimacs/import/event-driven")
                .then()
                .statusCode(200)
                .body(equalTo(
                        "[(1,2,1.0), (1,4,2.0), (1,7,3.0), (1,9,4.0), (2,3,5.0), (2,6,6.0), (2,8,7.0), (3,5,8.0), (3,7,9.0), (3,10,10.0), (4,5,11.0), (4,6,12.0), (4,10,13.0), (5,8,14.0), (5,9,15.0), (6,11,16.0), (7,11,17.0), (8,11,18.0), (9,11,19.0), (10,11,20.0)]"));
    }
}
