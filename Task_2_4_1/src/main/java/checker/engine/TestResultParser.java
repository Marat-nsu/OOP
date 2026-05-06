package checker.engine;

import checker.logging.CheckerLogger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

class TestResultParser {
    private final CheckerLogger log;

    TestResultParser(CheckerLogger log) {
        this.log = log;
    }

    TestCounts parse(Path taskDir) {
        Path testResultsDir = taskDir.resolve("build/test-results");
        if (!Files.exists(testResultsDir)) {
            return TestCounts.ZERO;
        }

        int passed = 0;
        int failed = 0;
        int skipped = 0;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            try (var stream = Files.walk(testResultsDir)) {
                List<Path> xmlFiles = stream.filter(p -> p.toString().endsWith(".xml")).toList();
                for (Path xmlFile : xmlFiles) {
                    try {
                        var doc = builder.parse(xmlFile.toFile());
                        var suites = doc.getElementsByTagName("testsuite");
                        for (int i = 0; i < suites.getLength(); i++) {
                            var suite = (Element) suites.item(i);
                            int tests = parseInt(suite.getAttribute("tests"));
                            int fail = parseInt(suite.getAttribute("failures"))
                                + parseInt(suite.getAttribute("errors"));
                            int skip = parseInt(suite.getAttribute("skipped"));
                            passed += Math.max(0, tests - fail - skip);
                            failed += fail;
                            skipped += skip;
                        }
                    } catch (Exception ignored) { }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse test results: " + e.getMessage());
        }
        return new TestCounts(passed, failed, skipped);
    }

    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
