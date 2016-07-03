package ua.epam.spring.hometask.dao.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoUtils {

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
    
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
    }
    
    public static <T> Set<T> toSet(List<T> list) {
        return list.stream().collect(Collectors.toSet());
    }
    
}
