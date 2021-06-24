package learn.home.data;

import learn.home.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {

    private final String filePath;
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public HostFileRepository(@Value("${hostFilePath}") String filePath) {
        this.filePath = filePath;
    }

    public List<Host> findAll() throws DataAccessException {
        ArrayList<Host> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Host host = lineToHost(line);
                result.add(host);
            }
        } catch (FileNotFoundException ex) {
            // ignore since it is ok that file may not exist when app starts
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return result;
    }

    public Host findHostByEmail(String email) throws DataAccessException {
        Host result = new Host();
        for (Host host : findAll()) {
            if (email.equalsIgnoreCase(host.getEmail())) {
                result = host;
            }
        }
        return result;
    }

    private Host lineToHost(String line) {
        String[] fields = line.split(DELIMITER);

        if (fields.length != 10) {
            return null;
        }

        Host host = new Host(
                fields[0],
                restore(fields[1]),
                restore(fields[2]),
                restore(fields[3]),
                restore(fields[4]),
                restore(fields[5]),
                restore(fields[6]),
                restore(fields[7]),
                new BigDecimal(fields[8]),
                new BigDecimal(fields[9])
        );

        return host;
    }

    private String clean(String value) {
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }
}
