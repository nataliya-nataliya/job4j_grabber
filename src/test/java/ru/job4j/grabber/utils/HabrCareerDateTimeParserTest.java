package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HabrCareerDateTimeParserTest {

    @Test
    void convertedStringWithTimezoneToLocalDateTimeWithoutTimezone() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String date = "2023-01-01T12:00:00+03:00";
        LocalDateTime expected = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        assertThat(expected).isEqualTo(parser.parse(date));
    }
}
