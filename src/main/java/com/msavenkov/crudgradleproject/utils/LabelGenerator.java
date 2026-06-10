package com.msavenkov.crudgradleproject.utils;

import com.msavenkov.crudgradleproject.model.Label;
import com.msavenkov.crudgradleproject.model.Status;

import java.util.ArrayList;
import java.util.List;

public class LabelGenerator {
    public static List<Label> generateLabels(long count) {
        List<Label> labels = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Label label = new Label(i, "label" + i, Status.ACTIVE);
            labels.add(label);
        }
        return labels;
    }
}
