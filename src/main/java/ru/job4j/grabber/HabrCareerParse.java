package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGES = "/vacancies/java_developer?page=";
    private static final int NUMBER_OF_PAGES = 5;

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

    public static void printVacancies(int numberOfPages) {
        for (int i = 1; i <= numberOfPages; i++) {
            Connection connection = Jsoup.connect(String.format("%s%s%d", SOURCE_LINK, PAGES, i));
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
                System.out.printf("%s %s %s %s%n", vacancyName, link, date, retrieveDescription(link));
            });
        }
    }

    public static void main(String[] args) {
        printVacancies(NUMBER_OF_PAGES);
    }
}
