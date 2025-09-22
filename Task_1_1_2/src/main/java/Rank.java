/**
 * Enum с рангами карт.
 */
public enum Rank {
    TWO("Двойка", 2),
    THREE("Тройка", 3),
    FOUR("Четверка", 4),
    FIVE("Пятерка", 5),
    SIX("Шестерка", 6),
    SEVEN("Семерка", 7),
    EIGHT("Восьмерка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10),
    ACE("Туз", 11); // Базовое значение 11, может стать 1 при переборе

    private final String russianName;
    private final int value;

    Rank(String russianName, int value) {
        this.russianName = russianName;
        this.value = value;
    }

    public String getRussianName() {
        return russianName;
    }

    public int getValue() {
        return value;
    }
}
