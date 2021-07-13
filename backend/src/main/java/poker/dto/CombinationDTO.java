package poker.dto;

public class CombinationDTO {

    private final Boolean is;

    private final int worth;

    public CombinationDTO(Boolean is, int worth) {
        this.is = is;
        this.worth = worth;
    }

    public Boolean getIs() {
        return is;
    }

    public int getWorth() {
        return worth;
    }
}
