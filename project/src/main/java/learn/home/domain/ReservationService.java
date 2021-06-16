package learn.home.domain;

import learn.home.data.*;
import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
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

        Map<Integer, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getGuest_id, i -> i));

        Host host = hostRepository.findHostByEmail(email);
        List<Reservation> result = reservationRepository.findAllByHostId(host.getId());
        for (Reservation reservation : result) {
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getGuest_id()));
        }

        List<Reservation> sortByStartDate = result.stream().sorted(Comparator.comparing(Reservation::getStart_date))
                .collect(Collectors.toList());


        return sortByStartDate;
    }

    public Result<Reservation> addReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.addReservation(reservation));

        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = new Result<>();

        if (reservation.getStart_date() == null) {
            result.addErrorMessage("Reservation start date is required");
        }

        if (reservation.getEnd_date() == null) {
            result.addErrorMessage("Reservation end date is required");
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host not found");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest not found");
        }

        if (reservation.getStart_date().isAfter(reservation.getEnd_date())) {
            result.addErrorMessage("Reservation start date must be before the end date");
        }

        if (reservation.getStart_date().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation start date must be in the future");
        }

        List<Reservation> all = reservationRepository.findAllByHostId(reservation.getHost().getId());
        for (Reservation existing : all) {
            boolean isOverlap = !(existing.getStart_date().compareTo(reservation.getEnd_date()) >= 0
                        || existing.getEnd_date().compareTo(reservation.getStart_date()) <= 0);

            if (isOverlap) {
               result.addErrorMessage("These dates conflict with an already existing reservation for this host");
               return result;
            }
//            if (reservation.getStart_date().isBefore(existing.getStart_date()) && reservation.getEnd_date().isAfter(existing.getEnd_date())) {
//
//            }
        }
        return result;
    }
}
