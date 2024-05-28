package com.labdesoft.roteiro1.controller;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/task")
    @Operation(summary = "Lista todas as tarefas da lista")
    public ResponseEntity<List<Task>> listAll(){
        try{
            List<Task> taskList = this.taskService.listAll();

            return new ResponseEntity<>(taskList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/task/{id}")
    @Operation(summary = "Pega uma tarefa da lista")
    public ResponseEntity<Task> findOne(@PathVariable Long id){
        try{
            Task task = this.taskService.findById(id);

            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/task")
    @Operation(summary = "Adicionar uma nova task")
    public ResponseEntity<Task> create(@Valid @RequestBody Task task){
        try{
            Task taskCreated = this.taskService.create(task);

            return new ResponseEntity<>(taskCreated, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/task/{id}")
    @Operation(summary = "Conclui uma nova task")
    public ResponseEntity<Task> update(@PathVariable Long id){
        try{
            Task task = this.taskService.conclude(id);

            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
