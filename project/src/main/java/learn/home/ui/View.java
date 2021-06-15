package learn.home.ui;

import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) { this.io = io; }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getDisplayText());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public String getHostEmail() {
        return io.readString("Host Email: ");
    }

    public void displayReservationsByHost(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found. Please search again");
        }

        assert reservations != null;
        Host host = reservations.get(0).getHost();
        io.printf("%s: %s, %s", host.getLast_name(), host.getCity(), host.getState());
        for (int i = 0; i < reservations.size(); i++) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservations.get(i).getId(),
                    reservations.get(i).getStart_date(),
                    reservations.get(i).getEnd_date(),
                    reservations.get(i).getGuest().getLast_name(),
                    reservations.get(i).getGuest().getFirst_name(),
                    reservations.get(i).getGuest().getEmail());
        }
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }
}
