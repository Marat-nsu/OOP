package checker.engine;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PropertyResolver;
import com.puppycrawl.tools.checkstyle.XMLLogger;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

class StyleChecker {
    String check(Path repoPath, Path taskDir) throws IOException, CheckstyleException {
        Path configPath = repoPath.resolve(".github/google_checks.xml");
        if (!Files.exists(configPath)) {
            return "Checkstyle config not found: .github/google_checks.xml";
        }

        List<File> javaFiles;
        try (var stream = Files.walk(taskDir)) {
            javaFiles = stream
                .filter(p -> p.toString().endsWith(".java"))
                .filter(p -> !p.toString().contains("/build/"))
                .map(Path::toFile)
                .toList();
        }
        if (javaFiles.isEmpty()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Checker checker = new Checker();
        try {
            Configuration configuration = ConfigurationLoader.loadConfiguration(
                configPath.toString(),
                properties(repoPath),
                ConfigurationLoader.IgnoredModulesOptions.OMIT
            );
            checker.setModuleClassLoader(Checker.class.getClassLoader());
            checker.addListener(new DefaultLogger(output, XMLLogger.OutputStreamOptions.NONE));
            checker.configure(configuration);
            int violations = checker.process(javaFiles);
            if (violations == 0) {
                return null;
            }
            return output.toString(StandardCharsets.UTF_8).strip();
        } finally {
            checker.destroy();
        }
    }

    private PropertyResolver properties(Path repoPath) {
        Properties properties = new Properties();
        properties.putAll(System.getProperties());
        properties.setProperty(
            "org.checkstyle.google.suppressionfilter.config",
            repoPath.resolve(".github/checkstyle-suppressions.xml").toString()
        );
        return properties::getProperty;
    }
}
