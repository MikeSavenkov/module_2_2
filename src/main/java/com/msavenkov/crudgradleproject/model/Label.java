package com.msavenkov.crudgradleproject.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Label {
    private Long id;
    private String name;
    private Status status;

    public Label(String name) {
        this.name = name;
    }
}
