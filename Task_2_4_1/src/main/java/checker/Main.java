package checker;

import checker.logging.CheckerLogger;
import checker.model.CourseConfig;
import java.io.File;


public class Main {
    private static final String CONFIG_FILE = "oop.groovy";
    private static final CheckerLogger LOG = CheckerLogger.stderr();

    public static void main(String[] args) throws Exception {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            LOG.error("Config file not found: " + configFile.getAbsolutePath());
            LOG.error("Create an 'oop.groovy' file in the current directory.");
            System.exit(1);
        }

        CourseConfig config = ConfigLoader.load(configFile);
        String html = Checker.check(config);
        System.out.println(html);
    }
}
