package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private final int decksCount;

    public Deck(int decksCount) {
        this.decksCount = decksCount;
        initialize();
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            initialize(); // Пересоздаем колоду если закончились карты
        }
        return cards.remove(cards.size() - 1);
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
        Collections.shuffle(cards);
    }
}
