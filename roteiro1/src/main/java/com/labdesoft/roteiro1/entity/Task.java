package com.labdesoft.roteiro1.entity;

import com.labdesoft.roteiro1.enums.TaskPriority;
import com.labdesoft.roteiro1.enums.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Todos os detalhes sobre uma tarefa.")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, message = "Descrição da tarefa deve possuir pelo menos 10 caracteres")
    private String description;

    private Boolean completed;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskType type;

    private LocalDate date;

    private Integer deadlineDays;

    private LocalDate creationDate;

    public Task(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        if (this.type.equals(TaskType.DATA))
            return "Task [id=" + id + ", description=" + description + ", completed=" +
                    completed + "priority=" + priority + "date=" + date +"]";
        if (this.type.equals(TaskType.PRAZO))
            return "Task [id=" + id + ", description=" + description + ", completed=" +
                    completed + "priority=" + priority + "deadline=" + deadlineDays +"]";

        return "Task [id=" + id + ", description=" + description + ", completed=" +
                completed + "priority=" + priority +"]";
    }
}