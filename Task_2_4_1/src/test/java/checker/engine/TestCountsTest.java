package checker.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestCountsTest {
    @Test
    void totalAndStringUsePassedFailedSkippedOrder() {
        TestCounts counts = new TestCounts(7, 2, 1);

        assertEquals(10, counts.total());
        assertEquals("7/2/1", counts.toString());
    }
}
