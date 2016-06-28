package ua.epam.spring.hometask.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Yuriy_Tkach
 */
public class Auditorium  extends AbstractDomainObject {

    private String name;
    private String code;
    private long numberOfSeats;
    private Set<Long> vipSeats = Collections.emptySet();

    public Auditorium() {
    }
    
    public Auditorium(String name, String code, long numberOfSeats,
            Set<Long> vipSeats) {
        this.name = name;
        this.code = code;
        this.numberOfSeats = numberOfSeats;
        this.vipSeats = vipSeats;
    }

    public Auditorium(Auditorium auditorium) {
        this.name = auditorium.name;
        this.vipSeats = new HashSet<>(auditorium.vipSeats);
        this.numberOfSeats = auditorium.numberOfSeats;
        this.code = auditorium.code;
    }

    /**
     * Counts how many vip seats are there in supplied <code>seats</code>
     * 
     * @param seats
     *            Seats to process
     * @return number of vip seats in request
     */
    public long countVipSeats(Collection<Long> seats) {
        return seats.stream().filter(seat -> vipSeats.contains(seat)).count();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(long numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
    
    public Set<Long> getAllSeats() {
        return LongStream.range(1, numberOfSeats + 1).boxed().collect(Collectors.toSet());
    }

    public Set<Long> getVipSeats() {
        return vipSeats;
    }

    public void setVipSeats(Set<Long> vipSeats) {
        this.vipSeats = vipSeats;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public boolean equals(Object obj) {
        Auditorium other = (Auditorium) obj;
        return super.equals(other) && Objects.equals(name, other.name) &&
                Objects.equals(code, other.code);
    }

}
