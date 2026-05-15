package checker.engine;

import lombok.Value;

@Value
public class StudentActivity {
    int activeWeeks;
    int totalWeeks;
    int activityBonus;

    public static final StudentActivity UNKNOWN = new StudentActivity(0, 0, 0);
}
