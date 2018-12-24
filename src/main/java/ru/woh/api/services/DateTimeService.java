package ru.woh.api.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeService {
    public static Date beginOfTheDay(Date date) {
        LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return Date.from(now.with(LocalDateTime.MIN).toInstant(ZoneOffset.of(TimeZone.getDefault().getID())));
    }

    public static Date endOfTheDay(Date date) {
        LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return Date.from(now.with(LocalDateTime.MAX).toInstant(ZoneOffset.of(TimeZone.getDefault().getID())));
    }
}
