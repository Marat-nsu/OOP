package checker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskConfig {
    private String id = "";
    private String name = "";
    private int maxScore = 1;
    private String softDeadline = "";
    private String hardDeadline = "";
}
