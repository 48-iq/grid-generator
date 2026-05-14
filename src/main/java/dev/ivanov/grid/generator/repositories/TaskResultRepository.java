package dev.ivanov.grid.generator.repositories;

import dev.ivanov.grid.generator.entities.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskResultRepository extends JpaRepository<TaskResultEntity, String> {
}
