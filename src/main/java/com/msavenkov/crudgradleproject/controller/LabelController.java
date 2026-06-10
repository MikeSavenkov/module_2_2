package com.msavenkov.crudgradleproject.controller;

import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.repository.LabelRepository;

import java.util.List;

public class LabelController {

    private final LabelRepository labelRepository;

    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label getLabelById(Long id) {
        return labelRepository.getById(id);
    }

    public List<Label> getAllLabels() {
        return labelRepository.getAll();
    }

    public void deleteLabel(Long id) {
        labelRepository.remove(id);
    }

    public void createLabel(String name) {
        Label label = new Label(name);
        labelRepository.create(label);
    }

    public void updateLabel(long id, String name) {
        Label label = new Label(id, name, Status.ACTIVE);
        labelRepository.update(label);
    }
}
