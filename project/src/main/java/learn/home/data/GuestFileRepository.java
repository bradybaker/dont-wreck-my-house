package learn.home.data;

import learn.home.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class GuestFileRepository implements GuestRepository {

    private final String filePath;
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public GuestFileRepository(@Value("${guestFilePath}") String filePath) {
        this.filePath = filePath;
    }

    public List<Guest> findAll() throws DataAccessException {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Guest guest = lineToGuest(line);
                result.add(guest);
            }
        } catch (FileNotFoundException ex) {
            // ignore since it is ok that file may not exist when app starts
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return result;
    }

    public Guest findGuestByEmail(String email) throws DataAccessException {
        Guest result = new Guest();
        for (Guest guest : findAll()) {
            if (email.equalsIgnoreCase(guest.getEmail())) {
                result = guest;
            }
        }
        return result;
    }

    private Guest lineToGuest(String line) {
        String[] fields = line.split(DELIMITER);

        if (fields.length != 6) {
            return null;
        }

        Guest guest = new Guest(
                Integer.parseInt(fields[0]),
                restore(fields[1]),
                restore(fields[2]),
                restore(fields[3]),
                restore(fields[4]),
                restore(fields[5])
        );

        return guest;
    }

    private String clean(String value) {
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }
}
