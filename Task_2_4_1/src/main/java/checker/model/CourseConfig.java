package checker.model;

import java.util.ArrayList;
import java.util.List;

public class CourseConfig {
    private List<TaskConfig> tasks = new ArrayList<>();
    private List<GroupConfig> groups = new ArrayList<>();
    private List<CheckEntry> checks = new ArrayList<>();
    private Settings settings = new Settings();

    public List<TaskConfig> getTasks() {
        return tasks;
    }
    
    public void addTask(TaskConfig task) {
        this.tasks.add(task);
    }

    public List<GroupConfig> getGroups() {
        return groups;
    }
    
    public void addGroup(GroupConfig group) {
        this.groups.add(group);
    }

    public List<CheckEntry> getChecks() {
        return checks;
    }
    
    public void addCheck(CheckEntry check) {
        this.checks.add(check);
    }

    public Settings getSettings() {
        return settings;
    }
    
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public TaskConfig findTask(String id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public GroupConfig findGroup(String name) {
        return groups.stream().filter(g -> g.getName().equals(name)).findFirst().orElse(null);
    }
}
