package ru.woh.api.services;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeService {
    public static Date beginOfTheDay(Date date) {
        return Date.from(date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .with(LocalTime.MIN)
            .atZone(ZoneId.systemDefault())
            .toInstant());
    }

    public static Date endOfTheDay(Date date) {
        return Date.from(date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .with(LocalTime.MAX)
            .atZone(ZoneId.systemDefault())
            .toInstant());
    }
}
