package poker.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonDeserialize(builder = CardHandDTO.Builder.class)
public class CardHandDTO {
    private final List<CardDTO> cards;

    public CardHandDTO(List<CardDTO> cards) {
        this.cards = cards;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    @JsonPOJOBuilder
    public static class Builder {
        private List<CardDTO> cards;

        public CardHandDTO build() {
            return new CardHandDTO(cards);
        }

        public CardHandDTO.Builder withCards(List<CardDTO> cards) {
            this.cards = cards;
            return this;
        }
    }
}
