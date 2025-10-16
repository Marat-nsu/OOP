package model;

import ui.LocalizationLoader;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    public String toString(LocalizationLoader loc) {
        String rankName = loc.get("rank." + rank.name().toLowerCase());
        String suitName = loc.get("suit." + suit.name().toLowerCase());
        return rankName + " " + suitName + " (" + getValue() + ")";
    }
}
