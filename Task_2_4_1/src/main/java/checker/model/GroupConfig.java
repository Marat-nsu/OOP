package checker.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupConfig {
    private String name = "";
    private List<StudentConfig> students = new ArrayList<>();

    public void addStudent(StudentConfig student) { 
        this.students.add(student);
    }
}
