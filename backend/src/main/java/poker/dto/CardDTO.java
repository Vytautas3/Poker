package poker.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CardDTO.Builder.class)
public class CardDTO {

    private final String suit;

    private final String rank;

    public CardDTO(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String suit;
        private String rank;

        public CardDTO build() {
            return new CardDTO(suit, rank);
        }

        public CardDTO.Builder withSuit(String suit) {
            this.suit = suit;
            return this;
        }

        public CardDTO.Builder withRank(String rank) {
            this.rank = rank;
            return this;
        }
    }
}
