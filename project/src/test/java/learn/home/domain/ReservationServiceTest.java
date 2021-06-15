package learn.home.domain;

import learn.home.data.DataAccessException;
import learn.home.data.GuestRepositoryDouble;
import learn.home.data.HostRepositoryDouble;
import learn.home.data.ReservationRepositoryDouble;
import learn.home.models.Reservation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new HostRepositoryDouble(),
            new GuestRepositoryDouble()
    );

    @Test
    void shouldFindOneReservationByHostEmail() throws DataAccessException {
        List<Reservation> result = service.findReservationByEmail("smith@gmail.com");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(1, result.get(0).getGuest_id());
        assertEquals("Smith", result.get(0).getHost().getLast_name());
        assertEquals("Baker", result.get(0).getGuest().getLast_name());
    }
}