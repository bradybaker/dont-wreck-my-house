package learn.home.ui;

public enum MainMenuOption {
    EXIT (0,"Exit"),
    VIEW_RESERVATION_FOR_HOST (1,"View Reservation for Host"),
    MAKE_A_RESERVATION (2, "Make a Reservation"),
    EDIT_A_RESERVATION (3, "Edit a Reservation"),
    CANCEL_A_RESERVATION (4, "Cancel a Reservation");

    private String displayText;
    private int value;

    private MainMenuOption(int value, String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getValue() {
        return value;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }
}
