package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse.substring(0, parse.indexOf("+")), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
