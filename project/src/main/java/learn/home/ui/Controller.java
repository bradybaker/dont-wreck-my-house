package learn.home.ui;

import com.sun.tools.javac.Main;
import learn.home.data.DataAccessException;
import learn.home.domain.ReservationService;
import org.springframework.stereotype.Component;

@Component
public class Controller {

    private final ReservationService reservationService;
    private final View view;

    public Controller(ReservationService reservationService, View view) {
        this.reservationService = reservationService;
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
                    break;
                case MAKE_A_RESERVATION:
                    view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getDisplayText());
                    break;
                case EDIT_A_RESERVATION:
                    view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getDisplayText());
                    break;
                case CANCEL_A_RESERVATION:
                    view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getDisplayText());
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }
}
