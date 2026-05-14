package dev.ivanov.grid.generator.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_results")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResultEntity {

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(columnDefinition = "TEXT")
    private String resultJson;
}
