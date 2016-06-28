package ua.epam.spring.hometask.service.impl;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

@Service("auditoriumService")
public class DefaultAuditoriumService implements AuditoriumService {

    @Resource
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
