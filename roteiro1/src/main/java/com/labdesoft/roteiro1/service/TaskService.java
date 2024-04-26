package com.labdesoft.roteiro1.service;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task create (@NotNull Task task) {
        task.setId(null);
        task.setCompleted(false);
        return this.taskRepository.save(task);
    }

    public List<Task> listAll () {
        List<Task> tasks = this.taskRepository.findAll();

        if (tasks.isEmpty()){
            throw new NoSuchElementException("Não há task no momento!");
        }
        return  tasks;
    }

    public Task conclude (@NotNull long id) {
        Task taskToConclude = findById(id);
        taskToConclude.setCompleted(Boolean.TRUE);
        return this.taskRepository.save(taskToConclude);
    }

    public Task findById(@NotNull long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Task não encontrada, id: %s", id)));
    }
}
