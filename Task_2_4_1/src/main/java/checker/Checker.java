package checker;

import checker.engine.CheckEngine;
import checker.engine.CheckResults;
import checker.model.CourseConfig;
import checker.report.HtmlReporter;

public class Checker {

    public static String check(CourseConfig config) throws Exception {
        CheckEngine engine = new CheckEngine(config);
        CheckResults results = engine.run();
        return new HtmlReporter().generateHtml(config, results);
    }
}
