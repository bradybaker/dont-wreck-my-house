package learn.home.ui;

import learn.home.data.DataAccessException;
import learn.home.domain.GuestService;
import learn.home.domain.HostService;
import learn.home.domain.ReservationService;
import learn.home.domain.Result;
import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller {

    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;
    private final View view;


    public Controller(ReservationService reservationService, HostService hostService, GuestService guestService, View view) {
        this.reservationService = reservationService;
        this.hostService = hostService;
        this.guestService = guestService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to this thing that isn't AirBnb at all!");
        try {
            runAppLoop();
        } catch (DataAccessException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataAccessException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case EXIT:
                    view.displayHeader(MainMenuOption.EXIT.getDisplayText());
                    break;
                case VIEW_RESERVATION_FOR_HOST:
                    view.displayHeader(MainMenuOption.VIEW_RESERVATION_FOR_HOST.getDisplayText());
                    viewReservationsByHost();
                    break;
                case MAKE_A_RESERVATION:
                    view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getDisplayText());
                    addReservation();
                    break;
                case EDIT_A_RESERVATION:
                    view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getDisplayText());
                    updateReservation();
                    break;
                case CANCEL_A_RESERVATION:
                    view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getDisplayText());
                    deleteReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsByHost() throws DataAccessException {
        String email = view.getHostEmail();

        Host host = hostService.findHostByEmail(email);

        if (host.getEmail() == null) {
            Result<Reservation> result = new Result<>();
            result.addErrorMessage("No host found at that email");
            view.displayStatus(false, result.getMessages());
            return;
        }

        List<Reservation> reservations = reservationService.findReservationByEmail(email);
        view.displayReservations(reservations, host.getLast_name());

        if (reservations == null || reservations.isEmpty()) {
            return;
        }
        view.enterToContinue();
    }

    private void addReservation() throws DataAccessException {
        boolean isConfirmed = false;
        while (!isConfirmed) {
            String guestEmail = view.getGuestEmail();
            Guest guest = guestService.findGuestByEmail(guestEmail);

            if (guest.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No guest found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            String hostEmail = view.getHostEmail();
            Host host = hostService.findHostByEmail(hostEmail);

            if (host.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No host found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            List<Reservation> reservations = reservationService.findReservationByEmail(hostEmail);
            view.displayReservations(reservations, host.getLast_name());

            Reservation reservation = view.makeReservation(host, guest);
            reservation.calculateTotal();

            view.displaySummary(reservation);

            isConfirmed = view.displayConfirmation();
            if (isConfirmed) {
                Result<Reservation> result = reservationService.addReservation(reservation);
                if (result.isSuccess()) {
                    String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
                    view.displayStatus(true, successMessage);
                } else {
                    view.displayStatus(false, result.getMessages());
                }

            }
        }
    }

    private void updateReservation() throws DataAccessException {
        boolean isConfirmed = false;
        while (!isConfirmed) {
            String guestEmail = view.getGuestEmail();
            Guest guest = guestService.findGuestByEmail(guestEmail);

            if (guest.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No guest found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            String hostEmail = view.getHostEmail();
            Host host = hostService.findHostByEmail(hostEmail);

            if (host.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No host found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            List<Reservation> guestReservationsForHost = reservationService.filterReservationsByGuestEmail(host.getEmail(), guest.getEmail());

            view.displayReservations(guestReservationsForHost, guest.getLast_name());

            if (guestReservationsForHost.isEmpty()) {
                return;
            }

            int resId = view.getReservationId();
            Reservation reservation = reservationService.findReservationById(resId, host.getId());

            Reservation updatedReservation = view.updateReservation(reservation);
            updatedReservation.setHost(host);
            updatedReservation.setGuest(guest);
            updatedReservation.calculateTotal();


            view.displaySummary(updatedReservation);

            isConfirmed = view.displayConfirmation();
            if (isConfirmed) {
                Result<Reservation> result = reservationService.updateReservation(reservation);
                if (result.isSuccess()) {
                    view.displayStatus(true, result.getMessages());
                    view.displayText("Successfully updated reservation with ID: " + resId);
                } else {
                    view.displayStatus(false, result.getMessages());
                }
            }
        }
    }

        private void deleteReservation () throws DataAccessException {
            String guestEmail = view.getGuestEmail();
            Guest guest = guestService.findGuestByEmail(guestEmail);

            if (guest.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No guest found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            String hostEmail = view.getHostEmail();
            Host host = hostService.findHostByEmail(hostEmail);

            if (host.getEmail() == null) {
                Result<Reservation> result = new Result<>();
                result.addErrorMessage("No host found at that email");
                view.displayStatus(false, result.getMessages());
                return;
            }

            List<Reservation> guestReservationsForHost = reservationService.filterReservationsByGuestEmail(host.getEmail(), guest.getEmail());
            view.displayReservations(guestReservationsForHost, guest.getLast_name());

            if (guestReservationsForHost.isEmpty()) {
                return;
            }


            int resId = view.getReservationId();
            Reservation reservation = reservationService.findReservationById(resId, host.getId());

            Result<Reservation> result = reservationService.deleteReservation(reservation);
            if (result.isSuccess()) {
                view.displayStatus(true, result.getMessages());
                view.displayText("Successfully deleted reservation with ID: " + resId);
            } else {
                view.displayStatus(false, result.getMessages());
            }
        }

}

