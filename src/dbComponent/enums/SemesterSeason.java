package dbComponent.enums;

public enum SemesterSeason {
    SPRING, AUTUMN;

    public static SemesterSeason fromString(String val) {
        switch (val.toLowerCase()) {
            case "spring":
            case "forår":
                return SemesterSeason.SPRING;
            case "autumn":
            case "efterår":
                return SemesterSeason.AUTUMN;
            default:
                throw new RuntimeException(String.format("Error converting from string to SemesterSeason, string: %s not recognised", val));
        }
    }
}
