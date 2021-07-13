package poker.dto;

import java.util.List;

public class ResultDTO {

    private final String result;

    private final int pokerHandIndex;

    private final int worth;

    private final List<CardDTO> cards;

    public ResultDTO(String result, int pokerHandIndex, int worth, List<CardDTO> cards) {
        this.result = result;
        this.pokerHandIndex = pokerHandIndex;
        this.worth = worth;
        this.cards = cards;
    }

    public String getResult() {
        return result;
    }

    public int getPokerHandIndex() {
        return pokerHandIndex;
    }

    public int getWorth() {
        return worth;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
