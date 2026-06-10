package com.msavenkov.crudgradleproject.controller;

import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.model.Writer;
import com.msavenkov.crudgradleproject.repository.WriterRepository;

import java.util.List;

public class WriterController {
    private final WriterRepository writerRepository;

    public WriterController(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    public Writer getWriterById(Long id) {
        return writerRepository.getById(id);
    }

    public List<Writer> getAllWriters() {
        return writerRepository.getAll();
    }

    public void deleteWriter(Long id) {
        writerRepository.remove(id);
    }

    public void createWriter(String firstName, String lastName, long countPosts) {
        Writer writer = new Writer(firstName, lastName, countPosts);
        writerRepository.create(writer);
    }

    public void updateWriter(long id, String firstName, String lastName) {
        Writer writer = new Writer(id, firstName, lastName, Status.ACTIVE);
        writerRepository.update(writer);
    }
}
