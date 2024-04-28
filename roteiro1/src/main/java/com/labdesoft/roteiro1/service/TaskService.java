package com.labdesoft.roteiro1.service;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.enums.TaskType;
import com.labdesoft.roteiro1.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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

        if (task.getType().equals(TaskType.LIVRE)){
            task.setDeadlineDays(null);
            task.setDate(null);

        } else if (task.getType().equals(TaskType.DATA)) {
            if (task.getDate() == null){
                throw new IllegalArgumentException("Tarefas do tipo data tem que ter uma data");
            }
            if (task.getDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Data invalida!");
            }
            task.setDeadlineDays(null);
        }
        else if (task.getType().equals(TaskType.PRAZO)) {
            if (task.getDeadlineDays() == null || task.getDeadlineDays() < 0){
                throw new IllegalArgumentException("Tarefas do tipo prazo tem que um prazo valido");
            }
            task.setDate(null);
        }


        task.setCreationDate(LocalDate.now());

        return this.taskRepository.save(task);
    }

    public List<Task> listAll () {
        List<Task> tasks = this.taskRepository.findAll();

        if (tasks.isEmpty()){
            throw new NoSuchElementException("Não há task no momento!");
        }

        tasks.forEach(this::updateStatus);
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

    private void updateStatus(@NotNull Task task){
        String statusString;
        if (task.getCompleted())
            statusString = "Concluida";
        else {
            statusString = "Prevista";
            if (task.getType().equals(TaskType.DATA)) {
                if (task.getDate().isBefore(LocalDate.now())) {
                    Period period = Period.between(task.getDate(), LocalDate.now());
                    statusString = period.getDays() + " dias de atraso";
                }
            } else if (task.getType().equals(TaskType.PRAZO)) {
                int daysUntilDeadLine = daysUntilDeadline(task);
                if (daysUntilDeadLine < 0)
                    statusString = -daysUntilDeadLine + " dias de atraso";
            }
        }

        task.setStatus(statusString);
    }

    private int daysUntilDeadline (Task task){
        Period period = Period.between(task.getCreationDate(), LocalDate.now());

        return task.getDeadlineDays() - period.getDays();
    }
}
