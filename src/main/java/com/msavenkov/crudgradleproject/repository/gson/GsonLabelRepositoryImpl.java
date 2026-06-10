package com.msavenkov.crudgradleproject.repository.gson;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.LabelRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GsonLabelRepositoryImpl implements LabelRepository {

    private static final String FILE_PATH = "src/main/resources/labels.json";

    //private final Gson gson = new Gson();

    private List<Label> loadAllLabels() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            //Type type = new TypeToken<List<Label>>(){}.getType();
            //List<Label> labels = gson.fromJson(reader, type);
            return new ArrayList<>();

        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    private void writeAllLabels(List<Label> labels) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
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
