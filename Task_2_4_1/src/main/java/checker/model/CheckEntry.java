package checker.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckEntry {
    private String taskId = "";
    private String groupName = "";
    private List<String> studentGithubs = new ArrayList<>();

    public boolean includesStudent(StudentConfig student) {
        return studentGithubs.isEmpty() || studentGithubs.contains(student.getGithub());
    }
}
