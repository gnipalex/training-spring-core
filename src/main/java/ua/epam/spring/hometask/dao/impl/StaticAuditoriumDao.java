package ua.epam.spring.hometask.dao.impl;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.domain.Auditorium;

public class StaticAuditoriumDao implements AuditoriumDao {

    private Set<Auditorium> auditoriums;
    private Map<String, Auditorium> auditoriumMap;
    
    public StaticAuditoriumDao(Set<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
        this.auditoriumMap = auditoriums.stream().collect(Collectors.toMap(Auditorium::getName, a -> a, (a1, a2) -> a1));
    }
    
    @Override
    public Set<Auditorium> getAll() {
        return auditoriums.stream().map(Auditorium::new).collect(Collectors.toSet());
    }

    @Override
    public Auditorium findByName(String name) {
        Optional<Auditorium> auditorium = Optional.ofNullable(auditoriumMap.get(name)).map(Auditorium::new);
        return auditorium.orElse(null);
    }

}
