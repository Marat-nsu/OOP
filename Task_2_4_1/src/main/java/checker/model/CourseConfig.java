package checker.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseConfig {
    private List<TaskConfig> tasks = new ArrayList<>();
    private List<GroupConfig> groups = new ArrayList<>();
    private List<CheckEntry> checks = new ArrayList<>();
    private List<CheckpointConfig> checkpoints = new ArrayList<>();
    private Settings settings = new Settings();

    public void addTask(TaskConfig task) {
        this.tasks.add(task);
    }

    public void addGroup(GroupConfig group) {
        this.groups.add(group);
    }

    public void addCheck(CheckEntry check) {
        this.checks.add(check);
    }

    public void addCheckpoint(CheckpointConfig checkpoint) {
        this.checkpoints.add(checkpoint);
    }

    public TaskConfig findTask(String id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public GroupConfig findGroup(String name) {
        return groups.stream().filter(g -> g.getName().equals(name)).findFirst().orElse(null);
    }
}
