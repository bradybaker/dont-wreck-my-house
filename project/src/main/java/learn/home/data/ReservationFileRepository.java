package learn.home.data;

import learn.home.models.Guest;
import learn.home.models.Host;
import learn.home.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository{

    private final String directory;
    private final String HEADER = "id,start_date,end_date,guest_id,total";
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public ReservationFileRepository(@Value("${reservationDirPath}") String directory) {
        this.directory = directory;
    }

    @Override
    public List<Reservation> findAllByHostId(String hostId) throws DataAccessException {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {
            reader.readLine(); // skip header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, hostId));
                }
            }
        } catch (FileNotFoundException ex) {
            // ignore since it is ok that file may not exist when app starts
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
        return result;
    }

    public Reservation addReservation(Reservation reservation) throws DataAccessException {
        List<Reservation> all = findAllByHostId(reservation.getHost().getId());
        all.add(reservation);
        writeAll(all, reservation.getHost().getId());
        return reservation;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

//    private Reservation lineToReservation(String line) {
//        String[] fields = line.split(DELIMITER);
//
//        if (fields.length != 5) {
//            return null;
//        }
//
//        Reservation reservation = new Reservation(
//                Integer.parseInt(fields[0]),
//                LocalDate.parse(fields[1]),
//                LocalDate.parse(fields[2]),
//                Integer.parseInt(fields[3]),
//                new BigDecimal(fields[4])
//        );
//
//        return reservation;
//    }

    private Reservation deserialize(String[] fields, String hostId) {
        Reservation result = new Reservation();
        result.setId(Integer.parseInt(fields[0]));
        result.setStart_date(LocalDate.parse(fields[1]));
        result.setEnd_date(LocalDate.parse(fields[2]));
        result.setGuest_id(Integer.parseInt(fields[3]));
        result.setTotal(new BigDecimal(fields[4]));

        Guest guest = new Guest();
        guest.setGuest_id(Integer.parseInt(fields[3]));
        result.setGuest(guest);

        Host host = new Host();
        host.setId(hostId);
        result.setHost(host);

        return result;
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {

            writer.println(HEADER);

            for (Reservation item : reservations) {
                writer.println(serialize(item));
            }
        } catch (FileNotFoundException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private String serialize(Reservation reservation) {
        StringBuilder buffer = new StringBuilder(100);
        buffer.append(reservation.getId()).append(DELIMITER);
        buffer.append(reservation.getStart_date()).append(DELIMITER);
        buffer.append(reservation.getEnd_date()).append(DELIMITER);
        buffer.append(reservation.getGuest_id()).append(DELIMITER);
        buffer.append(reservation.getTotal());
        return buffer.toString();
    }

    private String clean(String value) {
        return value.replace(DELIMITER, DELIMITER_REPLACEMENT);
    }

    private String restore(String value) {
        return value.replace(DELIMITER_REPLACEMENT, DELIMITER);
    }
}
