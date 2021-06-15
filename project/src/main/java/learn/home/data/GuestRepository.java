package learn.home.data;

import learn.home.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll() throws DataAccessException;
}
