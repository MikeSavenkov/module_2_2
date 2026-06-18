package com.msavenkov.crudgradleproject.repository.gson;

import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.LabelRepository;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GsonLabelRepositoryImpl implements LabelRepository {

    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/test_db";
    static final String USER = "root";
    static final String PASSWORD = "123";

    private List<Label> loadAllLabels() {
        List<Label> labels;
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM label";
            ResultSet resultSet = statement.executeQuery(sql);
            labels = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                Label label = new Label(id, name, Status.valueOf(status));
                labels.add(label);

                System.out.println("\n================\n");
                System.out.println("id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Status: " + status);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return labels;
    }

    private void writeAllLabels(List<Label> labels) {
        try (Writer writer = new FileWriter("FILE_PATH")) {
            //gson.toJson(labels, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long generateAutoIncrementId(List<Label> labels) {
        return labels.stream().mapToLong(Label::getId).max().orElse(0L) + 1;
    }

    @Override
    public List<Label> getAll() {
        return loadAllLabels();
    }

    @Override
    public Label create(Label labelToCreate) {
        List<Label> currentLabels = loadAllLabels();
        labelToCreate.setId(generateAutoIncrementId(currentLabels));
        labelToCreate.setStatus(Status.ACTIVE);
        currentLabels.add(labelToCreate);
        writeAllLabels(currentLabels);
        return labelToCreate;
    }

    @Override
    public Label update(Label labelToUpdate) {
        List<Label> currentLabels = loadAllLabels();
        List<Label> updatedLabels = currentLabels.stream().map(existingLabels -> {
            if (existingLabels.getId().equals(labelToUpdate.getId())) {
                return labelToUpdate;
            }
            return existingLabels;
        }).collect(Collectors.toList());

        writeAllLabels(updatedLabels);
        return labelToUpdate;
    }

    @Override
    public Label getById(Long id) {
        return loadAllLabels().stream()
                .filter(label -> label.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Label not found with id: " + id));
    }

    @Override
    public void remove(Long id) {
        List<Label> labels = getAll();
        for (Label label : labels) {
            if (Objects.equals(label.getId(), id)) {
                label.setStatus(Status.DELETED);
            }
        }
        writeAllLabels(labels);
    }
}
