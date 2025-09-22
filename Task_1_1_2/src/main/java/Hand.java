import java.util.ArrayList;
import java.util.List;

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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Card card : cards) {
      sb.append(card.toString()).append(", ");
    }
    if (!cards.isEmpty()) {
      sb.setLength(sb.length() - 2); // Убираем последнюю запятую и пробел
    }
    return sb.toString();
  }
}
