package com.labdesoft.roteiro1.controller;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/task")
    @Operation(summary = "Lista todas as tarefas da lista")
    public ResponseEntity<List<Task>> listAll(){
        try{
            List<Task> taskList = new ArrayList<>();
            taskRepository.findAll().forEach(taskList::add);

            if(taskList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(taskList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/task")
    @Operation(summary = "Adicionar uma nova task")
    public ResponseEntity<Void> create(@Valid @RequestBody Task task){
        try{
            task.setId(null);
            task.setCompleted(false);
            taskRepository.save(task);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/task/{id}")
    @Operation(summary = "Conclui uma nova task")
    public ResponseEntity<Void> update(@PathVariable Long id){
        try{
            Optional<Task> newTask = taskRepository.findById(id);
            newTask.get().setCompleted(Boolean.TRUE);
            taskRepository.save(newTask.get());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
