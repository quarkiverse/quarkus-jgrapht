package io.quarkiverse.jgrapht.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JgraphtResourceTest {
    @Test
    public void testGraphMLExport() {
        given()
                .when().get("/jgrapht/graphml/export")
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
                .when().get("/jgrapht/graphml/import")
                .then()
                .statusCode(200)
                .body(containsString("(www.jgrapht.org,www.wikipedia.org)"))
                .body(containsString("(www.google.com,www.jgrapht.org)"))
                .body(containsString("(www.google.com,www.wikipedia.org)"))
                .body(containsString("(www.wikipedia.org,www.google.com)"));
    }

    @Test
    public void testBrokenGraphMLImport() {
        given()
                .when().get("/jgrapht/graphml/import/broken")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
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

    private static final String GEXF_DEFINITION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<gexf xmlns=\"http://www.gexf.net/1.2draft\" "
            + "      version=\"1.2\" "
            + "      xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" "
            + "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
            + "<meta>\n"
            + "  <creator>The JGraphT Library</creator>\n"
            + "  <description>Test</description>\n"
            + "</meta>\n"
            + "<graph defaultedgetype=\"undirected\">\n"
            + "  <attributes class=\"node\">\n"
            + "    <attribute id=\"0\" title=\"color\" type=\"string\"/>\n"
            + "    <attribute id=\"1\" title=\"city\" type=\"string\"/>\n"
            + "  </attributes>\n"
            + "  <attributes class=\"edge\">\n"
            + "    <attribute id=\"0\" title=\"length\" type=\"double\"/>\n"
            + "  </attributes>\n"
            + "  <nodes>\n"
            + "    <node id=\"0\" label=\"0\">\n"
            + "      <attvalues>\n"
            + "        <attvalue for=\"0\" value=\"Red\"/>\n"
            + "        <attvalue for=\"1\" value=\"Paris\"/>\n"
            + "      </attvalues>\n"
            + "    </node>\n"
            + "    <node id=\"1\" label=\"1\"/>\n"
            + "    <node id=\"2\" label=\"2\"/>\n"
            + "  </nodes>\n"
            + "  <edges>\n"
            + "    <edge id=\"0\" source=\"0\" target=\"1\" type=\"undirected\" weight=\"1.0\">\n"
            + "      <attvalues>\n"
            + "        <attvalue for=\"0\" value=\"100.0\"/>\n"
            + "      </attvalues>\n"
            + "    </edge>\n"
            + "    <edge id=\"1\" source=\"2\" target=\"0\" type=\"undirected\" weight=\"13.5\">\n"
            + "      <attvalues>\n"
            + "        <attvalue for=\"0\" value=\"30.0\"/>\n"
            + "      </attvalues>\n"
            + "    </edge>\n"
            + "  </edges>\n"
            + "</graph>\n"
            + "</gexf>\n";

    @Test
    public void testGEXFExport() {
        String responseString = given()
                .when().get("/jgrapht/gexf/export")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        Diff diff = DiffBuilder.compare(responseString).withTest(GEXF_DEFINITION).ignoreWhitespace().checkForIdentical()
                .build();
        assertFalse(diff.hasDifferences(), "XML files are not identical");
    }

    @Test
    public void testGEXFImport() {
        given()
                .when().get("/jgrapht/gexf/import")
                .then()
                .statusCode(200)
                .body(equalTo("([0, 1, 2], [{1,2}, {0,1}, {2,0}])"));
    }

    @Test

    public void testBrokenGEXFImport() {
        given()
                .when().get("/jgrapht/gexf/import/broken")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    private static final String JSON_DEFINITION = "{\"creator\":\"JGraphT JSON Exporter\",\"version\":\"1\",\"nodes\":[{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"}],\"edges\":[{\"id\":\"1\",\"source\":\"1\",\"target\":\"2\"},{\"id\":\"2\",\"source\":\"2\",\"target\":\"3\"},{\"id\":\"3\",\"source\":\"3\",\"target\":\"4\"},{\"id\":\"4\",\"source\":\"1\",\"target\":\"4\"}]}";

    @Test
    public void testJSONExport() {
        String responseString = given()
                .when().get("/jgrapht/json/export")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals(JSON_DEFINITION, responseString);
    }

    @Test
    public void testJSONImport() {
        given()
                .when().get("/jgrapht/json/import")
                .then()
                .statusCode(200)
                .body(equalTo("([1, 2, 3, 4], [{1,2}, {1,3}, {2,3}])"));
    }

    @Test

    public void testBrokenJSONImport() {
        given()
                .when().get("/jgrapht/json/import/broken")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    private static final String GRAPH6_DEFINITION = ":Cda";

    @Test
    public void testGraph6Export() {
        String responseString = given()
                .when().get("/jgrapht/graph6/export")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals(GRAPH6_DEFINITION, responseString);
    }

    @Test
    public void testGraph6Import() {
        given()
                .when().get("/jgrapht/graph6/import")
                .then()
                .statusCode(200)
                .body(equalTo("([0, 1, 2, 3, 4, 5, 6], [{0,1}, {0,2}, {1,2}, {5,6}])"));
    }

    @Test
    public void testBrokenGraph6Import() {
        given()
                .when().get("/jgrapht/graph6/import/broken")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }

    @Test
    public void testDotExport() {
        given()
                .when().get("/jgrapht/dot/export")
                .then()
                .statusCode(200)
                .body(containsString("www_wikipedia_org"))
                .body(containsString("label=\"http://www.wikipedia.org\""))
                .body(containsString("www_jgrapht_org"))
                .body(containsString("www_google_com"))
                .body(containsString("www_google_com -> www_wikipedia_org"));
    }

    @Test
    public void testDotImport() {
        given()
                .when().get("/jgrapht/dot/import")
                .then()
                .statusCode(200)
                .body(equalTo("([0, 1, 2, 3], [(0,1), (1,2), (1,3)])"));
    }

    @Test
    public void testBrokenDotImport() {
        given()
                .when().get("/jgrapht/dot/import/broken")
                .then()
                .statusCode(200)
                .body(equalTo("OK"));
    }
}
