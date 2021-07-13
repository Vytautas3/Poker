package poker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import poker.dto.CardDTO;
import poker.dto.CardHandDTO;
import poker.dto.CombinationDTO;
import poker.dto.ResultDTO;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PokerService {

    @Autowired
    public PokerService() {

    }

    public String getResults(List<CardHandDTO> cardHands) {
        List<ResultDTO> resultDTOS = cardHands.stream().map(this::processor).collect(Collectors.toList());
        ResultDTO player1 = resultDTOS.get(0);
        ResultDTO player2 = resultDTOS.get(1);
        if (resultDTOS.stream().map(ResultDTO::getPokerHandIndex).distinct().count() == 1) {
            if (player1.getWorth() == player2.getWorth()) {
                return highestHighCard(player1, player2);
            } else if (player1.getWorth() > player2.getWorth()) {
                return "Both players had " + player1.getResult() + " but Player1 won due to higher rank values";
            } else {
                return "Both players had " + player2.getResult() + " but Player2 won due to higher rank values";
            }
        } else {
            if (player1.getPokerHandIndex() > player2.getPokerHandIndex()) {
                return "Player 1 won with " + player1.getResult() +
                        " because Player 2 only had " + player2.getResult();
            } else {
                return "Player 2 won with " + player2.getResult() +
                        " because Player 1 only had " + player1.getResult();
            }
        }
    }

    private String highestHighCard(ResultDTO player1, ResultDTO player2) {
        Set<Integer> ranksP1 = new HashSet<>(parseRankToInt(new CardHandDTO(player1.getCards()), true));
        Set<Integer> ranksP2 = new HashSet<>(parseRankToInt(new CardHandDTO(player2.getCards()), true));
        Set<Integer> copyP1 = new HashSet<>(ranksP1);
        Set<Integer> copyP2 = new HashSet<>(ranksP2);
        ranksP1.forEach(copyP2::remove);
        ranksP2.forEach(copyP1::remove);
        if (copyP1.isEmpty() && copyP2.isEmpty()) {
            return "Tie";
        }
        if (Collections.max(copyP1) > Collections.max(copyP2)) {
            return "Both players had: " + player1.getResult() + ", but Player 1 won because of higher card: " + parseRankToString(Collections.max(copyP1).toString());
        } else {
            return "Both players had: " + player2.getResult() + ", but Player 2 won because of higher card: " + parseRankToString(Collections.max(copyP2).toString());
        }
    }

    private ResultDTO processor(CardHandDTO cardHand) {
        if (isFlush(cardHand) && isStraight(cardHand).getIs() && isRoyal(cardHand)) {
            return new ResultDTO("Royal Flush", 10, 1, cardHand.getCards());
        } else if (isFlush(cardHand) && isStraight(cardHand).getIs()) {
            return new ResultDTO("Straight Flush", 9, isStraight(cardHand).getWorth(), cardHand.getCards());
        } else if (is4s(cardHand).getIs() && !isFullHouse(cardHand).getIs()) {
            return new ResultDTO("Four of a Kind", 8, is4s(cardHand).getWorth(), cardHand.getCards());
        } else if (isFullHouse(cardHand).getIs()) {
            return new ResultDTO("Full House", 7, isFullHouse(cardHand).getWorth(), cardHand.getCards());
        } else if (isFlush(cardHand)) {
            return new ResultDTO("Flush", 6, 1, cardHand.getCards());
        } else if (isStraight(cardHand).getIs()) {
            return new ResultDTO("Straight", 5, isStraight(cardHand).getWorth(), cardHand.getCards());
        } else if (is3s(cardHand).getIs() /*&& !is2pairs(cardHand).getIs()*/) {
            return new ResultDTO("Three of a Kind", 4, is3s(cardHand).getWorth(), cardHand.getCards());
        } else if (is2pairs(cardHand).getIs()) {
            return new ResultDTO("Two pairs", 3, is2pairs(cardHand).getWorth(), cardHand.getCards());
        } else if (isPair(cardHand).getIs()) {
            return new ResultDTO("A Pair", 2, isPair(cardHand).getWorth(), cardHand.getCards());
        } else {
            Integer highCard = highCardInt(cardHand);
            return new ResultDTO("High Card"/*: " + parseRankToString(highCard.toString())*/, 1, highCard, cardHand.getCards());
        }
    }

    private boolean isFlush(CardHandDTO cardHand) {
        return cardHand.getCards().stream().map(CardDTO::getSuit).distinct().count() == 1;
    }

    private boolean isRoyal(CardHandDTO cardHand) {
        Pattern pattern = Pattern.compile(".*\\d+.*")/*("-?\\d+(\\.\\d+)?")*/;
        if (cardHand.getCards().stream().filter(card -> pattern.matcher(card.getRank()).matches()).count() == 1) {
            return cardHand.getCards().stream().filter(card -> card.getRank().equals("10")).count() == 1;
        } else {
            return false;
        }
    }

    private CombinationDTO isFullHouse(CardHandDTO cardHand) {
        return countRanks(cardHand, 3, 2);
    }

    private List<Integer> parseRankToInt(CardHandDTO cardHand, boolean highCard) {
        List<Integer> intValues = new LinkedList<>();
        List<String> ranks = cardHand.getCards().stream().map(CardDTO::getRank).collect(Collectors.toList());
        for (String rank : ranks) {
            switch (rank) {
                case "A":
                    if (highCard) {
                        intValues.add(14);
                    } else {
                        intValues.add(1);
                    }
                    break;
                case "K":
                    intValues.add(13);
                    break;
                case "Q":
                    intValues.add(12);
                    break;
                case "J":
                    intValues.add(11);
                    break;
                default:
                    intValues.add(NumberUtils.parseNumber(rank, Integer.class));
                    break;
            }
        }
        return intValues;
    }

    private String parseRankToString(String card) {
        switch (card) {
            case "14":
                return "Ace";
            case "13":
                return "King";
            case "12":
                return "Queen";
            case "11":
                return "Jack";
            default:
                return card;
        }
    }

    private CombinationDTO isStraight(CardHandDTO cardHand) {
        List<Integer> ranks;
        if (cardHand.getCards().stream().filter(card -> card.getRank().equals("A")).count() >= 1) {
            ranks = parseRankToInt(cardHand, true).stream().sorted().collect(Collectors.toList());
            CombinationDTO combDto = checkStraight(ranks.stream().sorted().collect(Collectors.toList()));
            if (combDto.getIs()) {
                return combDto;
            }
        }
        ranks = parseRankToInt(cardHand, false).stream().sorted().collect(Collectors.toList());
        return checkStraight(ranks);
    }

    private CombinationDTO checkStraight(List<Integer> ranks) {
        int worth = ranks.get(0);
        int c = ranks.get(0);
        int rc = 0;
        for (int i = 1; i < ranks.size(); i++) {
            while (c + 1 == ranks.get(i)) {
                c += 1;
                worth += ranks.get(i);
                rc++;
            }
        }
        return new CombinationDTO(rc == 4, worth);

    }

    private Integer highCardInt(CardHandDTO cardHand) {
        List<Integer> ranks = parseRankToInt(cardHand, false).stream().sorted().collect(Collectors.toList());
        if (ranks.get(0) == 1) {
            return 14;
        } else {
            return ranks.get(4);
        }
    }

    private CombinationDTO is4s(CardHandDTO cardHand) {
        return countRanks(cardHand, 3, 0);
    }

    private CombinationDTO is3s(CardHandDTO cardHand) {
        return countRanks(cardHand, 2, 1);
    }

    private CombinationDTO is2pairs(CardHandDTO cardHand) {
        return countRanks(cardHand, 2, 2);
    }

    private CombinationDTO isPair(CardHandDTO cardHand) {
        return countRanks(cardHand, 1, 0);
    }

    private CombinationDTO countRanks(CardHandDTO cardHand, int quantity, int distinct) {
        List<Integer> ranks = parseRankToInt(cardHand, false);
        List<Integer> distinctRanks = ranks.stream().distinct().collect(Collectors.toList());
        boolean is;
        distinctRanks.forEach(ranks::remove);
        if (distinct != 0) {
            is = ranks.stream().distinct().count() == distinct && ranks.size() == quantity;
            return new CombinationDTO(is, ifDistinctAce(ranks, is, quantity, distinct));
        }
        is = ranks.size() == quantity;
        return new CombinationDTO(is, is ? ifAce(ranks, 0) : 0);
    }

    private Integer ifDistinctAce(List<Integer> ranks, boolean is, int quantity, int distinct) {
        if (is && quantity == 3 && distinct == 2) {
            List<Integer> distinctRanks = ranks.stream().distinct().collect(Collectors.toList());
            return ifAce(distinctRanks, 0) + ifAce(distinctRanks, 1);
        } else if (is && quantity == 2 && distinct == 2) {
            return ifAce(ranks, 0) + ifAce(ranks, 1);
        } else if (is && quantity == 2 && distinct == 1) {
            return ifAce(ranks, 0);
        } else {
            return 0;
        }
    }

    private Integer ifAce(List<Integer> ranks, int index) {
        return ranks.get(index) == 1 ? 14 : ranks.get(index);
    }
}
