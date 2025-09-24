import java.util.ArrayList;
import java.util.List;

/**
 * Класс руки игрока, содержит список карт.
 */
public class Hand {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Вычисляет общий счет руки, суммируя значения всех карт.
     * Если общий счет больше 21 и в руке есть тузы, то значения тузов
     * корректируется до 1 вместо 11.
     *
     * @return общий счет руки
     */
    public int calculateScore() {
        int score = 0;
        int acesCount = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank() == Rank.ACE) {
                acesCount++;
            }
        }

        // Корректируем значение тузов если сумма > 21
        while (score > 21 && acesCount > 0) {
            score -= 10; // Тузы считаем как 1 вместо 11
            acesCount--;
        }

        return score;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && calculateScore() == 21;
    }

    public boolean isBust() {
        return calculateScore() > 21;
    }

    public void clear() {
        cards.clear();
    }
}
