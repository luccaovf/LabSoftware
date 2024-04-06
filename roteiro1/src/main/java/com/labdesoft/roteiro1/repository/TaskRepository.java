package com.labdesoft.roteiro1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.labdesoft.roteiro1.entity.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
