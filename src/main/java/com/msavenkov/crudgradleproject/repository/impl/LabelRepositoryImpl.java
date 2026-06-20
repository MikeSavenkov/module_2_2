package com.msavenkov.crudgradleproject.repository.impl;

import com.msavenkov.crudgradleproject.config.DatabaseConfig;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.LabelRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabelRepositoryImpl implements LabelRepository {

    static final String DATABASE_URL = DatabaseConfig.getUrl();
    static final String USER = DatabaseConfig.getUser();
    static final String PASSWORD = DatabaseConfig.getPassword();

    private List<Label> loadAllLabels() {
        List<Label> labels;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM label";
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                labels = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String status = resultSet.getString("status");
                    Label label = new Label(id, name, Status.valueOf(status));
                    labels.add(label);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return labels;
    }

    @Override
    public List<Label> getAll() {
        return loadAllLabels();
    }

    @Override
    public Label create(Label labelToCreate) {

        String sql = "INSERT INTO label (name, status) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, labelToCreate.getName());
            pstmt.setString(2, Status.ACTIVE.name());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labelToCreate;
    }

    @Override
    public Label update(Label labelToUpdate) {
        String sql = "UPDATE label SET name = ?, status = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, labelToUpdate.getName());
            pstmt.setString(2, labelToUpdate.getStatus().name());
            pstmt.setLong(3, labelToUpdate.getId());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labelToUpdate;
    }

    @Override
    public Label getById(Long id) {
        String sql = "SELECT * FROM label WHERE id = ?";
        Label label = null;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                long labelId = 0;
                if (rs.next()) {
                    labelId = rs.getLong("id");
                    String name = rs.getString("name");
                    String status = rs.getString("status");
                    label = new Label(labelId, name, Status.valueOf(status));

                } else {
                    System.out.println("Пользователь с ID " + labelId + " не найден");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM label WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
