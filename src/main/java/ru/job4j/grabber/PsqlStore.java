package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final Connection cnn;

    public static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = PsqlStore.class.getClassLoader().getResourceAsStream("post.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return properties;
    }

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO post (name, text, link, created) Values (?, ?, ?, ?)"
                + "on conflict (link) do nothing";
        try (PreparedStatement preparedStatement = cnn.prepareStatement(sql)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setObject(4, post.getCreated());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> listPosts = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnn.prepareStatement("select * from post")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listPosts.add(post(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listPosts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement preparedStatement = cnn.prepareStatement("select * from post where id = ?")) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    post = post(resultSet);
                }
            }
            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Post post(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp("created");
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("text"),
                resultSet.getString("link"),
                localDateTime
        );
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Post post = new Post("java developer", "www.example.com", "java developer job",
                LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        PsqlStore psqlStore = new PsqlStore(load());
        psqlStore.save(post);
        psqlStore.getAll();
        psqlStore.findById(2);
    }
}
