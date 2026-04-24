package checker;

import checker.model.CourseConfig;
import java.io.File;


public class Main {
    private static final String CONFIG_FILE = "oop.groovy";

    public static void main(String[] args) throws Exception {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            System.err.println("Config file not found: " + configFile.getAbsolutePath());
            System.err.println("Create an 'oop.groovy' file in the current directory.");
            System.exit(1);
        }

        CourseConfig config = ConfigLoader.load(configFile);
        String html = Checker.check(config);
        System.out.println(html);
    }
}
