package ua.epam.spring.hometask.service.impl;

import java.util.Objects;
import java.util.Set;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

public class DefaultAuditoriumService implements AuditoriumService {

    private AuditoriumDao auditoriumDao;
    
    @Override
    public Set<Auditorium> getAll() {
        return auditoriumDao.getAll();
    }

    @Override
    public Auditorium getByName(String name) {
        Objects.requireNonNull(name);
        return auditoriumDao.findByName(name);
    }

    public void setAuditoriumDao(AuditoriumDao auditoriumDao) {
        this.auditoriumDao = auditoriumDao;
    }

    @Override
    public Auditorium getByCode(String code) {
        Objects.requireNonNull(code);
        return auditoriumDao.getByCode(code);
    }

}
