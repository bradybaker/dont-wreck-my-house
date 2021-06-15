package learn.home.data;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private final String SEED_PATH = "./data/guests-seed.csv";
    private final String TEST_PATH = "./data/guests-test.csv";

    public GuestFileRepository repository = new GuestFileRepository(TEST_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Files.copy(Paths.get(SEED_PATH), Paths.get(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
    }

}