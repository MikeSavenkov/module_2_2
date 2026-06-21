package com.msavenkov.crudgradleproject.model;

import com.msavenkov.crudgradleproject.utils.LabelGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class Post {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<Label> labels;
    private Writer writer;
    private Status status;

    public Post(String title, String content, long countLabels) {
        this.title = title;
        this.content = content;
        labels = LabelGenerator.generateLabels(countLabels);
    }

    public Post(Long id, String title, String content, Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
    }
}
