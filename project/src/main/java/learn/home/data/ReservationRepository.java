package learn.home.data;

import learn.home.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllByHostId(String hostId) throws DataAccessException;
}
