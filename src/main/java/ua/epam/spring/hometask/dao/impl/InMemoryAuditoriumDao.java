package ua.epam.spring.hometask.dao.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.domain.Auditorium;

public class InMemoryAuditoriumDao implements AuditoriumDao {

    private Set<Auditorium> auditoriums = new HashSet<>();
    
    public InMemoryAuditoriumDao(Set<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
    }
    
    private Auditorium getAuditoriumCopy(Auditorium auditorium) {
        return new Auditorium(auditorium);
    }
    
    @Override
    public Set<Auditorium> getAll() {
        return auditoriums.stream().map(this::getAuditoriumCopy).collect(Collectors.toSet());
    }

    @Override
    public Auditorium findByName(String name) {
        return auditoriums.stream().filter(a -> Objects.equals(name, a.getName())).findFirst()
                .map(this::getAuditoriumCopy).orElse(null);
    }

    @Override
    public Auditorium getByCode(String code) {
        return auditoriums.stream().filter(a -> Objects.equals(code, a.getCode())).findFirst()
                .map(this::getAuditoriumCopy).orElse(null);
    }

	public void setAuditoriums(Set<Auditorium> auditoriums) {
		this.auditoriums = auditoriums;
	}
	
	@SuppressWarnings("unused")
	private void checkAuditoriumsProvided() {
		if (CollectionUtils.isEmpty(auditoriums)) {
			throw new IllegalStateException("you should have provided auditoriums");
		}
	} 

}
