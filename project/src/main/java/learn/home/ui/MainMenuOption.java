package learn.home.ui;

public enum MainMenuOption {
    EXIT ("Exit"),
    VIEW_RESERVATION_FOR_HOST ("View Reservation for Host"),
    MAKE_A_RESERVATION ("Make a Reservation"),
    EDIT_A_RESERVATION ("Edit a Reservation"),
    CANCEL_A_RESERVATION ("Cancel a Reservation");

    private String displayText;

    private MainMenuOption(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
