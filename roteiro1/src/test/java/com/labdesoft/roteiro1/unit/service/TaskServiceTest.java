package com.labdesoft.roteiro1.unit.service;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.enums.TaskPriority;
import com.labdesoft.roteiro1.enums.TaskType;
import com.labdesoft.roteiro1.repository.TaskRepository;
import com.labdesoft.roteiro1.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    public void should_save_a_task() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.LIVRE);
        task.setPriority(TaskPriority.MEDIA);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        // when
        Task saved = taskService.create(task);

        // then
        assertThat(saved).isEqualTo(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_save_DataTask_without_date_should_throw_exeception() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.DATA);
        task.setPriority(TaskPriority.MEDIA);

        // when
        taskService.create(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_save_DataTask_with_date_b4_today_should_throw_exeception() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.DATA);
        task.setPriority(TaskPriority.MEDIA);
        task.setDate(LocalDate.of(2024, 5,10));

        // when
        taskService.create(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_save_PrazoTask_with_negative_deadline_should_throw_exeception() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.PRAZO);
        task.setPriority(TaskPriority.MEDIA);
        task.setDeadlineDays(-1);

        // when
        taskService.create(task);
    }

    @Test
    public void should_find_a_task_by_id(){
        // given
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Descrição de teste para task");
        task.setType(TaskType.LIVRE);
        task.setPriority(TaskPriority.MEDIA);
        task.setCompleted(false);

        // Configure o comportamento do repositório simulado
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        // when
        Task foundTask = taskService.findById(1L);

        // then
        assertThat(foundTask).isEqualTo(task);
    }

    @Test(expected = EntityNotFoundException.class)
    public void when_find_by_id_is_not_found_should_throw_exeception() {
        // Configure o comportamento do repositório simulado
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        taskService.findById(1L);
    }

    @Test
    public void should_list_all_tasks(){
        // given
        Task task1 = new Task();
        task1.setDescription("Descrição de teste para task 1");
        task1.setType(TaskType.LIVRE);
        task1.setPriority(TaskPriority.ALTA);
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setDescription("Descrição de teste para task 2");
        task2.setType(TaskType.LIVRE);
        task2.setPriority(TaskPriority.BAIXA);
        task2.setCompleted(false);

        List<Task> expectedTasks = Arrays.asList(task1, task2);

        // Configure o comportamento do repositório simulado
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // when
        List<Task> actualTasks = taskService.listAll();

        // then
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void should_conclude_a_task() {
        // given
        Task task1 = new Task();
        task1.setDescription("Descrição de teste para task 1");
        task1.setType(TaskType.LIVRE);
        task1.setPriority(TaskPriority.ALTA);
        task1.setCompleted(false);

        // Configure o comportamento do repositório simulado
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        //when
        Task concluded = taskService.conclude(1);

        //then
        assertThat(concluded.getCompleted()).isTrue();
    }
}
