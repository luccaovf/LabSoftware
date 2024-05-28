package com.labdesoft.roteiro1.unit.repository;

import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.enums.TaskPriority;
import com.labdesoft.roteiro1.enums.TaskType;
import com.labdesoft.roteiro1.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void should_find_task_by_id() {
        // given
        Task initTask = new Task();
        initTask.setDescription("Descriacao de teste para task1");
        initTask.setType(TaskType.LIVRE);
        initTask.setPriority(TaskPriority.MEDIA);
        entityManager.persist(initTask);

        // when
        Optional<Task> found = taskRepository.findById(initTask.getId());

        // then
        assertThat(found.get().getDescription()).isEqualTo("Descriacao de teste para task1");
    }

    @Test
    public void should_find_all_tasks() {
        // given
        Task initTask = new Task();
        initTask.setDescription("Descriacao de teste para task1");
        initTask.setType(TaskType.LIVRE);
        initTask.setPriority(TaskPriority.MEDIA);
        entityManager.persist(initTask);

        Task task2 = new Task();
        task2.setDescription("Descriacao de teste para task2");
        task2.setType(TaskType.LIVRE);
        task2.setPriority(TaskPriority.MEDIA);
        entityManager.persist(task2);

        // when
        List<Task> allTasks = taskRepository.findAll();

        // then
        assertThat(allTasks).hasSize(2).contains(task2, initTask);

    }

    @Test
    public void should_store_a_task() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.LIVRE);
        task.setPriority(TaskPriority.MEDIA);

        // when
        Task saved = taskRepository.save(task);

        // then
        assertThat(saved).isEqualTo(task);

    }

    @Test
    public void should_update_a_task() {
        // given
        Task task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.LIVRE);
        task.setPriority(TaskPriority.MEDIA);
        entityManager.persist(task);

        Task updatedTask = new Task();
        updatedTask.setDescription("Nova descriacao de teste para task");
        updatedTask.setPriority(TaskPriority.ALTA);
        updatedTask.setId(task.getId());
        // when
        Task check = taskRepository.save(updatedTask);

        // then
        assertThat(check.getId()).isEqualTo(task.getId());
        assertThat(check.getDescription()).isEqualTo(updatedTask.getDescription());
        assertThat(check.getPriority()).isEqualTo(updatedTask.getPriority());
    }
}
