package com.msavenkov.crudgradleproject.repository.impl;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.msavenkov.crudgradleproject.exception.NotFoundException;
import com.msavenkov.crudgradleproject.model.Status;
import com.msavenkov.crudgradleproject.model.Writer;
import com.msavenkov.crudgradleproject.repository.WriterRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GsonWriterRepositoryImpl implements WriterRepository {

    private static final String WRITERS_FILE_PATH = "src/main/resources/writers.json";

    //private final Gson gson = new Gson();

    private List<Writer> loadAllWriters() {
        try (Reader reader = new FileReader(WRITERS_FILE_PATH)) {
            //Type type = new TypeToken<List<Writer>>(){}.getType();
            return new ArrayList<>();

        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    private void writeAllWriters(List<Writer> writers) {
        try (java.io.Writer writer = new FileWriter(WRITERS_FILE_PATH)) {
            //gson.toJson(writers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long generateAutoIncrementId(List<Writer> writers) {
        return writers.stream().mapToLong(Writer::getId).max().orElse(0L) + 1;
    }

    @Override
    public List<Writer> getAll() {
        return loadAllWriters();
    }

    @Override
    public Writer create(Writer writerToCreate) {
        List<Writer> currentWriters = loadAllWriters();
        writerToCreate.setId(generateAutoIncrementId(currentWriters));
        writerToCreate.setStatus(Status.ACTIVE);
        currentWriters.add(writerToCreate);
        writeAllWriters(currentWriters);
        return writerToCreate;
    }

    @Override
    public Writer update(Writer writerToUpdate) {
        List<Writer> currentWriters = loadAllWriters();
        List<Writer> updatedWriters = currentWriters.stream().map(existingWriters -> {
            if (existingWriters.getId().equals(writerToUpdate.getId())) {
                writerToUpdate.setPosts(existingWriters.getPosts());
                return writerToUpdate;
            }
            return existingWriters;
        }).collect(Collectors.toList());

        writeAllWriters(updatedWriters);
        return writerToUpdate;
    }

    @Override
    public Writer getById(Long id) {
        return loadAllWriters().stream()
                .filter(writer -> writer.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Writer not found with id: " + id));
    }

    @Override
    public void remove(Long id) {
        List<Writer> writers = getAll();
        for (Writer writer : writers) {
            if (Objects.equals(writer.getId(), id)) {
                writer.setStatus(Status.DELETED);
            }
        }
        writeAllWriters(writers);
    }
}
