import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс для колоды кард, которая содержит все карты в игре.
 */
public class Deck {
    private List<Card> cards;
    private int decksCount;

    public Deck(int decksCount) {
        this.decksCount = decksCount;
        initialize();
    }

    private void initialize() {
        cards = new ArrayList<>();
        for (int i = 0; i < decksCount; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Метод для извлечения карты из колоды, если карты кончились, то пересоздаем колоду.
     * Возвращает извлеченную карту.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            initialize(); // Пересоздаем колоду если закончились карты
        }
        return cards.remove(cards.size() - 1);
    }

    public int getRemainingCards() {
        return cards.size();
    }
}
