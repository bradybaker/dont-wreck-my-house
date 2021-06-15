package learn.home.data;

import learn.home.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private final String SEED_PATH = "./data/hosts-seed.csv";
    private final String TEST_PATH = "./data/hosts-test.csv";
    private static final int HOST_COUNT = 1000;

    public HostFileRepository repository = new HostFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAllHosts() throws DataAccessException {
        List<Host> all = repository.findAll();

        assertEquals(HOST_COUNT, all.size());
    }

}