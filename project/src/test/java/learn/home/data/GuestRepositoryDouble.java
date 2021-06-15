package learn.home.data;

import learn.home.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private final ArrayList<Guest> guests = new ArrayList<>();
    private final static Guest GUEST1 = new Guest(1, "Brady", "Baker", "bradybaker82@gmail.com", "573-573-0000", "MO");
    private final static Guest GUEST2 = new Guest(1, "Free", "Willy", "freewilly82@gmail.com", "573-573-1111", "KY");
    private final static Guest GUEST3 = new Guest(1, "Mac", "Dog", "macdog@gmail.com", "573-573-2222", "AL");
    private final static Guest GUEST4 = new Guest(1, "Bugs", "Bunny", "bugsbunny@gmail.com", "573-573-3333", "TX");

    public GuestRepositoryDouble() {
        guests.add(GUEST1);
        guests.add(GUEST2);
        guests.add(GUEST3);
        guests.add(GUEST4);

    }


    @Override
    public List<Guest> findAll() throws DataAccessException { return new ArrayList<>(guests); }

}
