package ua.epam.spring.hometask.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;

@Repository("eventDao")
public class JdbcEventDao extends AbstractJdbcDao<Event> implements EventDao {

    private static final String SELECT_AIR_DATE_AUDITORIUM_FROM_EVENTAIRDATESAUDITORIUMS = "SELECT airDate,auditorium FROM eventAirDatesAuditoriums WHERE eventId=?";
    private static final String SELECT_AIR_DATES_FROM_EVENTAIRDATES = "SELECT airDate FROM eventAirDates WHERE eventId=?";
    private static final String INSERT_EVENT = "INSERT INTO events (name,basePrice,rating) VALUES (?,?,?)";
    private static final String UPDATE_EVENT = "UPDATE events SET name=?, basePrice=?, rating=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM events WHERE id=?";
    private static final String SELECT_BY_ID = "SELECT * FROM events WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM events";
    private static final String SELECT_BY_NAME = "SELECT * FROM events WHERE name=?";
    
    @Resource
    private AuditoriumDao auditoriumDao;
    
    @Override
    public Event save(Event event) {
        //check
        if (canUpdate(event)) {
            updateEvent(event);
            updateAirDates(event);
            updateAuditoriums(event);
        } else {
            saveEvent(event);
            saveAllAirDates(event);
            saveAllAuditoriums(event);
        }
        return event;
    }
    
    private void updateEvent(Event event) {
        Object[] args = new Object[] { event.getName(), event.getBasePrice(), getRatingCode(event.getRating()), event.getId() };
        updateRow(UPDATE_EVENT, args);
    }

    private boolean canUpdate(Event object) {
        return getById(object.getId()) != null;
    }
    
    private void saveEvent(Event event) {
        Object[] args = new Object[] {event.getName(), event.getBasePrice(), getRatingCode(event.getRating()) };
        long generatedId = updateAndGetKey(INSERT_EVENT, args).longValue();
        event.setId(generatedId);
    }
    
    private String getRatingCode(EventRating rating) {
        return rating != null ? rating.name() : null;
    }
    
    private void updateAirDates(Event event) {
        removeExistingAirDates(event);
        saveAllAirDates(event);
    }
    
    private void updateAuditoriums(Event event) {
        removeAllAuditoriums(event);
        saveAllAuditoriums(event);
    }

    private void removeAllAuditoriums(Event event) {
        getJdbcTemplate().update("DELETE FROM eventAirDatesAuditoriums WHERE eventId=?", event.getId());
    }

    private void saveAllAuditoriums(Event event) {
        List<AuditoriumAndAirDate> auditoriums = event.getAuditoriums().entrySet().stream()
            .map(this::mapToAuditoriumAndAirDate).collect(Collectors.toList());
        getJdbcTemplate().batchUpdate("INSERT INTO eventAirDatesAuditoriums(eventId,airDate,auditorium) VALUES(?,?,?)", 
                getBatchStatementSetterForAuditoriums(auditoriums, event.getId()));
    }
    
    private BatchPreparedStatementSetter getBatchStatementSetterForAuditoriums(List<AuditoriumAndAirDate> auditoriums, long eventId) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedstatement, int i) throws SQLException {
                AuditoriumAndAirDate auditoriumAndDate = auditoriums.get(i);
                preparedstatement.setLong(1, eventId);
                preparedstatement.setTimestamp(2, auditoriumAndDate.airDate);
                preparedstatement.setString(3, auditoriumAndDate.auditorium);
            }
            @Override
            public int getBatchSize() {
                return auditoriums.size();
            }
        };
    }
    
    private AuditoriumAndAirDate mapToAuditoriumAndAirDate(Map.Entry<LocalDateTime, String> mapEntry) {
        AuditoriumAndAirDate entity = new AuditoriumAndAirDate();
        entity.airDate = Timestamp.valueOf(mapEntry.getKey());
        entity.auditorium = mapEntry.getValue();
        return entity;
    }

    private void saveAllAirDates(Event event) {
        List<Timestamp> airDates = event.getAirDates()
                .stream().map(Timestamp::valueOf).collect(Collectors.toList());
        getJdbcTemplate().batchUpdate("INSERT INTO eventAirDates(eventId,airDate) VALUES(?,?)", getBatchStatementSetterForAirDates(airDates, event.getId()));
    }
    
    private BatchPreparedStatementSetter getBatchStatementSetterForAirDates(List<Timestamp> airDates, long eventId) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedstatement, int i) throws SQLException {
                preparedstatement.setLong(1, eventId);
                preparedstatement.setTimestamp(2, airDates.get(i));
            }
            @Override
            public int getBatchSize() {
                return airDates.size();
            }
        };
    }

    private void removeExistingAirDates(Event event) {
        getJdbcTemplate().update("DELETE FROM eventAirDates WHERE eventId=?", event.getId());
    }
    
    private NavigableSet<LocalDateTime> getAirDates(long eventId) {
        List<Timestamp> timestamps = getJdbcTemplate().queryForList(SELECT_AIR_DATES_FROM_EVENTAIRDATES, Timestamp.class, eventId);
        return timestamps.stream()
                .map(Timestamp::toLocalDateTime)
                .collect(Collectors.toCollection(TreeSet::new));
    }
    
    private NavigableMap<LocalDateTime, String> getAuditoriums(long eventId) {
        List<AuditoriumAndAirDate> auditoriumAndAirDates = getJdbcTemplate().query(SELECT_AIR_DATE_AUDITORIUM_FROM_EVENTAIRDATESAUDITORIUMS, 
                getAuditoriumAndAirDateRowMapper(), eventId);
        return auditoriumAndAirDates.stream().collect(Collectors.toMap(
                e -> e.airDate.toLocalDateTime(), 
                e -> e.auditorium, 
                (e1, e2) -> e1, 
                TreeMap::new));
    }
    
    private static class AuditoriumAndAirDate {
        private Timestamp airDate;
        private String auditorium;
    }
    
    private RowMapper<AuditoriumAndAirDate> getAuditoriumAndAirDateRowMapper() {
        return (resultSet, rowNumber) -> {
            AuditoriumAndAirDate entity = new AuditoriumAndAirDate();
            entity.airDate = resultSet.getTimestamp("airDate");
            entity.auditorium = resultSet.getString("auditorium");
            return entity;
        };
    }

    @Override
    public void remove(Event object) {
        getJdbcTemplate().update(DELETE_BY_ID, object.getId());
    }

    @Override
    public Event getById(long id) {
        Event event = queryForObject(SELECT_BY_ID, id);
        populateAuditoriumsAndAirDates(event);
        return event;
    }
    
    private void populateAuditoriumsAndAirDates(Event event) {
        if (event != null) {
            event.setAirDates(getAirDates(event.getId()));
            event.setAuditoriums(getAuditoriums(event.getId()));
        }
    }

    @Override
    public Collection<Event> getAll() {
        List<Event> events = queryForList(SELECT_ALL, new Object[] {});
        events.forEach(this::populateAuditoriumsAndAirDates);
        return events;
    }

    @Override
    public Event getByName(String name) {
        Event event = queryForObject(SELECT_BY_NAME, name);
        populateAuditoriumsAndAirDates(event);
        return event;
    }

    @Override
    public NavigableMap<LocalDateTime, Auditorium> getAuditoriumAssignments(
            Event event) {
        return event.getAuditoriums().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), 
                            e -> auditoriumDao.getByCode(e.getValue()), 
                            (e1, e2) -> e1,
                            TreeMap::new));
    }

    @Override
    protected RowMapper<Event> getRowMapper() {
        return (resultSet, rowNumber) -> {
            Event event = new Event();
            event.setId(resultSet.getLong("id"));
            event.setName(resultSet.getString("name"));
            event.setRating(EventRating.valueOf(resultSet.getString("rating")));
            event.setBasePrice(resultSet.getDouble("basePrice"));
            return event;
        };
    }

}
