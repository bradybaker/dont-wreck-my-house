package learn.home.data;

import learn.home.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private final ArrayList<Guest> guests = new ArrayList<>();
    public final static Guest GUEST1 = new Guest(1, "Brady", "Baker", "bradybaker82@gmail.com", "573-573-0000", "MO");
    public final static Guest GUEST2 = new Guest(2, "Free", "Willy", "freewilly82@gmail.com", "573-573-1111", "KY");
    public final static Guest GUEST3 = new Guest(3, "Mac", "Dog", "macdog@gmail.com", "573-573-2222", "AL");
    public final static Guest GUEST4 = new Guest(4, "Bugs", "Bunny", "bugsbunny@gmail.com", "573-573-3333", "TX");

    public GuestRepositoryDouble() {
        guests.add(GUEST1);
        guests.add(GUEST2);
        guests.add(GUEST3);
        guests.add(GUEST4);

    }


    @Override
    public List<Guest> findAll() throws DataAccessException { return new ArrayList<>(guests); }

    @Override
    public Guest findGuestByEmail(String email) throws DataAccessException {
        return findAll().stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

}
