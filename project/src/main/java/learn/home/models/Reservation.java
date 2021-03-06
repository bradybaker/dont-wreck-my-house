package learn.home.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Reservation {

    private int id;
    private Host host;
    private LocalDate start_date;
    private LocalDate end_date;
    private int guest_id;
    private Guest guest;
    private BigDecimal total;

    public Reservation() {

    }

    public Reservation(int id, LocalDate start_date, LocalDate end_date, int guest_id) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.guest_id = guest_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public BigDecimal getTotal() { return total; }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void calculateTotal() {
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal standardRate = new BigDecimal(String.valueOf(host.getStandard_rate()));
        BigDecimal weekendRate = new BigDecimal(String.valueOf(host.getWeekend_rate()));

        for (LocalDate current = start_date; current.compareTo(end_date) < 0; current = current.plusDays(1)) {
            if (current.getDayOfWeek() == DayOfWeek.FRIDAY || current.getDayOfWeek() == DayOfWeek.SATURDAY) {
                total = total.add(weekendRate);
            } else {
                total = total.add(standardRate);
            }
        }
        setTotal(total);
    }
}
