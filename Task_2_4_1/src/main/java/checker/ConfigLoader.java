package checker;

import checker.model.CourseConfig;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import org.codehaus.groovy.control.CompilerConfiguration;

public class ConfigLoader {

    public static CourseConfig load(File scriptFile) throws Exception {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass("checker.dsl.CourseScript");
        GroovyShell shell = new GroovyShell(ConfigLoader.class.getClassLoader(), new Binding(), cc);
        Script script = shell.parse(scriptFile);
        script.run();
        return (CourseConfig) script.getClass().getMethod("getConfig").invoke(script);
    }
}
