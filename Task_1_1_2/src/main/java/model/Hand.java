package model;

import java.util.ArrayList;
import java.util.List;
import ui.LocalizationLoader;

public class Hand {
    private final List<Card> cards;
    private int score;

    /*
     * Количество тузов, которые считаются за 11
     */
    private int softAcesCount;

    public Hand() {
        cards = new ArrayList<>();
        score = 0;
    }

    public void addCard(Card card) {
        cards.add(card);
        calculateScore();
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Вычисляет общий счет руки, суммируя значения всех карт.
     * Если общий счет больше 21 и в руке есть тузы, то значения тузов
     * корректируется до 1 вместо 11.
     */
    private void calculateScore() {
        score = 0;
        softAcesCount = 0;
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
            softAcesCount++;
            acesCount--;
        }
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && score == 21;
    }

    public boolean isBust() {
        return score > 21;
    }

    public void clear() {
        cards.clear();
        score = 0;
    }

    public int score() {
        return score;
    }

    public String formatForDisplay(boolean showAllCards, LocalizationLoader loc) {
        if (cards.isEmpty()) {
            return loc.get("emptyHand");
        }

        StringBuilder handStr = new StringBuilder();
        handStr.append('[');

        // Первая карта всегда видна
        handStr.append(cards.get(0).toString(loc));

        if (showAllCards) {
            int aceIndex = 0;
            for (int i = 1; i < cards.size(); i++) {
                if (cards.get(i).getRank() == Rank.ACE) {
                    int effectiveValue = softAcesCount > aceIndex ? 1 : 11;
                    handStr.append(loc.get("cardSeparator"))
                            .append(cards.get(i).toString().replace("(11)",
                                    "(%d)".formatted(effectiveValue)));
                    aceIndex++;
                } else {
                    handStr.append(loc.get("cardSeparator")).append(cards.get(i).toString(loc));
                }
            }
        } else {
            if (cards.size() > 1) {
                handStr.append(loc.get("cardSeparator")).append(loc.get("hiddenCard"));
            }
        }
        handStr.append(']');

        if (showAllCards) {
            handStr.append(" (").append(loc.get("scorePrefix")).append(score()).append(')');
        }

        return handStr.toString();
    }
}
