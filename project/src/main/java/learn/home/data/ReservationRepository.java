package learn.home.data;

import learn.home.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllByHostId(String hostId) throws DataAccessException;

    Reservation addReservation(Reservation reservation) throws DataAccessException;

    boolean deleteReservation(Reservation reservation) throws DataAccessException;
}
