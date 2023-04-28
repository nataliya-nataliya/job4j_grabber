package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGES = "/vacancies/java_developer?page=";
    private final DateTimeParser dateTimeParser;
    public static final List<Post> LIST_OF_VACANCIES = new ArrayList<>();
    public static final int NUMBER_OF_PAGES = 5;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static String generateLinkOfPage(int numberOfPage) {
        return String.format("%s%s%d", SOURCE_LINK, PAGES, numberOfPage);
    }

    public static String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element description = document.select(".vacancy-description__text").first();
        return description.text();
    }

    @Override
    public List<Post> list(String linkOfVacanciesPage) {
        Connection connection = Jsoup.connect(linkOfVacanciesPage);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element dateElement = row.select(".vacancy-card__date").first();
            Element linkElement = titleElement.child(0);
            Element dateChildElement = dateElement.child(0);
            String vacancyName = titleElement.text();
            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String date = String.format("%s", dateChildElement.attr("datetime"));
            String description = retrieveDescription(link);
            LIST_OF_VACANCIES.add(new Post(vacancyName, link, description, dateTimeParser.parse(date)));
        });
        return LIST_OF_VACANCIES;
    }

    @Override
    public List<Post> listWithAllPages() {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        for (int i = 1; i <= NUMBER_OF_PAGES; i++) {
            habrCareerParse.list(generateLinkOfPage(i));
        }
        return LIST_OF_VACANCIES;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        for (int i = 2; i <= NUMBER_OF_PAGES; i++) {
            habrCareerParse.list(generateLinkOfPage(i));
        }
        System.out.println(LIST_OF_VACANCIES);
    }
}
