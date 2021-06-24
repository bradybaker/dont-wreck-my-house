package learn.home.domain;

import learn.home.data.DataAccessException;
import learn.home.data.GuestRepository;
import learn.home.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() throws DataAccessException {
        return repository.findAll();
    }

    public Guest findGuestByEmail(String email) throws DataAccessException {
        Guest guest = repository.findGuestByEmail(email);
        Result<Guest> result = new Result<>();

        if (guest == null) {
            result.addErrorMessage("Could not find host with email: " + email);
        }

        return guest;
    }
}
