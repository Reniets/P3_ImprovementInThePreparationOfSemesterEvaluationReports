package Enums;

public enum SentimentPolarity {
    POSITIVE(0),
    NEGATIVE(1);

    // Static initializer
    static {
        POSITIVE.opposite = SentimentPolarity.NEGATIVE;
        NEGATIVE.opposite = SentimentPolarity.POSITIVE;
    }

    private SentimentPolarity opposite;
    private int value;

    SentimentPolarity(int i) {
        this.value = i;
    }

    public static SentimentPolarity fromString(String polarity) {
        switch (polarity.toUpperCase()) {
            case "POSITIVE":
                return POSITIVE;
            case "NEGATIVE":
                return NEGATIVE;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static SentimentPolarity fromValue(int value) {
        switch (value) {
            case 0:
                return POSITIVE;
            case 1:
                return NEGATIVE;
            default:
                throw new IllegalArgumentException();
        }
    }

    // Enum methods for getting opposite polarities and converting polarity from string to polarity
    public SentimentPolarity getOpposite() {
        return opposite;
    }

    public int getValue() {
        return value;
    }
}
