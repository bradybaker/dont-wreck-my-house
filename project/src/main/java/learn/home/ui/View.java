package learn.home.ui;

import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class View {

    private final ConsoleIO io;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");


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

    public String getHostEmail() { return io.readRequiredString("Host Email: "); }

    public String getGuestEmail() {return io.readRequiredString("Guest Email: "); }

    public void displayReservations(List<Reservation> reservations, String lastName) {

        if (reservations.isEmpty()) {
            io.println(" ");
            io.println(lastName + " has no reservations booked.");
            return;
        }

        Host host = reservations.get(0).getHost();
        displayHeader(host.getLast_name() + ": " + host.getCity() + ", " + host.getState());
        io.println("(Sorted by Start Date)");
        for (Reservation reservation : reservations) {
            String startDate = reservation.getStart_date().format(formatter);
            String endDate = reservation.getEnd_date().format(formatter);
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservation.getId(),
                    startDate,
                    endDate,
                    reservation.getGuest().getLast_name(),
                    reservation.getGuest().getFirst_name(),
                    reservation.getGuest().getEmail());
        }
    }

    public Reservation makeReservation(Host host, Guest guest) {
        Reservation res = new Reservation();
        res.setHost(host);
        res.setGuest(guest);
        LocalDate startDate = io.readLocalDate("Start Date [MM/dd/yyyy]: ");
        res.setStart_date(startDate);
        LocalDate endDate = io.readLocalDate("End Date [MM/dd/yyyy]: ");
        res.setEnd_date(endDate);
        res.setGuest_id(guest.getGuest_id());
        return res;
    }

    public Reservation updateReservation(Reservation reservation) {
        displayHeader("Editing Reservation " + reservation.getId());

        String startDateFormat = reservation.getStart_date().format(formatter);
        String endDateFormat = reservation.getEnd_date().format(formatter);
        LocalDate startDate = io.readNotRequiredLocalDate("Start Date (" + startDateFormat + "): ", reservation.getStart_date());
        reservation.setStart_date(startDate);

        LocalDate endDate = io.readNotRequiredLocalDate("End Date (" + endDateFormat + "): ", reservation.getEnd_date());
        reservation.setEnd_date(endDate);

        return reservation;
    }

    public int getReservationId() {
        return io.readInt("Reservation ID: ");
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displaySummary(Reservation reservation) {
        displayHeader("Summary");
        io.println("Start Date: " + reservation.getStart_date());
        io.println("End Date: " + reservation.getEnd_date());
        io.println("Total: $" + reservation.getTotal());
    }

    public boolean displayConfirmation() {
        boolean isConfirmed;
        isConfirmed = io.readBoolean("Is this ok? [y/n]: ");
        return isConfirmed;
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

    public void displayText(String prompt) {
        io.println(prompt);
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
