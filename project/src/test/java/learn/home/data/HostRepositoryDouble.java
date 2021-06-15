package learn.home.data;

import learn.home.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final ArrayList<Host> hosts = new ArrayList<>();
    public final static Host HOST1 = new Host("498604db-b6d6-4599-a503-3d8190fda823", "Smith", "smith@gmail.com", "888-888-0000",
            "123 Second Ave", "Townie City", "CA", "90210", new BigDecimal(100.00), new BigDecimal(150.00));
    public final static Host HOST2 = new Host("498604db-b6d6-4599-a503-3d8190fda823", "Smith", "smith@gmail.com", "888-888-0000",
            "123 Second Ave", "Townie City", "CA", "90210", new BigDecimal(100.00), new BigDecimal(150.00));

    public HostRepositoryDouble() {
        hosts.add(HOST1);
        hosts.add(HOST2);
    }

    @Override
    public List<Host> findAll() { return new ArrayList<>(hosts); }

    @Override
    public Host findHostByEmail(String email) {
        return findAll().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

}
