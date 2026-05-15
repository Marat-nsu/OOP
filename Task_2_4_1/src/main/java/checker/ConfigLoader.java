package checker;

import checker.dsl.DslConfigException;
import checker.model.CourseConfig;
import groovy.lang.Binding;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.FileNotFoundException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ConfigLoader {

    public static CourseConfig load(File scriptFile) {
        CourseConfig config = new CourseConfig();
        loadInto(scriptFile, config);
        return config;
    }

    public static void loadInto(File scriptFile, CourseConfig config) {
        try {
            File absoluteScript = scriptFile.getCanonicalFile();
            if (!absoluteScript.exists()) {
                throw new DslConfigException(
                    "DSL config file not found: " + absoluteScript.getAbsolutePath()
                );
            }

            CompilerConfiguration cc = new CompilerConfiguration();
            cc.setScriptBaseClass("checker.dsl.CourseScript");
            Binding binding = new Binding();
            binding.setVariable("__config", config);
            binding.setVariable("__scriptDir", absoluteScript.getParentFile().getAbsolutePath());
            GroovyShell shell = new GroovyShell(ConfigLoader.class.getClassLoader(), binding, cc);
            Script script = shell.parse(absoluteScript);
            script.run();
        } catch (DslConfigException e) {
            throw e;
        } catch (FileNotFoundException e) {
            throw new DslConfigException("DSL config file not found: " + scriptFile, e);
        } catch (CompilationFailedException e) {
            throw new DslConfigException(
                "DSL syntax error in " + scriptFile.getPath() + ": " + e.getMessage(),
                e
            );
        } catch (MissingMethodException e) {
            throw new DslConfigException(
                "Unknown DSL method '" + e.getMethod() + "' in " + scriptFile.getPath(),
                e
            );
        } catch (MissingPropertyException e) {
            throw new DslConfigException(
                "Unknown DSL property '" + e.getProperty() + "' in " + scriptFile.getPath(),
                e
            );
        } catch (Exception e) {
            throw new DslConfigException(
                "DSL error in " + scriptFile.getPath() + ": " + e.getMessage(),
                e
            );
        }
    }
}
