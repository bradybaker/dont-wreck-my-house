package learn.home.domain;

import learn.home.data.GuestFileRepository;
import learn.home.data.HostFileRepository;
import learn.home.data.ReservationRepository;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostFileRepository hostFileRepository;
    private final GuestFileRepository guestFileRepository;

    public ReservationService(ReservationRepository reservationRepository, HostFileRepository hostFileRepository, GuestFileRepository guestFileRepository) {
        this.reservationRepository = reservationRepository;
        this.hostFileRepository = hostFileRepository;
        this.guestFileRepository = guestFileRepository;
    }


}
