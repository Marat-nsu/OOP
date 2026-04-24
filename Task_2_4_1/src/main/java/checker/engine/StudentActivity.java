package checker.engine;

public class StudentActivity {
    private final int activeWeeks;
    private final int totalWeeks;
    private final int activityBonus;

    public StudentActivity(int activeWeeks, int totalWeeks, int activityBonus) {
        this.activeWeeks = activeWeeks;
        this.totalWeeks = totalWeeks;
        this.activityBonus = activityBonus;
    }

    public static final StudentActivity UNKNOWN = new StudentActivity(0, 0, 0);

    public int getActiveWeeks() { return activeWeeks; }
    public int getTotalWeeks() { return totalWeeks; }
    public int getActivityBonus() { return activityBonus; }
}
