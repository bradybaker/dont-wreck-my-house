package learn.home.domain;

import learn.home.data.*;
import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findReservationByEmail(String email) throws DataAccessException {

        Map<String, Host> hostMap = hostRepository.findAll().stream()
                .collect(Collectors.toMap(Host::getId, i -> i));

        List<Guest> guests = guestRepository.findAll();
        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getGuest_id, i -> i));

        Host host = hostRepository.findHostByEmail(email);
        List<Reservation> result = reservationRepository.findAllByHostId(host.getId());
        for (Reservation reservation : result) {
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getGuest_id()));
        }
        return result;
    }
}
