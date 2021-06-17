package learn.home.data;

import learn.home.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();

//    private final Reservation RES1 = new Reservation();

    public ReservationRepositoryDouble() {
        Reservation res = new Reservation();
        res.setId(1);
        res.setHost(HostRepositoryDouble.HOST1);
        res.setStart_date(LocalDate.of(2021, 10, 10));
        res.setEnd_date(LocalDate.of(2021, 10, 13));
        res.setGuest_id(1);
        res.setGuest(GuestRepositoryDouble.GUEST1);
        res.setTotal(new BigDecimal(300.00));
        reservations.add(res);
    }

//    private final static Reservation RES2 = new Reservation(1, LocalDate.of(2021, 10, 15),
//            LocalDate.of(2021, 10, 18),
//            2, new BigDecimal(300.00));
//    private final static Reservation RES3 = new Reservation(1, LocalDate.of(2021, 10, 19),
//            LocalDate.of(2021, 10, 21),
//            3, new BigDecimal(400.00));
//    private final static Reservation RES4 = new Reservation(1, LocalDate.of(2021, 10, 22),
//            LocalDate.of(2021, 10, 28),
//            4, new BigDecimal(1200.00));

    public List<Reservation> findAllByHostId(String hostId) throws DataAccessException {
        return reservations.stream()
                .filter(r -> r.getHost().getId().equals(hostId))
                .collect(Collectors.toList());
    }

    public Reservation findReservationById(int reservationId, String hostId) throws DataAccessException {
        List<Reservation> all = findAllByHostId(hostId);
        for (Reservation existing : all) {
            if (existing.getId() == reservationId) {
                return existing;
            }
        }
        return null;
    }


    public Reservation addReservation(Reservation reservation) throws DataAccessException {
        reservations.add(reservation);
        return reservation;
    }

    public boolean updateReservation(Reservation reservation) throws DataAccessException {
        return true;
    }

    public boolean deleteReservation(Reservation reservation) throws DataAccessException {
        return true;
    }

}
