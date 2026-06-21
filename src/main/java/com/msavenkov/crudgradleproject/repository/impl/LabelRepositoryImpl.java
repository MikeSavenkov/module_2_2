package com.msavenkov.crudgradleproject.repository.impl;

import com.msavenkov.crudgradleproject.config.DatabaseConfig;
import com.msavenkov.crudgradleproject.exception.DatabaseException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.LabelRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabelRepositoryImpl implements LabelRepository {

    private static final String DATABASE_URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    private static final String SQL_SELECT = "SELECT * FROM labels";
    private static final String SQL_INSERT = "INSERT INTO labels (name, status) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE labels SET name = ?, status = ? WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM labels WHERE id = ?";
    //todo поменять на update, т.к. мы меняем статус а не удаляем запись
    private static final String SQL_DELETE = "DELETE FROM labels WHERE id = ?";

    private List<Label> loadAllLabels() {
        List<Label> labels;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT)) {
                labels = new ArrayList<>();
                while (resultSet.next()) {
                    long id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String status = resultSet.getString("status");
                    Label label = new Label(id, name, Status.valueOf(status));
                    labels.add(label);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Failed to load labels", e);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException("Failed to connect database", e);
        }
        return labels;
    }

    @Override
    public List<Label> getAll() {
        return loadAllLabels();
    }

    @Override
    public Label create(Label labelToCreate) {

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {

            pstmt.setString(1, labelToCreate.getName());
            pstmt.setString(2, Status.ACTIVE.name());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to create new label", e);
        }
        return labelToCreate;
    }

    @Override
    public Label update(Label labelToUpdate) {

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

            pstmt.setString(1, labelToUpdate.getName());
            pstmt.setString(2, labelToUpdate.getStatus().name());
            pstmt.setLong(3, labelToUpdate.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update label", e);
        }
        return labelToUpdate;
    }

    @Override
    public Label getById(Long id) {
        Label label = null;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

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
            } catch (SQLException e) {
                throw new DatabaseException("Failed get label by id = " + id, e);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect database", e);
        }
        return label;
    }

    @Override
    public void remove(Long id) {

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed remove label by id = " + id, e);
        }
    }
}
