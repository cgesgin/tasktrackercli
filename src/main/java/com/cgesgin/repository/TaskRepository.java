package com.cgesgin.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cgesgin.model.Task;
import com.cgesgin.model.enums.Status;
import com.cgesgin.model.repository.ITaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskRepository implements ITaskRepository {

    private static final String FILE_DIRECTORY  = "tasktracker/src/main/resources/";
    private static final String FILE_PATH = "tasks.json";

    private File file;

    private  ObjectMapper mapper;

    List<Task> tasks;

    public TaskRepository() {
        this.file = new File(this.FILE_DIRECTORY + this.FILE_PATH);
        this.mapper = new ObjectMapper();
        tasks = new ArrayList<>();
    }

    @Override
    public Optional<Task> save(Task task) {
        try {

            File directoryFile = new File(this.FILE_DIRECTORY);

            if(!directoryFile.exists()){
                directoryFile.mkdirs();
            }

            if(!file.exists()){
                file.createNewFile();
            }

            if (file.length() != 0) {
                tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            }

            tasks.add(task);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);

            return Optional.of(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        try {
            if (file.length() == 0) {
                return false;
            }
            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            for (Task task : tasks) {
                if(task.getId() == id){
                    tasks.remove(task);
                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Optional<Task> update(int id, Task task) {
        try {
            
            if (file.length() == 0) {
                return Optional.empty();
            }

            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                if (t.getId() == id) {
                    t.setDescription(task.getDescription());
                    t.setStatus(task.getStatus());
                    t.setUpdatedAt(task.getUpdatedAt());
                    t.setCreatedAt(task.getCreatedAt());
                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
                    return Optional.of(t);
                }
            }
            return Optional.empty();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Task> get(int id) {
        try {
            if (file.length() == 0) {
                return Optional.empty();
            }
            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            for (Task task : tasks) {
                if(task.getId() == id){
                    return Optional.of(task);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Task>> getAll() {
        try {
            if (file.length() == 0) {
                return Optional.empty();
            }
            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            return Optional.of(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Task>> getByStatus(int id, Status status) {
        try {
            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {});
            List<Task> tasksByStatus = new ArrayList<>();
            for (Task task : tasks) {
                if(task.getId() == id && task.getStatus().equals(status)){
                    tasksByStatus.add(task);
                }
            }
            return Optional.of(tasksByStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}