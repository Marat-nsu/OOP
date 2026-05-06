package checker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SettingsTest {
    @Test
    void computesGradeFromHighestReachedThreshold() {
        Settings settings = new Settings();
        settings.addGradeThreshold(threshold(3, "3"));
        settings.addGradeThreshold(threshold(5, "4"));
        settings.addGradeThreshold(threshold(8, "5"));

        assertEquals("-", settings.computeGrade(2));
        assertEquals("3", settings.computeGrade(3));
        assertEquals("4", settings.computeGrade(7));
        assertEquals("5", settings.computeGrade(8));
    }

    @Test
    void storesBonusPointsByStudentAndTask() {
        Settings settings = new Settings();

        settings.addBonusPoints("student", "2_4_1", 2);

        assertEquals(2, settings.getBonusPoints("student", "2_4_1"));
        assertEquals(0, settings.getBonusPoints("student", "2_1_1"));
        assertEquals(0, settings.getBonusPoints("other", "2_4_1"));
    }

    @Test
    void repositoryDownloadParallelismIsAtLeastOne() {
        Settings settings = new Settings();

        settings.setRepositoryDownloadParallelism(0);

        assertEquals(1, settings.getRepositoryDownloadParallelism());
    }

    private GradeThreshold threshold(int minScore, String grade) {
        GradeThreshold threshold = new GradeThreshold();
        threshold.setMinScore(minScore);
        threshold.setGrade(grade);
        return threshold;
    }
}
