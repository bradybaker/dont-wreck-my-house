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

        return result.stream().sorted(Comparator.comparing(Reservation::getStart_date))
                .collect(Collectors.toList());
    }

    public List<Reservation> filterReservationsByGuestEmail(String hostEmail, String guestEmail) throws DataAccessException {
        List<Reservation> all = findReservationByEmail(hostEmail);
        return all.stream().filter(r -> r.getGuest().getEmail().equalsIgnoreCase(guestEmail))
                .sorted(Comparator.comparing(Reservation::getStart_date)).collect(Collectors.toList());
    }

    public Reservation findReservationById(int reservationId, String hostId) throws DataAccessException {
        Reservation res = reservationRepository.findReservationById(reservationId, hostId);
        Result<Reservation> result = new Result<>();

        if (res == null) {
            result.addErrorMessage("No reservation found with the ID of " + reservationId);
        }

        return res;
    }

    public Result<Reservation> addReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        if (reservation.getStart_date().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation start date must be in the future");
            return result;
        }

        List<Reservation> all = reservationRepository.findAllByHostId(reservation.getHost().getId());
        for (Reservation existing : all) {
            boolean isOverlap = !(existing.getStart_date().compareTo(reservation.getEnd_date()) >= 0
                    || existing.getEnd_date().compareTo(reservation.getStart_date()) <= 0);

            if (isOverlap) {
                result.addErrorMessage("These dates conflict with an already existing reservation for this host");
                return result;
            }
        }

        result.setPayload(reservationRepository.addReservation(reservation));

        return result;
    }

    public Result<Reservation> updateReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validate(reservation);

        List<Reservation> all = reservationRepository.findAllByHostId(reservation.getHost().getId());
        for (Reservation existing : all) {
            boolean resSurroundsExisting = existing.getStart_date().isAfter(reservation.getStart_date()) && existing.getEnd_date().isBefore(reservation.getEnd_date());
            boolean resSurroundsExistingStart = reservation.getStart_date().isBefore(existing.getStart_date()) && reservation.getEnd_date().isAfter(existing.getStart_date()) && reservation.getEnd_date().isBefore(existing.getEnd_date());
            boolean resSurroundsExistingEnd = reservation.getStart_date().isAfter(existing.getStart_date()) && reservation.getStart_date().isBefore(existing.getStart_date()) && reservation.getEnd_date().isAfter(existing.getEnd_date());
            boolean resEqualsExisting = reservation.getStart_date().equals(existing.getStart_date()) && reservation.getEnd_date().equals(existing.getEnd_date());
            boolean existingSurroundsRes = existing.getStart_date().isBefore(reservation.getStart_date()) && existing.getEnd_date().isAfter(reservation.getEnd_date());
            boolean existingEndEqualsResEndResStartIsBefore = reservation.getStart_date().isBefore(existing.getStart_date()) && reservation.getEnd_date().equals(existing.getEnd_date());
            boolean existingEndEqualsResResStartIsAfter = reservation.getStart_date().isAfter(existing.getStart_date()) && reservation.getEnd_date().equals(existing.getEnd_date());
            boolean existingStartEqualsResStartResEndIsBefore = reservation.getStart_date().equals(existing.getStart_date()) && reservation.getEnd_date().isBefore(existing.getEnd_date());
            boolean existingStartEqualsResStartResEndIsAfter = reservation.getStart_date().equals(existing.getStart_date()) && reservation.getEnd_date().isAfter(existing.getEnd_date());
            boolean idsNotEqual = existing.getId() != reservation.getId();


            boolean isOverlap = (resEqualsExisting || resSurroundsExisting || resSurroundsExistingStart || resSurroundsExistingEnd ||
                    existingSurroundsRes || existingEndEqualsResEndResStartIsBefore || existingEndEqualsResResStartIsAfter ||
                    existingStartEqualsResStartResEndIsBefore || existingStartEqualsResStartResEndIsAfter);
            if (isOverlap && idsNotEqual) {
                result.addErrorMessage("These dates conflict with an already existing reservation for this host");
                return result;
            }
        }

        boolean success = reservationRepository.updateReservation(reservation);
        if(!success) {
            result.addErrorMessage("Could not update reservation");
        }

        return result;
    }

    public Result<Reservation> deleteReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("No reservation found");
            return result;
        }

        if (reservation.getStart_date().isBefore(LocalDate.now())) {
            result.addErrorMessage("Cannot delete current reservations or reservations that are in the past");
            return result;
        }

        boolean success = reservationRepository.deleteReservation(reservation);
        if(!success) {
            result.addErrorMessage("Could not find reservation");
        }

        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = new Result<>();

        if (reservation.getStart_date() == null) {
            result.addErrorMessage("Reservation start date is required");
            return result;
        }

        if (reservation.getEnd_date() == null) {
            result.addErrorMessage("Reservation end date is required");
            return result;
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host not found");
            return result;
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest not found");
            return result;
        }

        if (reservation.getStart_date().isAfter(reservation.getEnd_date())) {
            result.addErrorMessage("Reservation start date must be before the end date");
        }

        return result;
    }
}
