package model;


public enum Grade {
    EXCELLENT(5),      // Отлично
    GOOD(4),          // Хорошо
    SATISFACTORY(3),  // Удовлетворительно
    PASS(0);          // Зачет (не влияет на средний балл)

    private final int value;

    Grade(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isGraded() {
        return this != PASS;
    }

    @Override
    public String toString() {
        switch (this) {
            case EXCELLENT:
                return "Отлично (5)";
            case GOOD:
                return "Хорошо (4)";
            case SATISFACTORY:
                return "Удовлетворительно (3)";
            case PASS:
                return "Зачет";
            default:
                return "";
        }
    }
}
