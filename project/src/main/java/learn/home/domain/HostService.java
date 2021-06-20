package learn.home.domain;

import learn.home.data.DataAccessException;
import learn.home.data.HostRepository;
import learn.home.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() throws DataAccessException {
        return repository.findAll();
    }

    public Host findHostByEmail(String email) throws DataAccessException {
        return repository.findHostByEmail(email);
    }

}
