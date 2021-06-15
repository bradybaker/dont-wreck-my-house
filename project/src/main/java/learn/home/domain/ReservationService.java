package learn.home.domain;

import learn.home.data.DataAccessException;
import learn.home.data.GuestFileRepository;
import learn.home.data.HostFileRepository;
import learn.home.data.ReservationRepository;
import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostFileRepository hostFileRepository;
    private final GuestFileRepository guestFileRepository;

    public ReservationService(ReservationRepository reservationRepository, HostFileRepository hostFileRepository, GuestFileRepository guestFileRepository) {
        this.reservationRepository = reservationRepository;
        this.hostFileRepository = hostFileRepository;
        this.guestFileRepository = guestFileRepository;
    }

    public List<Reservation> findReservationByEmail(String email) throws DataAccessException {

        Map<String, Host> hostMap = hostFileRepository.findAll().stream()
                .collect(Collectors.toMap(Host::getId, i -> i));

        Map<Integer, Guest> guestMap = guestFileRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getGuest_id, i -> i));

        Host host = hostFileRepository.findHostByEmail(email);
        List<Reservation> result = reservationRepository.findAllByHostId(host.getId());
        for (Reservation reservation : result) {
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getGuest_id()));
        }

        return result;
    }
}
