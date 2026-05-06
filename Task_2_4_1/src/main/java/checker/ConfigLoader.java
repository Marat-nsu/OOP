package checker;

import checker.model.CourseConfig;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ConfigLoader {

    public static CourseConfig load(File scriptFile) throws Exception {
        CourseConfig config = new CourseConfig();
        loadInto(scriptFile, config);
        return config;
    }

    public static void loadInto(File scriptFile, CourseConfig config) throws Exception {
        File absoluteScript = scriptFile.getCanonicalFile();
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass("checker.dsl.CourseScript");
        Binding binding = new Binding();
        binding.setVariable("__config", config);
        binding.setVariable("__scriptDir", absoluteScript.getParentFile().getAbsolutePath());
        GroovyShell shell = new GroovyShell(ConfigLoader.class.getClassLoader(), binding, cc);
        Script script = shell.parse(absoluteScript);
        script.run();
    }
}
