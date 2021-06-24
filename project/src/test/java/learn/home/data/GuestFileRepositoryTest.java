package learn.home.data;

import learn.home.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private final String SEED_PATH = "./data/guests-seed.csv";
    private final String TEST_PATH = "./data/guests-test.csv";
    private static final int GUEST_COUNT = 1000;

    public GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllGuests() throws DataAccessException {
        List<Guest> all = repository.findAll();

        assertEquals(GUEST_COUNT, all.size());
    }

    @Test
    void shouldFindGuestByEmail() throws DataAccessException {
        Guest guest = repository.findGuestByEmail("slomas0@mediafire.com");
        assertEquals("slomas0@mediafire.com", guest.getEmail());
        assertEquals("Sullivan", guest.getFirst_name());
    }

}