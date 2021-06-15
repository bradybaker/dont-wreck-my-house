package learn.home.data;

import learn.home.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll() throws DataAccessException;
}
