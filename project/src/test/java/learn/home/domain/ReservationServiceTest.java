package learn.home.domain;

import learn.home.data.DataAccessException;
import learn.home.data.GuestRepositoryDouble;
import learn.home.data.HostRepositoryDouble;
import learn.home.data.ReservationRepositoryDouble;
import learn.home.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Test
    void shouldFindReservationById() throws DataAccessException {
        Reservation reservation = service.findReservationById(1, HostRepositoryDouble.HOST1.getId());
        assertEquals(1, reservation.getId());
    }

    @Test
    void shouldNotFindReservationAndAddErrorMessage() throws DataAccessException {
        Result<Reservation> result = new Result<>();
        Reservation reservation = service.findReservationById(99999, HostRepositoryDouble.HOST1.getId());
        assertNull(reservation);
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldAddValidReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        List<Reservation> reservations = service.findReservationByEmail("smith@gmail.com");

        assertTrue(result.isSuccess());
        assertEquals(2, reservations.size());
    }

    @Test
    void shouldAddReservationWhenSDAndEDAOfExistingReservationAreTheSame() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 13));
        res.setEnd_date(LocalDate.of(2021, 10, 14));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        List<Reservation> reservations = service.findReservationByEmail("smith@gmail.com");
        assertTrue(result.isSuccess());
        assertEquals(2, reservations.size());
    }

    @Test
    void shouldNotAddReservationWithNoStartDate() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(null);
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Reservation start date is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWithNoEndDate() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(null);
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Reservation end date is required", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWithNoHost() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(null);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Host not found", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWithNoGuest() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(null);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Guest not found", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDComeAfterED() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 4));
        res.setEnd_date(LocalDate.of(2025, 1, 1));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Reservation start date must be before the end date", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDIsInThePast() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2020, 1, 1));
        res.setEnd_date(LocalDate.of(2021, 7, 1));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("Reservation start date must be in the future", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDAndEDAreSurroundingAnExistingReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 9));
        res.setEnd_date(LocalDate.of(2021, 10, 14));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("These dates conflict with an already existing reservation for this host", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDAndEDAreSurroundingTheSDOfExistingReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 9));
        res.setEnd_date(LocalDate.of(2021, 10, 11));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("These dates conflict with an already existing reservation for this host", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDAndEDAreSurroundingTheEDOfExistingReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 12));
        res.setEnd_date(LocalDate.of(2021, 10, 14));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("These dates conflict with an already existing reservation for this host", result.getMessages().get(0));
    }

    @Test
    void shouldNotAddReservationWhenSDAndEDAreEqualToTheSDAndEDOfExistingReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 10));
        res.setEnd_date(LocalDate.of(2021, 10, 13));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal(300.00));

        Result<Reservation> result = service.addReservation(res);
        assertFalse(result.isSuccess());
        assertEquals("These dates conflict with an already existing reservation for this host", result.getMessages().get(0));
    }

    @Test
    void shouldCalculateAccurateTotalWithHost1() {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 10, 1));
        res.setEnd_date(LocalDate.of(2021, 10, 8));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        BigDecimal total = res.getTotal();

       assertEquals(new BigDecimal("800.00"), total);
    }

    @Test
    void shouldDeleteReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal("300.00"));

        service.addReservation(res);

        Result<Reservation> success = service.deleteReservation(res);

        assertTrue(success.isSuccess());

    }

    @Test
    void shouldNotDeleteNullReservation() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2025, 1, 1));
        res.setEnd_date(LocalDate.of(2025, 1, 4));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal("300.00"));

        service.addReservation(res);

        Result<Reservation> success = service.deleteReservation(null);

        assertFalse(success.isSuccess());

    }

    @Test
    void shouldNotDeleteReservationInThePast() throws DataAccessException {
        Reservation res = new Reservation();
        res.setId(2000);
        res.setStart_date(LocalDate.of(2021, 6, 10));
        res.setEnd_date(LocalDate.of(2021, 6, 30));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setTotal(new BigDecimal("300.00"));

        service.addReservation(res);

        Result<Reservation> success = service.deleteReservation(res);

        assertFalse(success.isSuccess());

    }

}