package checker.model;

import java.util.ArrayList;
import java.util.List;

public class GroupConfig {
    private String name = "";
    private List<StudentConfig> students = new ArrayList<>();

    public String getName() { 
        return name;
    }
    
    public void setName(String name) { 
        this.name = name;
    }

    public List<StudentConfig> getStudents() { 
        return students;
    }

    public void setStudents(List<StudentConfig> students) { 
        this.students = students;
    }

    public void addStudent(StudentConfig student) { 
        this.students.add(student);
    }
}
