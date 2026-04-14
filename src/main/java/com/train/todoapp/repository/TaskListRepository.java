package com.train.todoapp.repository;

import com.train.todoapp.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    @Query("""
        SELECT tl FROM TaskList tl
        LEFT JOIN FETCH tl.tasks
        WHERE tl.id = :id
    """)
    Optional<TaskList> findByIdWithTasks(Long id);
}
