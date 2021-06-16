package learn.home.data;

import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    private String SEED_FILE_PATH = "./data/reservations-seed/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    private String TEST_FILE_PATH = "./data/reservations-test/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    private String TEST_DIR_PATH = "./data/reservations-test";
    static final int RESERVATION_COUNT = 12;

    final String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    public ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);



    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_FILE_PATH), Paths.get(TEST_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllReservations() throws DataAccessException {
        List<Reservation> all = repository.findAllByHostId(hostId);

        assertEquals(RESERVATION_COUNT, all.size());
    }

    @Test
    void shouldAddValidReservation() throws DataAccessException {
        Reservation result = new Reservation(13, LocalDate.of(2024, 1, 1), LocalDate.of(2025,
                        1, 1), 1, new BigDecimal(1000));

        Host host = new Host();
        host.setId(hostId);
        result.setHost(host);

        Guest guest = new Guest();
        guest.setGuest_id(1);
        result.setGuest(guest);

        result = repository.addReservation(result);
        List<Reservation> all = repository.findAllByHostId(hostId);

        assertNotNull(result);
        assertEquals(13, all.size());
    }


}