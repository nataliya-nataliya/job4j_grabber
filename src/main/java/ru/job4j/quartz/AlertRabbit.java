package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    private static final Properties propertiesRabbit = load("rabbit.properties");

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(
                            Integer.parseInt(propertiesRabbit
                                    .getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(Integer.parseInt(propertiesRabbit.getProperty("thread.sleep")));
            scheduler.shutdown();
        } catch (SchedulerException | InterruptedException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            try (PreparedStatement statement = connection().prepareStatement(
                    "insert into rabbit(created_date) values(?);")) {
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static Properties load(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = Rabbit.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return properties;
    }

    public static Connection connection() {
        Connection connection = null;
        try {
            Class.forName(propertiesRabbit.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    propertiesRabbit.getProperty("url"),
                    propertiesRabbit.getProperty("username"),
                    propertiesRabbit.getProperty("password"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
