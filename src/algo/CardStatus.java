package algo;

public enum CardStatus {
    STATUS_ISSUED(1),
    STATUS_PENDING(0);
    private final int cardStatus;
    CardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int getCardStatus() {
        return cardStatus;
    }
}
